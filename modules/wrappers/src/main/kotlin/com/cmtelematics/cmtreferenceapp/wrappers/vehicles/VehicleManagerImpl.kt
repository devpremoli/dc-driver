package com.cmtelematics.cmtreferenceapp.wrappers.vehicles

import com.cmtelematics.cmtreferenceapp.common.service.DispatcherProvider
import com.cmtelematics.cmtreferenceapp.wrappers.util.sendRequest
import com.cmtelematics.sdk.VehicleDb
import com.cmtelematics.sdk.types.Vehicle
import com.cmtelematics.sdk.types.VehicleType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class VehicleManagerImpl @Inject constructor(
    private val vehicleDb: VehicleDb,
    private val dispatcherProvider: DispatcherProvider
) : VehicleManager {
    override val vehicleList = MutableStateFlow<List<Vehicle>>(emptyList())

    override suspend fun refreshVehicleList() = withContext(dispatcherProvider.default) {
        val vehiclesResponse = sendRequest(dispatcherProvider) { runRequest(vehicleDb.getVehicles(observer)) }

        vehicleList.value = vehiclesResponse.vehicles.orEmpty()
    }

    override suspend fun addVehicle(nickname: String, make: String?, model: String?): Vehicle {
        val vehicle = Vehicle(
            0,
            null,
            null,
            null,
            make,
            model,
            nickname,
            0f,
            VehicleType.CAR,
            false,
            0,
            0,
            null,
            null,
            null,
            null,
            false,
            null,
            null,
            null
        )
        val vehicles = sendRequest(dispatcherProvider) { runRequest(vehicleDb.putVehicle(vehicle, observer)) }
        return vehicles.vehicles?.last() ?: error("vehicles list must be not null")
    }

    override suspend fun getVehicleByShortId(vehicleShortId: Long): Vehicle? = withContext(dispatcherProvider.io) {
        vehicleDb.getVehicle(vehicleShortId)
    }
}
