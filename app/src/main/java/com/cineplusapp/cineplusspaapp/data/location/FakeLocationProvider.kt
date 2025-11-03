// data/location/FakeLocationProvider.kt
package com.cineplusapp.cineplusspaapp.data.location

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject
import javax.inject.Singleton

/** Ubicación fija: Plaza Baquedano (Santiago) */
@Singleton
class FakeLocationProvider @Inject constructor() : LocationProvider {
    override fun locationFlow(): Flow<GeoPoint?> =
        flowOf(GeoPoint(-33.4372, -70.6506))
}