package com.cmtelematics.cmtreferenceapp.wrappers.di

import com.cmtelematics.cmtreferenceapp.wrappers.manager.WrapperDriverManager
import com.cmtelematics.cmtreferenceapp.wrappers.manager.WrapperDriverManagerImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object WrapperManagerModule {

    @Singleton
    @Provides
    fun provideWrapperDriverManager(impl: WrapperDriverManagerImpl): WrapperDriverManager = impl
}
