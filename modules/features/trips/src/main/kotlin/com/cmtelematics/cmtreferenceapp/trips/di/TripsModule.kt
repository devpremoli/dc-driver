package com.cmtelematics.cmtreferenceapp.trips.di

import com.cmtelematics.cmtreferenceapp.trips.repository.TripDetailRepository
import com.cmtelematics.cmtreferenceapp.trips.repository.TripDetailRepositoryImpl
import com.cmtelematics.cmtreferenceapp.wrappers.trip.TripManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object TripsModule {

    @Provides
    @Singleton
    fun provideTripDetailRepository(tripManager: TripManager): TripDetailRepository =
        TripDetailRepositoryImpl(tripManager)
}
