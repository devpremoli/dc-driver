package com.cmtelematics.cmtreferenceapp.manager

import android.media.AudioManager
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.cmtelematics.cmtreferenceapp.common.service.DispatcherProvider
import com.google.android.exoplayer2.ExoPlayer

@Suppress("Deprecation")
internal class Android7AlertManager(
    private val audioManager: AudioManager,
    dispatcherProvider: DispatcherProvider,
    exoPlayer: ExoPlayer,
    dataStore: DataStore<Preferences>
) : BaseAlertManager(
    audioManager = audioManager,
    dispatcherProvider = dispatcherProvider,
    exoPlayer = exoPlayer,
    dataStore = dataStore
) {

    override val streamType = AudioManager.STREAM_MUSIC

    private val focusChangeListener = AudioManager.OnAudioFocusChangeListener {
        exoPlayer.pause()
    }

    override fun requestAudioFocus(): Int = audioManager.requestAudioFocus(
        focusChangeListener,
        streamType,
        AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK
    )

    override fun abandonAudioFocus() {
        audioManager.abandonAudioFocus(focusChangeListener)
    }
}
