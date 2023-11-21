@file:Suppress("Deprecation")

package com.cmtelematics.cmtreferenceapp.wrappers.di

import android.content.Context
import com.cmtelematics.cmtreferenceapp.wrappers.device.DeviceManager
import com.cmtelematics.cmtreferenceapp.wrappers.device.DeviceManagerImpl
import com.cmtelematics.cmtreferenceapp.wrappers.sdk.AppModelProvider
import com.cmtelematics.sdk.AppAnalyticsManager
import com.cmtelematics.sdk.AppConfiguration
import com.cmtelematics.sdk.AppModel
import com.cmtelematics.sdk.DeviceSettingsManager
import com.cmtelematics.sdk.MapDataReader
import com.cmtelematics.sdk.MockTripManager
import com.cmtelematics.sdk.PassThruRequester
import com.cmtelematics.sdk.StandbyModeManager
import com.cmtelematics.sdk.TagConnectionStatusObserver
import com.cmtelematics.sdk.TagStatusManager
import com.cmtelematics.sdk.TelematicsServiceManager
import com.cmtelematics.sdk.TripManager
import com.cmtelematics.sdk.UserManager
import com.cmtelematics.sdk.VehicleDb
import com.cmtelematics.sdk.VehicleTagsManager
import com.cmtelematics.sdk.bus.BusProvider
import com.cmtelematics.sdk.hardbrake.HardBrakeDetector
import com.cmtelematics.sdk.types.Configuration
import com.google.firebase.messaging.FirebaseMessaging
import com.squareup.otto.Bus
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
internal object SdkModule {
    @Singleton
    @Provides
    fun provideAppModel(appModelProvider: AppModelProvider) = appModelProvider.appModel

    @Singleton
    @Provides
    fun provideUserManager(@ApplicationContext context: Context): UserManager = UserManager.get(context)

    @Singleton
    @Provides
    fun provideVehicleDb(@ApplicationContext context: Context): VehicleDb = VehicleDb.get(context)

    @Singleton
    @Provides
    fun provideVehicleTagsManager(appModel: AppModel): VehicleTagsManager = appModel.vehicleTagsManager

    @Singleton
    @Provides
    fun provideDeviceSettingsManager(appModel: AppModel): DeviceSettingsManager = appModel.deviceSettingsManager

    @Singleton
    @Provides
    fun provideTagStatusManager(@ApplicationContext context: Context): TagStatusManager = TagStatusManager.get(context)

    @Singleton
    @Provides
    fun provideTagConnectionStatusObserver(): TagConnectionStatusObserver = TagConnectionStatusObserver.get()

    @Singleton
    @Provides
    fun provideTripManager(appModel: AppModel): TripManager = appModel.tripManager

    @Singleton
    @Provides
    fun provideServiceManager(appModel: AppModel): TelematicsServiceManager = appModel.serviceManager

    @Singleton
    @Provides
    fun provideMockTripManager(@ApplicationContext context: Context): MockTripManager = MockTripManager.get(context)

    @Singleton
    @Provides
    fun provideAppConfiguration(@ApplicationContext context: Context): Configuration =
        AppConfiguration.getConfiguration(context)

    @Singleton
    @Provides
    fun provideFirebaseMessaging() = FirebaseMessaging.getInstance()

    @Singleton
    @Provides
    fun provideDeviceManager(impl: DeviceManagerImpl): DeviceManager = impl

    @Singleton
    @Provides
    fun provideMapDataReader(@ApplicationContext context: Context): MapDataReader = MapDataReader(context)

    @Singleton
    @Provides
    fun provideBus(): Bus = BusProvider.getInstance()

    @Singleton
    @Provides
    fun provideAppAnalyticsManager(@ApplicationContext context: Context): AppAnalyticsManager =
        AppAnalyticsManager(context)

    @Singleton
    @Provides
    fun providePassThruRequester(@ApplicationContext context: Context): PassThruRequester =
        PassThruRequester.get(context)

    @Singleton
    @Provides
    fun provideStandbyModeManager(@ApplicationContext context: Context): StandbyModeManager =
        StandbyModeManager.get(context)

    @Singleton
    @Provides
    fun provideHardBrakeDetector(@ApplicationContext context: Context): HardBrakeDetector =
        HardBrakeDetector.get(context)
}
