package com.cmtelematics.cmtreferenceapp.di.module

import com.cmtelematics.cmtreferenceapp.navigation.Navigator
import com.cmtelematics.cmtreferenceapp.navigation.NavigatorImpl
import com.cmtelematics.cmtreferenceapp.navigation.RouteManager
import com.cmtelematics.cmtreferenceapp.navigation.RouteManagerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class NavigationModule {

    @Binds
    abstract fun NavigatorImpl.bindNavigator(): Navigator

    @Binds
    abstract fun RouteManagerImpl.bindRouteManager(): RouteManager
}
