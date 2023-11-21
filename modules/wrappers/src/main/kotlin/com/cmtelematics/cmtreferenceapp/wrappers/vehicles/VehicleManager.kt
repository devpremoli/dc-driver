package com.cmtelematics.cmtreferenceapp.wrappers.vehicles

import com.cmtelematics.sdk.types.Vehicle
import com.cmtelematics.sdk.types.VehicleType
import kotlinx.coroutines.flow.Flow

/**
 * Provides services related to Vehicle management.
 *
 * [Docs](https://my-cmt-alpha.cmtelematics.com/sdkdoc/tech_pubs_cmt/nextgen_help_center/en/drivewell-sdk/drivewell-sdk-for-android/managing-users-and-trip-data/managing-vehicle-information--android-.html)
 */
interface VehicleManager {
    /**
     * The currently cached list of vehicles. Call [refreshVehicleList] to update from the server.
     */
    val vehicleList: Flow<List<Vehicle>>

    /**
     * Updates the list of cached vehicles in [vehicleList]. This functions waits for the operation to complete and
     * will return once it is concluded.
     *
     * Any network errors encountered will be rethrown.
     */
    suspend fun refreshVehicleList()

    /**
     * Create a vehicle on the server with the specified parameters. The vehicle will be created as
     * [VehicleType.CAR].
     *
     * @param nickname the nickname to use
     * @param make the make of the vehicle can be optionally specified
     * @param model the model of the vehicle can be optionally specified
     */
    suspend fun addVehicle(nickname: String, make: String? = null, model: String? = null): Vehicle

    /**
     * Gets a cached vehicle from the local database based on the short ID.
     *
     * @param vehicleShortId the vehicle short Id query for
     * @return the corresponding [Vehicle] or null if not found
     */
    suspend fun getVehicleByShortId(vehicleShortId: Long): Vehicle?
}
