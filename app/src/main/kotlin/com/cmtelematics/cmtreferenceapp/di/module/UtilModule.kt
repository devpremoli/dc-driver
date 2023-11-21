package com.cmtelematics.cmtreferenceapp.di.module

import android.content.Context
import android.media.AudioManager
import com.cmtelematics.cmtreferenceapp.common.util.ActivityProvider
import com.cmtelematics.cmtreferenceapp.driver.CrashDetectionDriver
import com.cmtelematics.cmtreferenceapp.driver.CrashDetectionDriverImpl
import com.cmtelematics.cmtreferenceapp.driver.HardwareRequirementsDriver
import com.cmtelematics.cmtreferenceapp.driver.HardwareRequirementsDriverImpl
import com.cmtelematics.cmtreferenceapp.driver.LogoutNavigationDriver
import com.cmtelematics.cmtreferenceapp.driver.LogoutNavigationDriverImpl
import com.cmtelematics.cmtreferenceapp.driver.PermissionChangeNotifierDriver
import com.cmtelematics.cmtreferenceapp.driver.PermissionChangeNotifierDriverImpl
import com.cmtelematics.cmtreferenceapp.driver.ScreenResumeTrapdoorsDriver
import com.cmtelematics.cmtreferenceapp.driver.ScreenResumeTrapdoorsDriverImpl
import com.cmtelematics.cmtreferenceapp.util.ActivityProviderImpl
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.audio.AudioAttributes
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.time.Clock
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UtilModule {

    @Provides
    fun provideActivityProvider(impl: ActivityProviderImpl): ActivityProvider = impl

    @Provides
    fun provideClock(): Clock = Clock.systemDefaultZone()

    @Singleton
    @Provides
    fun provideHardwareRequirementsDriver(impl: HardwareRequirementsDriverImpl): HardwareRequirementsDriver = impl

    @Singleton
    @Provides
    fun provideLogoutNavigationDriver(impl: LogoutNavigationDriverImpl): LogoutNavigationDriver = impl

    @Singleton
    @Provides
    fun provideCrashDetectionDriver(impl: CrashDetectionDriverImpl): CrashDetectionDriver = impl

    @Singleton
    @Provides
    fun providePermissionChangeNotifierDriver(
        impl: PermissionChangeNotifierDriverImpl
    ): PermissionChangeNotifierDriver = impl

    @Singleton
    @Provides
    fun provideScreenResumeTrapdoorsDriver(impl: ScreenResumeTrapdoorsDriverImpl): ScreenResumeTrapdoorsDriver = impl

    @Provides
    fun provideAudioManager(@ApplicationContext context: Context): AudioManager =
        context.getSystemService(Context.AUDIO_SERVICE) as AudioManager

    @Provides
    @Singleton
    fun provideAudioAttributes(): AudioAttributes = AudioAttributes.Builder()
        .setContentType(C.AUDIO_CONTENT_TYPE_SPEECH)
        .setUsage(C.USAGE_ASSISTANCE_NAVIGATION_GUIDANCE)
        .build()

    @Provides
    fun provideExoplayer(@ApplicationContext context: Context, audioAttributes: AudioAttributes): ExoPlayer =
        ExoPlayer.Builder(context)
            .setTrackSelector(DefaultTrackSelector(context))
            .setAudioAttributes(audioAttributes, false)
            .build()
}
