package com.cmtelematics.cmtreferenceapp.common.di

import com.cmtelematics.cmtreferenceapp.common.di.qualifier.DriverScope
import com.cmtelematics.cmtreferenceapp.common.service.DispatcherProvider
import com.cmtelematics.cmtreferenceapp.common.service.ToastService
import com.cmtelematics.cmtreferenceapp.common.service.ToastServiceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object CommonModule {
    @Singleton
    @Provides
    fun provideToastService(impl: ToastServiceImpl): ToastService = impl

    @Singleton
    @Provides
    @DriverScope
    fun provideDriverScope(dispatcherProvider: DispatcherProvider) = CoroutineScope(dispatcherProvider.default)
}
