package com.cmtelematics.cmtreferenceapp.permission.di.module

import com.cmtelematics.cmtreferenceapp.common.factory.PermissionRouteFactory
import com.cmtelematics.cmtreferenceapp.permission.factory.PermissionRouteFactoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class FactoryModule {

    @Binds
    abstract fun PermissionRouteFactoryImpl.bindPermissionRouteFactory(): PermissionRouteFactory
}
