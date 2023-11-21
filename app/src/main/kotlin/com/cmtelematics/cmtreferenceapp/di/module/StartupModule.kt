package com.cmtelematics.cmtreferenceapp.di.module

import com.cmtelematics.cmtreferenceapp.common.manager.StartupManager
import com.cmtelematics.cmtreferenceapp.manager.StartupManagerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class StartupModule {

    @Binds
    @Singleton
    abstract fun StartupManagerImpl.bindStartupManager(): StartupManager
}
