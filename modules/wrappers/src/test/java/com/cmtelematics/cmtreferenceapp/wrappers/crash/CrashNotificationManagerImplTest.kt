package com.cmtelematics.cmtreferenceapp.wrappers.crash

import android.app.Activity
import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.service.notification.StatusBarNotification
import androidx.core.app.AlarmManagerCompat
import androidx.core.app.NotificationCompat
import androidx.datastore.core.DataStore
import com.cmtelematics.cmtreferenceapp.common.test.BaseMockkTest
import com.cmtelematics.cmtreferenceapp.common.util.createActivityPendingIntent
import com.cmtelematics.cmtreferenceapp.common.util.getCompatNotificationBuilder
import com.cmtelematics.cmtreferenceapp.wrappers.crash.model.CrashWithMetadata
import com.cmtelematics.cmtreferenceapp.wrappers.holder.WrapperNotificationParamsHolder
import com.cmtelematics.cmtreferenceapp.wrappers.service.model.WrapperNotificationParams
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.mockkStatic
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.Clock
import java.time.Instant

class CrashNotificationManagerImplTest : BaseMockkTest() {

    @MockK
    private lateinit var context: Context

    @MockK
    private lateinit var clock: Clock

    @MockK
    private lateinit var alarmManager: AlarmManager

    @MockK
    private lateinit var notificationManager: NotificationManager

    @MockK
    private lateinit var notificationParamsHolder: WrapperNotificationParamsHolder

    @MockK
    private lateinit var activity: Activity

    @RelaxedMockK
    private lateinit var pendingIntent: PendingIntent

    @RelaxedMockK
    private lateinit var notificationBuilder: NotificationCompat.Builder

    @BeforeEach
    fun setup() {
        every { context.getString(any()) } returns ""

        mockkStatic("com.cmtelematics.cmtreferenceapp.common.util.IntentUtilKt")
        every { context.createActivityPendingIntent(activity::class.java, any()) } returns pendingIntent
        every { clock.instant() } returns Instant.ofEpochMilli(DEFAULT_INSTANT_MILLIS)
        every { clock.millis() } returns DEFAULT_INSTANT_MILLIS

        every { notificationParamsHolder.notificationParams } returns WrapperNotificationParams(
            notificationIcon = 0,
            notificationTitle = 0,
            appNameRes = 0,
            mainActivityClass = activity::class.java
        )

        mockkStatic(::getCompatNotificationBuilder)
        every { getCompatNotificationBuilder(any(), any()) } returns notificationBuilder

        mockkConstructor(Intent::class)
        every { anyConstructed<Intent>().setAction(any()) } returns mockk()

        mockkStatic(PendingIntent::class)
        every { PendingIntent.getBroadcast(any(), any(), any(), any()) } returns mockk()

        mockkConstructor(Instant::class)
        every { anyConstructed<Instant>().plusMillis(any()) } returns Instant.ofEpochMilli(DEFAULT_INSTANT_MILLIS)
    }

    @Test
    fun `handleNotificationRetryAlarmFired fires crash notification when notificationParams of notificationParamsHolder is not null`() =
        runTest {
            val sut = createSut()
            sut.handleNotificationRetryAlarmFired()

            verify { notificationManager.notify(any(), any(), any()) }
        }

    @Test
    fun `handleNotificationRetryAlarmFired does not fire crash notification when notificationParams of notificationParamsHolder is null`() =
        runTest {
            every { notificationParamsHolder.notificationParams } returns null

            val sut = createSut()
            sut.handleNotificationRetryAlarmFired()

            verify(exactly = 0) { notificationManager.notify(any(), any(), any()) }
        }

    @Test
    fun `maintainNotificationScheduling cancels all notifications when crash is null and there is active notification`() =
        runTest {
            val statusBarNotification = mockk<StatusBarNotification>(relaxed = true)
            every { notificationParamsHolder.notificationParams } returns null
            every { statusBarNotification.tag } returns NOTIFICATION_TAG
            every { notificationManager.activeNotifications } returns arrayOf(statusBarNotification)

            val sut = createSut()
            val collectJob = launch(UnconfinedTestDispatcher()) { sut.maintainNotificationScheduling() }

            verify(atLeast = 1) { notificationManager.cancel(any(), any()) }
            collectJob.cancel()
        }

