// data/cinema/CinemaRepository.kt
package com.cineplusapp.cineplusspaapp.data.cinema

import android.location.Location
import com.cineplusapp.cineplusspaapp.data.location.GeoPoint
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.roundToInt

data class Cinema(
    val id: Int,
    val name: String,
    val address: String,
    val lat: Double,
    val lon: Double
)

@Singleton
class CinemaRepository @Inject constructor() {

    // Lista mock (Chile)
    private val all = listOf(
        Cinema(1, "CinePlus Costanera", "Av. Andrés Bello 2425, Providencia", -33.4179, -70.6066),
        Cinema(2, "CinePlus Plaza Egaña", "Av. Ossa 655, La Reina",        -33.4564, -70.5729),
        Cinema(3, "CinePlus Maipú", "Av. Pajaritos 4517, Maipú",           -33.4938, -70.7555),
        Cinema(4, "CinePlus Viña", "Av. Libertad 1348, Viña del Mar",      -33.0227, -71.5517),
        Cinema(5, "CinePlus Concepción", "O'Higgins 525, Concepción",      -36.8270, -73.0503)
    )

    fun nearby(from: GeoPoint, radiusKm: Double = 25.0): List<Pair<Cinema, Int>> {
        return all.map { c ->
            val dMeters = distanceMeters(from.lat, from.lon, c.lat, c.lon)
            val dKm = (dMeters / 1000.0)
            c to dKm.roundToInt()
        }.filter { (_, dKm) -> dKm <= radiusKm }
            .sortedBy { it.second }
    }

    private fun distanceMeters(
        lat1: Double, lon1: Double, lat2: Double, lon2: Double
    ): Float {
        val res = FloatArray(1)
        Location.distanceBetween(lat1, lon1, lat2, lon2, res)
        return res[0]
    }
}
