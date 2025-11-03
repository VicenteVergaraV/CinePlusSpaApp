// data/location/RealLocationProvider.kt
package com.cineplusapp.cineplusspaapp.data.location

import android.annotation.SuppressLint
import android.content.Context
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RealLocationProvider @Inject constructor(
    @ApplicationContext private val context: Context
) : LocationProvider {

    @SuppressLint("MissingPermission") // lo controlamos en la UI con runtime permissions
    override fun locationFlow() = callbackFlow<GeoPoint?> {
        val client = LocationServices.getFusedLocationProviderClient(context)

        // Emitir lastLocation (si existe)
        client.lastLocation.addOnSuccessListener { loc ->
            if (loc != null) trySend(GeoPoint(loc.latitude, loc.longitude))
        }

        // Suscribir a updates livianos
        val req = LocationRequest.Builder(Priority.PRIORITY_BALANCED_POWER_ACCURACY, 5_000L)
            .setMinUpdateIntervalMillis(2_000L)
            .build()

        val cb = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                val loc = result.lastLocation ?: return
                trySend(GeoPoint(loc.latitude, loc.longitude))
            }
        }

        client.requestLocationUpdates(req, cb, context.mainLooper)
        awaitClose { client.removeLocationUpdates(cb) }
    }
}
