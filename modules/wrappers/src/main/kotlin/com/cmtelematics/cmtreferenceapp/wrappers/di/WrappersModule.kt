package com.cmtelematics.cmtreferenceapp.wrappers.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import com.cmtelematics.cmtreferenceapp.common.service.DispatcherProvider
import com.cmtelematics.cmtreferenceapp.common.util.toNullableSerializer
import com.cmtelematics.cmtreferenceapp.wrappers.analytics.AnalyticsManager
import com.cmtelematics.cmtreferenceapp.wrappers.analytics.AnalyticsManagerImpl
import com.cmtelematics.cmtreferenceapp.wrappers.authentication.AuthenticationManager
import com.cmtelematics.cmtreferenceapp.wrappers.authentication.AuthenticationManagerImpl
import com.cmtelematics.cmtreferenceapp.wrappers.crash.CrashBroadcastManager
import com.cmtelematics.cmtreferenceapp.wrappers.crash.CrashManager
import com.cmtelematics.cmtreferenceapp.wrappers.crash.CrashManagerImpl
import com.cmtelematics.cmtreferenceapp.wrappers.crash.CrashNotificationManager
import com.cmtelematics.cmtreferenceapp.wrappers.crash.CrashNotificationManagerImpl
import com.cmtelematics.cmtreferenceapp.wrappers.crash.CrashService
import com.cmtelematics.cmtreferenceapp.wrappers.crash.CrashServiceImpl
import com.cmtelematics.cmtreferenceapp.wrappers.crash.CrashUpdateScheduler
import com.cmtelematics.cmtreferenceapp.wrappers.crash.CrashUpdateSchedulerImpl
import com.cmtelematics.cmtreferenceapp.wrappers.crash.model.CrashWithMetadata
import com.cmtelematics.cmtreferenceapp.wrappers.device.SettingsManager
import com.cmtelematics.cmtreferenceapp.wrappers.device.SettingsManagerImpl
import com.cmtelematics.cmtreferenceapp.wrappers.di.qualifier.SharingScope
import com.cmtelematics.cmtreferenceapp.wrappers.driver.BatteryIsLowForTripRecordingDriver
import com.cmtelematics.cmtreferenceapp.wrappers.driver.BatteryIsLowForTripRecordingDriverImpl
import com.cmtelematics.cmtreferenceapp.wrappers.driver.FirebaseIdDriver
import com.cmtelematics.cmtreferenceapp.wrappers.driver.FirebaseIdDriverImpl
import com.cmtelematics.cmtreferenceapp.wrappers.driver.HardBrakeDetectorDriver
import com.cmtelematics.cmtreferenceapp.wrappers.driver.HardBrakeDetectorDriverImpl
import com.cmtelematics.cmtreferenceapp.wrappers.driver.LogoutDataStoreClearDriver
import com.cmtelematics.cmtreferenceapp.wrappers.driver.LogoutDataStoreClearDriverImpl
import com.cmtelematics.cmtreferenceapp.wrappers.holder.WrapperBatteryIsLowForTripRecordingHolder
import com.cmtelematics.cmtreferenceapp.wrappers.holder.WrapperBatteryIsLowForTripRecordingHolderImpl
import com.cmtelematics.cmtreferenceapp.wrappers.holder.WrapperNotificationParamsHolder
import com.cmtelematics.cmtreferenceapp.wrappers.holder.WrapperNotificationParamsHolderImpl
import com.cmtelematics.cmtreferenceapp.wrappers.sdk.AppModelProvider
import com.cmtelematics.cmtreferenceapp.wrappers.sdk.SdkManager
import com.cmtelematics.cmtreferenceapp.wrappers.sdk.SdkManagerImpl
import com.cmtelematics.cmtreferenceapp.wrappers.tags.TagManager
import com.cmtelematics.cmtreferenceapp.wrappers.tags.TagManagerImpl
import com.cmtelematics.cmtreferenceapp.wrappers.trip.RecordingManager
import com.cmtelematics.cmtreferenceapp.wrappers.trip.RecordingManagerImpl
import com.cmtelematics.cmtreferenceapp.wrappers.trip.TripManager
import com.cmtelematics.cmtreferenceapp.wrappers.trip.TripManagerImpl
import com.cmtelematics.cmtreferenceapp.wrappers.vehicles.VehicleManager
import com.cmtelematics.cmtreferenceapp.wrappers.vehicles.VehicleManagerImpl
import com.cmtelematics.sdk.DefaultCoreEnv
import com.cmtelematics.sdk.PassThruRequester
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
internal object WrappersModule {

