package com.cmtelematics.cmtreferenceapp.di.module

import com.cmtelematics.cmtreferenceapp.common.service.AppVersionProvider
import com.cmtelematics.cmtreferenceapp.common.service.DispatcherProvider
import com.cmtelematics.cmtreferenceapp.common.service.ErrorService
import com.cmtelematics.cmtreferenceapp.service.AppVersionProviderImpl
import com.cmtelematics.cmtreferenceapp.service.DispatcherProviderImpl
import com.cmtelematics.cmtreferenceapp.service.ErrorServiceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ServiceModule {

    @Singleton
    @Binds
    abstract fun ErrorServiceImpl.bindErrorService(): ErrorService

    @Binds
    abstract fun AppVersionProviderImpl.bindAppVersionProvider(): AppVersionProvider

    @Singleton
    @Binds
    abstract fun DispatcherProviderImpl.bindDispatcherProvider(): DispatcherProvider
}
