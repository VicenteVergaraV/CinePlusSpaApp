package com.cineplusapp.cineplusspaapp.data.mappers

import com.cineplusapp.cineplusspaapp.data.remote.dto.PeliculaDto
import com.cineplusapp.cineplusspaapp.domain.model.MovieUi
import javax.inject.Inject

class MovieMapper @Inject constructor() {

    fun mapFromRemoteList(list: List<PeliculaDto>): List<MovieUi> =
        list.map { mapFromRemote(it) }

    fun mapFromRemote(remote: PeliculaDto): MovieUi {
        val id = remote._id ?: ""
        return MovieUi(
            id = id,
            numericId = id.hashCode(),
            titulo = remote.titulo,
            director = remote.director,
            genero = remote.genero,
            duracion = remote.duracion,
            sinopsis = remote.sinopsis,
            posterUrl = remote.poster ?: remote.imagen,
            thumbnailUrl = remote.imagenThumbnail,
            trailerUrl = remote.trailer
        )
    }
}