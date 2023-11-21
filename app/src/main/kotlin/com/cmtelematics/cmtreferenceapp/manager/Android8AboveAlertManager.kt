package com.cmtelematics.cmtreferenceapp.manager

import android.media.AudioFocusRequest
import android.media.AudioManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.cmtelematics.cmtreferenceapp.common.service.DispatcherProvider
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.audio.AudioAttributes

@RequiresApi(Build.VERSION_CODES.O)
internal class Android8AboveAlertManager(
    private val audioManager: AudioManager,
    dispatcherProvider: DispatcherProvider,
    private val audioAttributes: AudioAttributes,
    exoPlayer: ExoPlayer,
    dataStore: DataStore<Preferences>
) : BaseAlertManager(
    audioManager = audioManager,
    dispatcherProvider = dispatcherProvider,
    exoPlayer = exoPlayer,
    dataStore = dataStore
) {

    override val streamType = audioAttributes.audioAttributesV21.audioAttributes.volumeControlStream

    private val focusRequest = AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK).run {
        setAudioAttributes(audioAttributes.audioAttributesV21.audioAttributes)
        setAcceptsDelayedFocusGain(false)
        build()
    }

    override fun requestAudioFocus(): Int = audioManager.requestAudioFocus(focusRequest)

    override fun abandonAudioFocus() {
        audioManager.abandonAudioFocusRequest(focusRequest)
    }
}
