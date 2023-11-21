package com.cmtelematics.cmtreferenceapp.wrappers.crash

import androidx.datastore.core.DataStore
import com.cmtelematics.cmtreferenceapp.common.test.BaseMockkTest
import com.cmtelematics.cmtreferenceapp.common.test.TestMainDispatcherExtension
import com.cmtelematics.cmtreferenceapp.wrappers.authentication.AuthenticationManager
import com.cmtelematics.cmtreferenceapp.wrappers.authentication.AuthenticationManager.AuthenticationState.LoggedIn
import com.cmtelematics.cmtreferenceapp.wrappers.authentication.AuthenticationManager.AuthenticationState.LoggedOut
import com.cmtelematics.cmtreferenceapp.wrappers.crash.model.Crash
import com.cmtelematics.cmtreferenceapp.wrappers.crash.model.CrashWithMetadata
import com.cmtelematics.cmtreferenceapp.wrappers.crash.util.generateFakeCrash
import com.google.common.truth.Truth.assertThat
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.spyk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.time.Clock
import java.time.Instant

internal class CrashManagerImplTest : BaseMockkTest() {

    @MockK
    private lateinit var authenticationManager: AuthenticationManager

    @MockK
    private lateinit var crashUpdateScheduler: CrashUpdateScheduler

    @MockK
    private lateinit var clock: Clock

    @BeforeEach
    fun setup() {
        every { authenticationManager.state } returns flowOf(LoggedIn(0))
        every { clock.instant() } returns Instant.ofEpochMilli(DEFAULT_INSTANT_MILLIS)
        every { clock.millis() } returns DEFAULT_INSTANT_MILLIS
    }

    @Test
    fun `activeCrash is initially null when datastore is empty`() = runTest {
        val sut = createSut()
        val activeCrash = sut.activeCrash.first()
        assertThat(activeCrash).isNull()
    }

    @Test
    fun `firstNotificationTime is initially null when datastore is empty`() = runTest {
        val sut = createSut()
        val firstNotificationTime = sut.firstNotificationTime.first()
        assertThat(firstNotificationTime).isNull()
    }

    @Test
    fun `handleCrashFromBroadcast should update data store with valid crash when user is logged in`() = runTest {
        val crash = generateFakeCrash(clock)
        val dataStore = spyk(FakeDataStore<CrashWithMetadata>())
        val sut = createSut(dataStore)
        sut.handleCrashFromBroadcast(crash)

        coVerify { dataStore.updateData(any()) }
        val updatedCrash = dataStore.data.first()?.crash
        assertThat(updatedCrash).isEqualTo(crash)
    }

    @Test
    fun `handleCrashFromBroadcast should not update data store when user is not logged in`() = runTest {
        every { authenticationManager.state } returns flowOf(LoggedOut)

        val crash = generateFakeCrash(clock)
        val dataStore = spyk(FakeDataStore<CrashWithMetadata>())
        val sut = createSut(dataStore)
        sut.handleCrashFromBroadcast(crash)

        coVerify(exactly = 0) { dataStore.updateData(any()) }
    }

    @Test
    fun `handleCrashFromBroadcast should update current crash when user is logged in and crashes have the same IDs`() =
        runTest {
            val currentCrashWithMetadata = CrashWithMetadata(
                crash = generateFakeCrash(clock).copy(id = DEFAULT_CRASH_ID),
                firstNotificationTime = Instant.ofEpochMilli(2000L)
            )
            val incomingNewCrashWithMetadata = CrashWithMetadata(
                crash = generateFakeCrash(clock).copy(id = DEFAULT_CRASH_ID),
                firstNotificationTime = Instant.ofEpochMilli(3000L)
            )

            val dataStore = spyk(FakeDataStore(currentCrashWithMetadata))
            val sut = createSut(dataStore)
            sut.handleCrashFromBroadcast(incomingNewCrashWithMetadata.crash)

            coVerify { dataStore.updateData(any()) }
            val updatedCrashWithMetadata = dataStore.data.first()
            assertThat(updatedCrashWithMetadata?.crash).isEqualTo(incomingNewCrashWithMetadata.crash)
            assertThat(updatedCrashWithMetadata?.firstNotificationTime)
                .isEqualTo(currentCrashWithMetadata.firstNotificationTime)
        }

