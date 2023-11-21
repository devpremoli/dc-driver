package com.cmtelematics.cmtreferenceapp.trips.repository

import com.cmtelematics.cmtreferenceapp.trips.model.TripDetailState
import com.cmtelematics.sdk.types.ProcessedTripSummary
import kotlinx.coroutines.flow.StateFlow

internal interface TripDetailRepository {
    val detailCache: StateFlow<Map<String, TripDetailState>>

    suspend fun loadTripDetails(trip: ProcessedTripSummary)

    suspend fun evictDetail(driveId: String)

    suspend fun clear()
}
