package com.cmtelematics.cmtreferenceapp.wrappers.analytics

import android.media.AudioDeviceInfo
import android.media.AudioDeviceInfo.TYPE_AUX_LINE
import android.media.AudioDeviceInfo.TYPE_BLE_HEADSET
import android.media.AudioDeviceInfo.TYPE_BLUETOOTH_A2DP
import android.media.AudioDeviceInfo.TYPE_BUILTIN_SPEAKER
import android.media.AudioDeviceInfo.TYPE_BUILTIN_SPEAKER_SAFE
import android.media.AudioDeviceInfo.TYPE_BUS
import android.media.AudioDeviceInfo.TYPE_WIRED_HEADPHONES
import android.media.AudioDeviceInfo.TYPE_WIRED_HEADSET
import android.media.AudioManager
import com.cmtelematics.cmtreferenceapp.common.manager.AlertType
import com.cmtelematics.cmtreferenceapp.common.manager.AlertType.HardBraking
import com.cmtelematics.cmtreferenceapp.common.test.BaseMockkTest
import com.cmtelematics.cmtreferenceapp.wrappers.analytics.util.getAlertAnalyticsCategory
import com.cmtelematics.cmtreferenceapp.wrappers.analytics.util.getDevicePlaybackLabel
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class AlertAnalyticsUtilTest : BaseMockkTest() {

    @MockK
    private lateinit var audioManager: AudioManager

    @MockK
    private lateinit var audioDeviceInfo: AudioDeviceInfo

    @BeforeEach
    fun setup() {
        every { audioManager.getDevices(AudioManager.GET_DEVICES_OUTPUTS) } returns arrayOf(audioDeviceInfo)
    }

    @Test
    fun `getAlertAnalyticsCategory returns hard braking analytics category when HardBraking is the actual alert type`() {
        val actualCategory = HardBraking.getAlertAnalyticsCategory()
        assertThat(actualCategory).isEqualTo("hardbrake_alerts")
    }

    @Test
    fun `getAlertAnalyticsCategory throws RuntimeException when actual alert type is unsupported`() {
        val alertType = mockk<AlertType>()
        val exception = assertThrows<RuntimeException> { alertType.getAlertAnalyticsCategory() }
        assertThat(exception.message).isEqualTo("Unsupported alert type: $alertType")
    }

    @Test
    fun `getDevicePlaybackLabel returns headphones analytics label when the sound output is a wired headphone`() {
        val expectedLabel = "headphones"

        every { audioDeviceInfo.type } returns TYPE_WIRED_HEADPHONES
        assertThat( getDevicePlaybackLabel(audioManager)).isEqualTo(expectedLabel)

        every { audioDeviceInfo.type } returns TYPE_WIRED_HEADSET
        assertThat( getDevicePlaybackLabel(audioManager)).isEqualTo(expectedLabel)
    }

     @Test
     fun `getDevicePlaybackLabel returns speakerPhone analytics label when the sound output is the speaker of device`() {
         val expectedLabel = "speakerPhone"

         every { audioDeviceInfo.type } returns TYPE_BUILTIN_SPEAKER
         assertThat(getDevicePlaybackLabel(audioManager)).isEqualTo(expectedLabel)

         every { audioDeviceInfo.type } returns TYPE_BUILTIN_SPEAKER_SAFE
         assertThat( getDevicePlaybackLabel(audioManager)).isEqualTo(expectedLabel)
     }

    @Test
    fun `getDevicePlaybackLabel returns bluetoothA2dp analytics label when the sound output is Bluetooth A2DP`() {
        val expectedLabel = "bluetoothA2dp"

        every { audioDeviceInfo.type } returns TYPE_BLUETOOTH_A2DP
        assertThat(getDevicePlaybackLabel(audioManager)).isEqualTo(expectedLabel)
    }

    @Test
    fun `getDevicePlaybackLabel returns bluetoothHeadset analytics label when the sound output is BLE headset`() {
        val expectedLabel = "bluetoothHeadset"

        every { audioDeviceInfo.type } returns TYPE_BLE_HEADSET
        assertThat(getDevicePlaybackLabel(audioManager)).isEqualTo(expectedLabel)
    }

    @Test
    fun `getDevicePlaybackLabel returns androidAuto analytics label when the sound output is Android Auto`() {
        val expectedLabel = "androidAuto"

        every { audioDeviceInfo.type } returns TYPE_AUX_LINE
        assertThat(getDevicePlaybackLabel(audioManager)).isEqualTo(expectedLabel)
    }

    @Test
    fun `getDevicePlaybackLabel returns default analytics label when the sound output is cannot be categorized`() {
        val expectedLabel = "default"

        every { audioDeviceInfo.type } returns TYPE_BUS
        assertThat(getDevicePlaybackLabel(audioManager)).isEqualTo(expectedLabel)
    }
}