    @Test
    fun `maintainNotificationScheduling fires crash notification and recreate retry notification when firstNotificationTime of crash is null`() =
        runTest {
            val crashWithMetadata = mockk<CrashWithMetadata>()
            every { crashWithMetadata.firstNotificationTime } returns null
            every { crashWithMetadata.crash } returns mockk(relaxed = true)
            every { crashWithMetadata.copy(any(), any(), any()) } returns crashWithMetadata

            mockkStatic(AlarmManagerCompat::class)

            val fakeDataStore = spyk(FakeDataStore<CrashWithMetadata?>(crashWithMetadata))
            val sut = createSut(fakeDataStore)
            val collectJob = launch(UnconfinedTestDispatcher()) { sut.maintainNotificationScheduling() }

            verify { notificationManager.notify(any(), any(), any()) }
            verify { AlarmManagerCompat.setAndAllowWhileIdle(any(), any(), any(), any()) }
            coVerify { fakeDataStore.updateData(any()) }

            collectJob.cancel()
        }

    @Test
    fun `maintainNotificationScheduling only recreate retry notification when firstNotificationTime of crash is not null`() =
        runTest {
            val crashWithMetadata = mockk<CrashWithMetadata>()
            every { crashWithMetadata.firstNotificationTime } returns Instant.ofEpochMilli(DEFAULT_INSTANT_MILLIS)

            mockkConstructor(Instant::class)
            every {
                anyConstructed<Instant>().plusMillis(any())
            } returns crashWithMetadata.firstNotificationTime?.plusMillis(DEFAULT_INSTANT_MILLIS)
            every { crashWithMetadata.crash } returns mockk(relaxed = true)

            mockkStatic(AlarmManagerCompat::class)

            val fakeDataStore = spyk(FakeDataStore<CrashWithMetadata?>(crashWithMetadata))
            val sut = createSut(fakeDataStore)
            val collectJob = launch(UnconfinedTestDispatcher()) { sut.maintainNotificationScheduling() }

            verify(exactly = CrashNotificationManager.RetryNotificationType.values().size) {
                alarmManager.cancel(any<PendingIntent>())
            }
            verify { AlarmManagerCompat.setAndAllowWhileIdle(any(), any(), any(), any()) }
            verify(exactly = 0) { notificationManager.notify(any(), any(), any()) }
            coVerify(exactly = 0) { fakeDataStore.updateData(any()) }

            collectJob.cancel()
        }

    @Test
    fun `maintainNotificationScheduling only cancels alarms but not recreate retry notification when firstNotificationTime of crash is not null`() =
        runTest {
            val crashWithMetadata = mockk<CrashWithMetadata>()
            every { crashWithMetadata.firstNotificationTime } returns Instant.ofEpochMilli(DEFAULT_INSTANT_MILLIS)
            every { crashWithMetadata.crash } returns mockk(relaxed = true)

            mockkStatic(AlarmManagerCompat::class)

            val fakeDataStore = spyk(FakeDataStore<CrashWithMetadata?>(crashWithMetadata))
            val sut = createSut(fakeDataStore)
            val collectJob = launch(UnconfinedTestDispatcher()) { sut.maintainNotificationScheduling() }

            verify(exactly = CrashNotificationManager.RetryNotificationType.values().size) {
                alarmManager.cancel(any<PendingIntent>())
            }
            verify(exactly = 0) { AlarmManagerCompat.setAndAllowWhileIdle(any(), any(), any(), any()) }
            verify(exactly = 0) { notificationManager.notify(any(), any(), any()) }
            coVerify(exactly = 0) { fakeDataStore.updateData(any()) }

            collectJob.cancel()
        }

    private fun createSut(dataStore: DataStore<CrashWithMetadata?> = FakeDataStore()) =
        CrashNotificationManagerImpl(
            context = context,
            clock = clock,
            alarmManager = alarmManager,
            notificationManager = notificationManager,
            notificationParamsHolder = notificationParamsHolder,
            dataStore = dataStore
        )

    companion object {
        private const val DEFAULT_INSTANT_MILLIS = 1000L
        private const val NOTIFICATION_TAG = "RefAppCrashNotifications"
    }
}
