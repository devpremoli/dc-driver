package com.cmtelematics.cmtreferenceapp.wrappers.authentication

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import com.cmtelematics.cmtreferenceapp.common.service.ErrorService
import com.cmtelematics.cmtreferenceapp.common.test.BaseMockkTest
import com.cmtelematics.cmtreferenceapp.wrappers.authentication.AuthenticationManager.AuthenticationState.LoggedIn
import com.cmtelematics.cmtreferenceapp.wrappers.authentication.AuthenticationManager.AuthenticationState.LoggedOut
import com.cmtelematics.cmtreferenceapp.wrappers.getTestDispatcherProvider
import com.cmtelematics.sdk.PassThruRequester
import com.cmtelematics.sdk.UserManager
import com.cmtelematics.sdk.types.CoreProfile
import com.cmtelematics.sdk.types.RegisterResponse
import com.google.common.truth.Truth.assertThat
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.reactivex.Observer
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.plus
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File

internal class AuthenticationManagerImplTest : BaseMockkTest() {

    @MockK
    private lateinit var userManager: UserManager

    @MockK
    private lateinit var errorService: ErrorService

    @MockK
    private lateinit var passThruRequester: PassThruRequester

    private lateinit var dataStore: DataStore<Preferences>

    @BeforeEach
    fun setup(@TempDir tempDir: File) {
        dataStore = PreferenceDataStoreFactory.create { File(tempDir, AUTH_MANAGER_PREF_FILE_NAME) }
    }

    @Test
    fun `profile is initially null when user is not logged in`() = runTest {
        every { userManager.subscribe(any()) } answers { firstArg<Observer<Long>>().onNext(LOGGED_OUT_USER_ID) }
        val job = Job()
        val sut = createSut(job)

        val profile = sut.profile.first()
        assertThat(profile).isNull()
        job.cancel()
    }

    @Test
    fun `state is initially LoggedOut when user is not logged in`() = runTest {
        every { userManager.subscribe(any()) } answers { firstArg<Observer<Long>>().onNext(LOGGED_OUT_USER_ID) }
        val job = Job()
        val sut = createSut(job)

        val state = sut.state.first()
        assertThat(state).isEqualTo(LoggedOut)

        job.cancel()
    }

    @Test
    fun `profile is not null initially when user is logged in`() = runTest {
        every { userManager.subscribe(any()) } answers { firstArg<Observer<Long>>().onNext(DEFAULT_USER_ID) }
        val dummyCoreProfile = CoreProfile().apply {
            tagUser = true
            email = ""
        }
        every { userManager.getProfile(any()) } answers {
            firstArg<Observer<CoreProfile>>().onNext(dummyCoreProfile);true
        }

        val job = Job()
        val sut = createSut(job)

        val profile = sut.profile.first()
        assertThat(profile).isNotNull()
        job.cancel()
    }

    @Test
    fun `state is initially LoggedIn when user is logged in`() = runTest {
        every { userManager.subscribe(any()) } answers { firstArg<Observer<Long>>().onNext(DEFAULT_USER_ID) }
        val job = Job()
        val sut = createSut(job)

        val state = sut.state.first()
        assertThat(state).isEqualTo(LoggedIn(shortUserId = DEFAULT_USER_ID))

        job.cancel()
    }

    @Test
    fun `register should emit a user profile when user is not logged in`() = runTest {
        val dummyCoreProfile = CoreProfile().apply {
            tagUser = true
            email = ""
        }
        val dummyRegisterResponse = RegisterResponse(null, null, false, dummyCoreProfile)
        every { userManager.subscribe(any()) } answers { firstArg<Observer<Long>>().onNext(LOGGED_OUT_USER_ID) }
        every { userManager.register(any(), any()) } answers {
            secondArg<Observer<RegisterResponse<CoreProfile>>>().onNext(dummyRegisterResponse);true
        }
        every { userManager.getProfile(any()) } answers {
            firstArg<Observer<CoreProfile>>().onNext(dummyCoreProfile);true
        }

        val job = Job()
        val sut = createSut(job)

        val loggedOutProfile = sut.profile.first()
        assertThat(loggedOutProfile).isNull()

        val coreProfile = sut.register("", false)
        coVerify { userManager.getProfile(any()) }
        assertThat(coreProfile).isSameInstanceAs(dummyCoreProfile)

        val registeredProfile = sut.profile.first()
        assertThat(registeredProfile).isNotNull()

        job.cancel()
    }

    private fun TestScope.createSut(job: Job) =
        AuthenticationManagerImpl(
            userManager = userManager,
            sharingScope = this + UnconfinedTestDispatcher() + job,
            driverScope = this + UnconfinedTestDispatcher() + job,
            errorService = errorService,
            dataStore = dataStore,
            passThruRequester = passThruRequester,
            dispatcherProvider = getTestDispatcherProvider(StandardTestDispatcher(testScheduler))
        )

    companion object {
        private const val AUTH_MANAGER_PREF_FILE_NAME = "auth_pref.preferences_pb"
        private const val LOGGED_OUT_USER_ID = 0L
        private const val DEFAULT_USER_ID = 123L
    }
}
