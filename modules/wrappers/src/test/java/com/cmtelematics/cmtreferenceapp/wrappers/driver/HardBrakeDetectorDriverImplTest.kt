package com.cmtelematics.cmtreferenceapp.wrappers.driver

import com.cmtelematics.cmtreferenceapp.common.manager.AlertManager
import com.cmtelematics.cmtreferenceapp.common.manager.AlertType.HardBraking
import com.cmtelematics.cmtreferenceapp.common.manager.StartupManager
import com.cmtelematics.cmtreferenceapp.common.test.BaseMockkTest
import com.cmtelematics.cmtreferenceapp.wrappers.analytics.AnalyticsManager
import com.cmtelematics.sdk.hardbrake.HardBrakeDetector
import com.cmtelematics.sdk.hardbrake.HardBrakeEvent
import com.cmtelematics.sdk.hardbrake.HardBrakeEventListener
import com.google.common.truth.Truth.assertThat
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class HardBrakeDetectorDriverImplTest : BaseMockkTest() {

    @MockK
    private lateinit var hardBrakeDetector: HardBrakeDetector

    @MockK
    private lateinit var alertManager: AlertManager

    @MockK
    private lateinit var analyticsManager: AnalyticsManager

    @MockK
    private lateinit var startupManager: StartupManager

    @BeforeEach
    fun setup() {
        every { hardBrakeDetector.setListener(any()) } coAnswers {
            delay(500) // Do not immediately trigger, give sharing time to complete
            firstArg<HardBrakeEventListener>().onHardBrake(HardBrakeEvent(mockk(), ""))
        }
        justRun { hardBrakeDetector.setListener(null) }
        every { startupManager.isAppInitialised() } returns flowOf(true)
    }

    @Test
    fun `run should throws an IllegalStateException when state of alert manager is empty`() = runTest {
        every { alertManager.state } returns flowOf(emptyMap())
        val sut = createSut()

        val exception = assertThrows<IllegalStateException> {
            sut.run()
        }
        assertThat(exception.message).isEqualTo("$HardBraking alert type is not found in alert states!")
    }

    @Test
    fun `run should not play alert when hard brake alert is not enabled and hard brake occurred`() =
        runTest {
            every { alertManager.state } returns flowOf(mapOf(HardBraking to false))

            val sut = createSut()
            val collectJob = launch(UnconfinedTestDispatcher()) { sut.run() }

            advanceUntilIdle()

            coVerify(exactly = 0) { alertManager.playAlert(any()) }

            val enabledSlot = slot<Boolean>()
            coVerify { analyticsManager.logAlertPlayback(any(), any(), capture(enabledSlot)) }
            assertThat(enabledSlot.captured).isFalse()

            coVerify { analyticsManager.logAlertSetting(any(), any(), capture(enabledSlot)) }
            assertThat(enabledSlot.captured).isFalse()

            collectJob.cancel()

            coVerify { hardBrakeDetector.setListener(null) }
        }

    @Test
    fun `run should play alert event when hard brake alert is enabled and and hard brake occurred`() = runTest {
        every { alertManager.state } returns flowOf(mapOf(HardBraking to true))

        val sut = createSut()
        val collectJob = launch(UnconfinedTestDispatcher()) { sut.run() }

        advanceUntilIdle()

        coVerify { alertManager.playAlert(any()) }

        val enabledSlot = slot<Boolean>()
        coVerify { analyticsManager.logAlertPlayback(any(), any(), capture(enabledSlot)) }
        assertThat(enabledSlot.captured).isTrue()

        coVerify { analyticsManager.logAlertSetting(any(), any(), capture(enabledSlot)) }
        assertThat(enabledSlot.captured).isTrue()

        collectJob.cancel()

        coVerify { hardBrakeDetector.setListener(null) }
    }

    private fun createSut() = HardBrakeDetectorDriverImpl(
        hardBrakeDetector = hardBrakeDetector,
        alertManager = alertManager,
        analyticsManager = analyticsManager,
        startupManager = startupManager
    )
}
