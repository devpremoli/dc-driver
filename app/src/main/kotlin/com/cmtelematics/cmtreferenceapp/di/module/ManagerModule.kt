package com.cmtelematics.cmtreferenceapp.di.module

import android.media.AudioManager
import android.os.Build
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.cmtelematics.cmtreferenceapp.common.di.qualifier.SessionDataStore
import com.cmtelematics.cmtreferenceapp.common.manager.AlertManager
import com.cmtelematics.cmtreferenceapp.common.manager.DriverManager
import com.cmtelematics.cmtreferenceapp.common.service.DispatcherProvider
import com.cmtelematics.cmtreferenceapp.manager.Android7AlertManager
import com.cmtelematics.cmtreferenceapp.manager.Android8AboveAlertManager
import com.cmtelematics.cmtreferenceapp.manager.DriverManagerImpl
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.audio.AudioAttributes
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object ManagerModule {

    @Singleton
    @Provides
    fun provideDriverManager(impl: DriverManagerImpl): DriverManager = impl

    @Singleton
    @Provides
    fun provideAlertManager(
        audioManager: AudioManager,
        dispatcherProvider: DispatcherProvider,
        audioAttributes: AudioAttributes,
        exoPlayer: ExoPlayer,
        @SessionDataStore
        dataStore: DataStore<Preferences>
    ): AlertManager = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        Android8AboveAlertManager(
            audioManager = audioManager,
            dispatcherProvider = dispatcherProvider,
            audioAttributes = audioAttributes,
            exoPlayer = exoPlayer,
            dataStore = dataStore
        )
    } else {
        Android7AlertManager(
            audioManager = audioManager,
            dispatcherProvider = dispatcherProvider,
            exoPlayer = exoPlayer,
            dataStore = dataStore
        )
    }
}
