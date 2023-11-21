package com.cmtelematics.cmtreferenceapp.manager

import android.media.AudioManager
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.cmtelematics.cmtreferenceapp.common.manager.AlertManager
import com.cmtelematics.cmtreferenceapp.common.manager.AlertType
import com.cmtelematics.cmtreferenceapp.common.service.DispatcherProvider
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.upstream.RawResourceDataSource
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import timber.log.Timber

internal abstract class BaseAlertManager(
    private val audioManager: AudioManager,
    private val dispatcherProvider: DispatcherProvider,
    private val exoPlayer: ExoPlayer,
    private val dataStore: DataStore<Preferences>
) : AlertManager {

    override val state = dataStore.data.map { prefs ->
        alertTypes.associateWith { alertType ->
            val preferenceKey = booleanPreferencesKey(alertType.name)
            val isEnabled = prefs[preferenceKey] == true
            isEnabled
        }
    }.flowOn(dispatcherProvider.default)

    private val alertTypes: MutableList<AlertType> = mutableListOf()

    private var defaultVolume: Int = 0
    private var currentVolume: Int = 0

    abstract val streamType: Int

    abstract fun requestAudioFocus(): Int

    abstract fun abandonAudioFocus()

    override suspend fun initialize(alerts: Map<AlertType, Int>) = withContext(dispatcherProvider.main) {
        exoPlayer.clearMediaItems()
        alertTypes.clear()

        alerts.forEach { (type, rawRes) ->
            val uri = RawResourceDataSource.buildRawResourceUri(rawRes)
            exoPlayer.addMediaItem(MediaItem.fromUri(uri))
            alertTypes.add(type)
        }

        exoPlayer.apply {
            addListener(object : Player.Listener {
                override fun onIsPlayingChanged(isPlaying: Boolean) {
                    super.onIsPlayingChanged(isPlaying)
                    if (!isPlaying) {
                        abandonAudioFocus()
                        changeVolume(currentVolume)
                    }
                }

                // One of the requirement is that we need to set a default volume if the phone is muted or at 0 volume.
                // This is an artificial volume change, which does not represent the actual device volume.
                override fun onDeviceVolumeChanged(volume: Int, muted: Boolean) {
                    super.onDeviceVolumeChanged(volume, muted)
                    val isArtificialChange = currentVolume == 0 && volume == defaultVolume
                    if (!isArtificialChange) {
                        currentVolume = volume
                    }
                }
            })
            prepare()
            pauseAtEndOfMediaItems = true
            playWhenReady = false
        }

        defaultVolume = (exoPlayer.deviceInfo.maxVolume * DEFAULT_VOLUME_RATIO).toInt()
        currentVolume = exoPlayer.deviceVolume
    }

    override suspend fun enableAudioAlerts(alertTypes: Array<AlertType>, enabled: Boolean) {
        dataStore.edit { preferences ->
            alertTypes.forEach { alertType ->
                val preferenceKey = booleanPreferencesKey(alertType.name)
                preferences[preferenceKey] = enabled
            }
        }
        Timber.d(
            "State of $alertTypes is changed to: %s",
            if (enabled) {
                "enabled"
            } else {
                "disabled"
            }
        )
    }

    override suspend fun playAlert(alertType: AlertType) = withContext(dispatcherProvider.main) {
        if (exoPlayer.isPlaying) return@withContext

        exoPlayer.seekTo(alertTypes.indexOf(alertType), C.TIME_UNSET)

        if (requestAudioFocus() == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            val isSilent = exoPlayer.isDeviceMuted || exoPlayer.deviceVolume == 0

            if (isSilent) {
                changeVolume(defaultVolume)
            }

            // Sometimes the audio starts playing at the same time the other audio loses focus.
            // To prevent audio overlapping we decided to add a small delay before we play the alert.
            delay(AUDIO_START_DELAY)

            exoPlayer.play()
        } else {
            Timber.d("Focus request denied.")
        }
    }

    override suspend fun stop() = withContext(dispatcherProvider.main) {
        exoPlayer.stop()
        abandonAudioFocus()
        changeVolume(currentVolume)
    }

    private fun changeVolume(volume: Int) {
        audioManager.setStreamVolume(streamType, volume, 0)
    }

    companion object {
        private const val AUDIO_START_DELAY = 500L
        private const val DEFAULT_VOLUME_RATIO = .7f
    }
}
