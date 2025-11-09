// data/location/LocationProvider.kt
package com.cineplusapp.cineplusspaapp.data.location

import kotlinx.coroutines.flow.Flow

data class GeoPoint(val lat: Double, val lon: Double)

interface LocationProvider {
    /** Se emite la última ubicación conocida y posteriores cambios */
    fun locationFlow(): Flow<GeoPoint?>
}