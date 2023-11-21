package com.cmtelematics.cmtreferenceapp.crash.di.module

import com.cmtelematics.cmtreferenceapp.crash.provider.LocationProvider
import com.cmtelematics.cmtreferenceapp.crash.provider.LocationProviderImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@Module
@InstallIn(ActivityRetainedComponent::class)
internal abstract class LocationProviderModule {
    @Binds
    abstract fun LocationProviderImpl.bindLocationProvider(): LocationProvider
}
