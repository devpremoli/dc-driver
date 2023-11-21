package com.cmtelematics.cmtreferenceapp.crash.model

import androidx.lifecycle.viewModelScope
import com.cmtelematics.cmtreferenceapp.common.service.ErrorService
import com.cmtelematics.cmtreferenceapp.common.util.getTicker
import com.cmtelematics.cmtreferenceapp.common.viewmodel.BaseScreenViewModel
import com.cmtelematics.cmtreferenceapp.navigation.NavigationOptions
import com.cmtelematics.cmtreferenceapp.navigation.Navigator
import com.cmtelematics.cmtreferenceapp.navigation.Route
import com.cmtelematics.cmtreferenceapp.navigation.route.crash.AmbulanceCancelledRoute
import com.cmtelematics.cmtreferenceapp.navigation.route.crash.AmbulanceRequestedRoute
import com.cmtelematics.cmtreferenceapp.navigation.route.crash.CrashDetectedCountDownRoute
import com.cmtelematics.cmtreferenceapp.navigation.route.crash.CrashDetectedNegativeRoute
import com.cmtelematics.cmtreferenceapp.wrappers.crash.CrashManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.time.Clock
import java.time.Duration
import java.time.Instant
import javax.inject.Inject

@HiltViewModel
internal class CrashDetectedCountDownViewModel @Inject constructor(
    navigator: Navigator,
    errorService: ErrorService,
    private val crashManager: CrashManager,
    private val clock: Clock
) : BaseScreenViewModel(navigator, errorService) {
    private val mutableShouldShowAmbulanceConfirmDialog = MutableStateFlow(false)

    val shouldShowAmbulanceConfirmDialog = mutableShouldShowAmbulanceConfirmDialog.asStateFlow()
    val remainingSeconds = combine(getTicker(), crashManager.firstNotificationTime) { _, firstNotificationTime ->
        getRemainingSeconds(firstNotificationTime).coerceAtLeast(0)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = CRASH_COUNTDOWN_ALLOWED_SECONDS
    )

    val remainingCountDownTimeProgress = remainingSeconds.map { -it.toFloat() / CRASH_COUNTDOWN_ALLOWED_SECONDS }
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(),
            initialValue = DEFAULT_REMAINING_COUNT_DOWN_TIME_PROGRESS
        )

    init {
        launchWithErrorHandling {
            crashManager.notifyFlowStarted()
        }
    }

    private fun getRemainingSeconds(firstNotificationTime: Instant?): Int =
        firstNotificationTime?.let { notificationSentTime ->
            val elapsedSeconds = Duration.between(notificationSentTime, clock.instant()).seconds.toInt()
            CRASH_COUNTDOWN_ALLOWED_SECONDS - elapsedSeconds
        } ?: 0

    fun showAmbulanceConfirmDialog() {
        mutableShouldShowAmbulanceConfirmDialog.value = true
    }

    fun callAmbulance() = respondToCrash(
        crashConfirmed = true,
        shouldSendAmbulance = true,
        navigateRoute = AmbulanceRequestedRoute()
    )

    fun cancelAmbulance() = respondToCrash(
        crashConfirmed = true,
        shouldSendAmbulance = false,
        navigateRoute = AmbulanceCancelledRoute()
    )

    fun crashDetectedNegative() = respondToCrash(
        crashConfirmed = false,
        shouldSendAmbulance = false,
        navigateRoute = CrashDetectedNegativeRoute()
    )

    private fun respondToCrash(
        crashConfirmed: Boolean,
        shouldSendAmbulance: Boolean,
        navigateRoute: Route
    ) = launchWithErrorHandling {
        crashManager.handleUserSelection(
            crashConfirmed = crashConfirmed,
            shouldSendAmbulance = shouldSendAmbulance
        )
        mutableShouldShowAmbulanceConfirmDialog.value = false
        navigator.navigateTo(
            route = navigateRoute,
            navigationOptions = NavigationOptions(popUpTo = CrashDetectedCountDownRoute, popUpToInclusive = true)
        )
    }

    companion object {
        private const val DEFAULT_REMAINING_COUNT_DOWN_TIME_PROGRESS = -1f
        private const val CRASH_COUNTDOWN_ALLOWED_SECONDS = 60
    }
}
