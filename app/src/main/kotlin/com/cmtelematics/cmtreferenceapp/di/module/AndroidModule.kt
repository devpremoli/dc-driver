@file:Suppress("Deprecation")

package com.cmtelematics.cmtreferenceapp.di.module

import android.app.AlarmManager
import android.app.NotificationManager
import android.bluetooth.BluetoothManager
import android.content.ClipboardManager
import android.content.Context
import android.location.LocationManager
import android.net.ConnectivityManager
import android.os.PowerManager
import androidx.core.content.getSystemService
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.work.WorkManager
import com.cmtelematics.cmtreferenceapp.BuildConfig
import com.cmtelematics.cmtreferenceapp.common.di.qualifier.GoogleMapsApiKey
import com.cmtelematics.cmtreferenceapp.common.di.qualifier.PermanentDataStore
import com.cmtelematics.cmtreferenceapp.common.di.qualifier.SdkVersion
import com.cmtelematics.cmtreferenceapp.common.di.qualifier.ServerUrl
import com.cmtelematics.cmtreferenceapp.common.di.qualifier.SessionDataStore
import com.cmtelematics.cmtreferenceapp.common.di.qualifier.VersionCode
import com.cmtelematics.cmtreferenceapp.common.di.qualifier.VersionName
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AndroidModule {

    @Provides
    fun provideContext(@ApplicationContext context: Context): Context = context

    @Provides
    fun provideConnectivityManager(@ApplicationContext context: Context): ConnectivityManager =
        requireNotNull(context.getSystemService())

    @Provides
    fun provideLocationManager(@ApplicationContext context: Context): LocationManager =
        requireNotNull(context.getSystemService())

    @Provides
    fun providePowerManager(@ApplicationContext context: Context): PowerManager =
        requireNotNull(context.getSystemService())

    @Provides
    @VersionName
    fun provideVersionName(): String = BuildConfig.VERSION_NAME

    @Provides
    @VersionCode
    fun provideVersionCode(): Int = BuildConfig.VERSION_CODE

    @Provides
    @ServerUrl
    fun provideServerUrl(): String = BuildConfig.API_ENDPOINT

    @Provides
    @SdkVersion
    fun provideSdkVersion(): String = BuildConfig.SDK_VERSION

    @Provides
    @Singleton
    @SessionDataStore
    fun provideSessionDataStore(@ApplicationContext context: Context): DataStore<Preferences> =
        PreferenceDataStoreFactory.create { context.preferencesDataStoreFile("reference_app_session_preferences") }

    @Provides
    @Singleton
    @PermanentDataStore
    fun providePermanentDataStore(@ApplicationContext context: Context): DataStore<Preferences> =
        PreferenceDataStoreFactory.create { context.preferencesDataStoreFile("reference_app_permanent_preferences") }

    @Provides
    fun provideClipboardManager(@ApplicationContext context: Context): ClipboardManager =
        context.getSystemService() ?: error("Could not get ClipboardManager")

    @Provides
    @GoogleMapsApiKey
    fun provideGoogleMapsApiKey(): String = BuildConfig.GOOGLE_MAPS_API_KEY

    @Provides
    fun providesNotificationManager(@ApplicationContext context: Context): NotificationManager =
        context.getSystemService() ?: error("Could not get NotificationManager")

    @Provides
    fun provideWorkManager(@ApplicationContext context: Context): WorkManager =
        WorkManager.getInstance(context)

    @Provides
    fun provideAlarmManager(@ApplicationContext context: Context): AlarmManager =
        requireNotNull(context.getSystemService())

    @Provides
    fun provideLocalBroadcastManager(@ApplicationContext context: Context): LocalBroadcastManager =
        LocalBroadcastManager.getInstance(context)

    @Provides
    fun provideBluetoothManager(@ApplicationContext context: Context): BluetoothManager =
        context.getSystemService() ?: error("Could not get BluetoothManager")
}