    @Provides
    fun provideSdkManager(impl: SdkManagerImpl): SdkManager = impl

    @Provides
    fun provideAppModelProvider(impl: SdkManagerImpl): AppModelProvider = impl

    @Singleton
    @Provides
    fun provideWrapperNotificationParamsHolder(
        impl: WrapperNotificationParamsHolderImpl
    ): WrapperNotificationParamsHolder = impl

    @Singleton
    @Provides
    fun provideWrapperBatteryIsLowForTripRecordingHolder(
        impl: WrapperBatteryIsLowForTripRecordingHolderImpl
    ): WrapperBatteryIsLowForTripRecordingHolder = impl

    @Singleton
    @Provides
    @SharingScope
    fun provideSharingScope(dispatcherProvider: DispatcherProvider) = CoroutineScope(dispatcherProvider.default)

    @Singleton
    @Provides
    fun provideAuthenticationManager(impl: AuthenticationManagerImpl): AuthenticationManager = impl

    @Provides
    fun provideVehicleManager(impl: VehicleManagerImpl): VehicleManager = impl

    @Singleton
    @Provides
    fun provideTagManager(impl: TagManagerImpl): TagManager = impl

    @Singleton
    @Provides
    fun provideSettingsManager(impl: SettingsManagerImpl): SettingsManager = impl

    @Singleton
    @Provides
    fun provideTripManager(impl: TripManagerImpl): TripManager = impl

    @Singleton
    @Provides
    fun provideRecordingManager(impl: RecordingManagerImpl): RecordingManager = impl

    @Singleton
    @Provides
    fun provideFirebaseIdDriver(impl: FirebaseIdDriverImpl): FirebaseIdDriver = impl

    @Singleton
    @Provides
    fun provideBatteryIsLowForTripRecordingDriver(
        impl: BatteryIsLowForTripRecordingDriverImpl
    ): BatteryIsLowForTripRecordingDriver = impl

    @Singleton
    @Provides
    fun provideLogoutDataStoreClearDriver(impl: LogoutDataStoreClearDriverImpl): LogoutDataStoreClearDriver = impl

    @Singleton
    @Provides
    fun provideHardBrakeDetectorDriver(impl: HardBrakeDetectorDriverImpl): HardBrakeDetectorDriver = impl

    @Singleton
    @Provides
    fun provideAnalyticsManager(impl: AnalyticsManagerImpl): AnalyticsManager = impl

    @Singleton
    @Provides
    fun provideCrashManager(impl: CrashManagerImpl): CrashManager = impl

    @Singleton
    @Provides
    fun provideCrashBroadcastManager(impl: CrashManagerImpl): CrashBroadcastManager = impl

    @Singleton
    @Provides
    fun provideCrashUpdateScheduler(impl: CrashUpdateSchedulerImpl): CrashUpdateScheduler = impl

    @Singleton
    @Provides
    fun provideCrashNotificationManager(impl: CrashNotificationManagerImpl): CrashNotificationManager = impl

    @Singleton
    @Provides
    fun provideCrashDataStore(@ApplicationContext context: Context): DataStore<CrashWithMetadata?> =
        DataStoreFactory.create(
            serializer = CrashWithMetadata.serializer().toNullableSerializer(),
            produceFile = { context.dataStoreFile("crashData.json") }
        )

    @Provides
    fun provideCrashService(
        dispatcherProvider: DispatcherProvider,
        passThruRequester: PassThruRequester
    ): CrashService = CrashServiceImpl(dispatcherProvider, passThruRequester)

    @Provides
    fun provideDefaultCoreEnv(@ApplicationContext context: Context): DefaultCoreEnv = DefaultCoreEnv(context)
}
