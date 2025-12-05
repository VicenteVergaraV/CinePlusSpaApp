package com.cineplusapp.cineplusspaapp.data.mappers

import com.cineplusapp.cineplusspaapp.data.model.Movie
import com.cineplusapp.cineplusspaapp.domain.model.MovieUi
import javax.inject.Inject

class MovieMapper @Inject constructor() {

    fun mapFromRemoteList(remoteMovies: List<Movie>): List<MovieUi> {
        return remoteMovies.map { mapFromRemote(it) }
    }

    fun mapFromRemote(remoteMovie: Movie): MovieUi {
        return MovieUi(
            id = remoteMovie.id.oid,
            title = remoteMovie.title,
            year = remoteMovie.year,
            plot = remoteMovie.plot,
            posterUrl = remoteMovie.poster ?: "",
            genres = remoteMovie.genres,
            rating = remoteMovie.imdb.rating
        )
    }
}