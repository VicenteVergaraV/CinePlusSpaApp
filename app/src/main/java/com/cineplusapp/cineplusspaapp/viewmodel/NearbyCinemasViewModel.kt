// viewmodel/NearbyCinemasViewModel.kt
package com.cineplusapp.cineplusspaapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cineplusapp.cineplusspaapp.data.cinema.Cinema
import com.cineplusapp.cineplusspaapp.data.cinema.CinemaRepository
import com.cineplusapp.cineplusspaapp.data.location.LocationProvider
import com.cineplusapp.cineplusspaapp.data.location.GeoPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

data class NearbyUi(
    val myLocation: GeoPoint? = null,
    val items: List<Pair<Cinema, Int>> = emptyList(), // (cine, distanciaKm)
    val error: String? = null
)

@HiltViewModel
class NearbyCinemasViewModel @Inject constructor(
    private val loc: LocationProvider,
    private val repo: CinemaRepository
) : ViewModel() {

    val ui: StateFlow<NearbyUi> =
        loc.locationFlow()
            .map { gp ->
                if (gp == null) NearbyUi(error = "Sin ubicación")
                else NearbyUi(
                    myLocation = gp,
                    items = repo.nearby(gp, radiusKm = 30.0)
                )
            }
            .catch { emit(NearbyUi(error = it.message ?: "Error")) }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), NearbyUi())
}
