package com.cmtelematics.cmtreferenceapp.crash.di.module

import com.cmtelematics.cmtreferenceapp.crash.factory.CrashDetectionRouteFactoryImpl
import com.cmtelematics.cmtreferenceapp.crash.factory.CrashRouteFactory
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class CrashRouteFactoryModule {
    @Binds
    abstract fun CrashDetectionRouteFactoryImpl.bindCrashRouteFactory(): CrashRouteFactory
}
