package com.cineplusapp.cineplusspaapp.data.mappers

import com.cineplusapp.cineplusspaapp.data.model.Movie
import com.cineplusapp.cineplusspaapp.domain.model.MovieUi
import javax.inject.Inject

// Puedes usar una extensión o una clase con funciones de mapeo
class MovieMapper @Inject constructor() {

    // Función que transforma la lista completa recibida de la API
    fun mapFromRemoteList(remoteMovies: List<Movie>): List<MovieUi> {
        return remoteMovies.map { mapFromRemote(it) }
    }

    // Función que transforma un objeto individual de la API a uno de UI
    fun mapFromRemote(remoteMovie: Movie): MovieUi {
        return MovieUi(
            // Accedemos a los campos anidados y obtenemos el valor de MovieUi:
            id = remoteMovie.id.oid,
            title = remoteMovie.title,
            year = remoteMovie.year,
            plot = remoteMovie.plot,
            posterUrl = remoteMovie.poster,
            genres = remoteMovie.genres,

            // Accedemos al valor dentro del campo IMDB anidado:
            rating = remoteMovie.imdb.rating
        )
    }
}