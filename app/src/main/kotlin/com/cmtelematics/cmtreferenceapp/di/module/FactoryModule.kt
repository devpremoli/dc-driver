package com.cmtelematics.cmtreferenceapp.di.module

import com.cmtelematics.cmtreferenceapp.common.factory.MainRouteFactory
import com.cmtelematics.cmtreferenceapp.factory.MainRouteFactoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class FactoryModule {

    @Binds
    abstract fun MainRouteFactoryImpl.bindMainRouteFactory(): MainRouteFactory
}