    @Test
    fun `handleCrashFromBroadcast should update current crash when user is logged in and crashes have different IDs`() =
        runTest {
            val currentCrashWithMetadata =
                generateCrashWithMetaData(clock).copy(firstNotificationTime = Instant.ofEpochMilli(2000L))
            val incomingNewCrashWithMetadata =
                generateCrashWithMetaData(clock).copy(crash = generateFakeCrash(clock).copy(intensity = 2))

            val dataStore = spyk(FakeDataStore(currentCrashWithMetadata))
            val sut = createSut(dataStore)
            sut.handleCrashFromBroadcast(incomingNewCrashWithMetadata.crash)

            coVerify { dataStore.updateData(any()) }
            val updatedCrashWithMetadata = dataStore.data.first()
            assertThat(updatedCrashWithMetadata?.crash).isEqualTo(incomingNewCrashWithMetadata.crash)
            assertThat(updatedCrashWithMetadata?.firstNotificationTime).isEqualTo(null)
        }

    @Test
    fun `handleCrashFromBroadcast should update current crash when user is logged in and new crash has higher intensity`() =
        runTest {
            val currentCrashWithMetadata = generateCrashWithMetaData(clock)

            val incomingNewCrashWithMetadata = CrashWithMetadata(
                crash = generateFakeCrash(clock).copy(intensity = 2)
            )

            val dataStore = spyk(FakeDataStore(currentCrashWithMetadata))
            val sut = createSut(dataStore)
            sut.handleCrashFromBroadcast(incomingNewCrashWithMetadata.crash)

            coVerify { dataStore.updateData(any()) }
            val updatedCrash = dataStore.data.first()?.crash
            assertThat(updatedCrash).isEqualTo(incomingNewCrashWithMetadata.crash)
        }

    @Test
    fun `handleCrashFromBroadcast should update current crash when user is logged in and new crash is happened after current one`() =
        runTest {
            val currentCrashWithMetadata = CrashWithMetadata(
                crash = generateFakeCrash(clock).copy(timestamp = Instant.now().minusMillis(DEFAULT_INSTANT_MILLIS))
            )

            val incomingNewCrashWithMetadata = CrashWithMetadata(
                crash = generateFakeCrash(clock).copy(timestamp = Instant.now())
            )

            val dataStore = spyk(FakeDataStore(currentCrashWithMetadata))
            val sut = createSut(dataStore)
            sut.handleCrashFromBroadcast(incomingNewCrashWithMetadata.crash)

            coVerify { dataStore.updateData(any()) }
            val updatedCrash = dataStore.data.first()?.crash
            assertThat(updatedCrash).isEqualTo(incomingNewCrashWithMetadata.crash)
        }

    @Test
    fun `handleCrashFromBroadcast should not update current crash when user is logged in and crashes are equals`() =
        runTest {
            val currentCrashWithMetadata = generateCrashWithMetaData(clock)
            val incomingNewCrashWithMetadata = generateCrashWithMetaData(clock)

            val dataStore = spyk(FakeDataStore(currentCrashWithMetadata))
            val sut = createSut(dataStore)
            sut.handleCrashFromBroadcast(incomingNewCrashWithMetadata.crash)

            coVerify(exactly = 0) { dataStore.updateData(any()) }
        }

    @Test
    fun `simulateCrash should update data store with new crash when user is logged in and crash is valid`() = runTest {
        val dataStore = spyk(FakeDataStore<CrashWithMetadata>())
        val sut = createSut(dataStore)
        sut.simulateCrash()

        coVerify { dataStore.updateData(any()) }
    }

    @Test
    fun `simulateCrash should not update data store with new crash when user is logged out and crash is valid`() =
        runTest {
            every { authenticationManager.state } returns flowOf(LoggedOut)
            val dataStore = spyk(FakeDataStore<CrashWithMetadata>())
            val sut = createSut(dataStore)
            sut.simulateCrash()

            coVerify(exactly = 0) { dataStore.updateData(any()) }
        }

    @Test
    fun `simulateCrash should not update data store with new crash when user is logged in and crash is not verified`() =
        runTest {
            val crash: Crash = mockk()
            every { crash.verified } returns false

            mockkStatic(::generateFakeCrash)
            every { generateFakeCrash(any()) } returns crash
            val dataStore = spyk(FakeDataStore<CrashWithMetadata>())
            val sut = createSut(dataStore)
            sut.simulateCrash()

            coVerify(exactly = 0) { dataStore.updateData(any()) }
        }

    @Test
    fun `simulateCrash should not update data store with new crash when user is logged in and intensity is 0 `() =
        runTest {
            val crash: Crash = mockk()
            every { crash.verified } returns true
            every { crash.intensity } returns 0

            mockkStatic(::generateFakeCrash)
            every { generateFakeCrash(any()) } returns crash

            val dataStore = spyk(FakeDataStore<CrashWithMetadata>())
            val sut = createSut(dataStore)
            sut.simulateCrash()

            coVerify(exactly = 0) { dataStore.updateData(any()) }
        }

    @Test
    fun `notifyFlowStarted should update current crash when datastore is not empty`() = runTest {
        val crashWithMetadata: CrashWithMetadata = mockk()
        val crash: Crash = mockk()

        every { crashWithMetadata.flowStarted } returns false
        every { crashWithMetadata.crash } returns crash

        every {
            crashWithMetadata.copy(
                crash = any(),
                firstNotificationTime = any(),
                flowStarted = true
            )
        } returns CrashWithMetadata(
            crash = crash,
            firstNotificationTime = null,
            flowStarted = true
        )

        val dataStore = spyk(FakeDataStore(crashWithMetadata))
        val sut = createSut(dataStore)
        sut.notifyFlowStarted()

        coVerify { dataStore.updateData(any()) }

        val flowStarted = dataStore.data.first()?.flowStarted
        assertThat(flowStarted).isEqualTo(true)
    }

    @Test
    fun `handleUserSelection should call scheduleCrashUpdate method when there is an active crash`() = runTest {
        val crashWithMetadata: CrashWithMetadata = mockk()
        val crash: Crash = mockk()
        every { crash.id } returns DEFAULT_CRASH_ID
        every { crashWithMetadata.crash } returns crash

        val dataStore = spyk(FakeDataStore(crashWithMetadata))
        val sut = createSut(dataStore)
        sut.handleUserSelection(crashConfirmed = true, shouldSendAmbulance = true)

        coVerify { crashUpdateScheduler.scheduleCrashUpdate(any(), any(), any()) }
        coVerify { dataStore.updateData(any()) }
        val updatedCrashWithMetadata = dataStore.data.first()
        assertThat(updatedCrashWithMetadata).isNull()
    }

    @Test
    fun `handleUserSelection should not call scheduleCrashUpdate method when there is no active crash`() = runTest {
        val dataStore = spyk(FakeDataStore<CrashWithMetadata>())
        val sut = createSut(dataStore)
        sut.handleUserSelection(crashConfirmed = true, shouldSendAmbulance = true)

        coVerify(exactly = 0) { crashUpdateScheduler.scheduleCrashUpdate(any(), any(), any()) }
        coVerify { dataStore.updateData(any()) }
        val updatedCrashWithMetadata = dataStore.data.first()
        assertThat(updatedCrashWithMetadata).isNull()
    }

    private fun createSut(dataStore: DataStore<CrashWithMetadata?> = FakeDataStore()) = CrashManagerImpl(
        authenticationManager = { authenticationManager },
        crashUpdateScheduler = crashUpdateScheduler,
        dataStore = dataStore,
        clock = clock
    )

    private fun generateCrashWithMetaData(clock: Clock) = CrashWithMetadata(
        crash = generateFakeCrash(clock)
    )

    companion object {
        private const val DEFAULT_INSTANT_MILLIS = 1000L
        private const val DEFAULT_CRASH_ID = 111L
    }
}
