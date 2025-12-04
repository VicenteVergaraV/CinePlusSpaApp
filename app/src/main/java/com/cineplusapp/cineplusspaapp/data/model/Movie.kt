package com.cineplusapp.cineplusspaapp.data.model

import com.google.gson.annotations.SerializedName

data class Movie(
    // 1. Mapear "_id" de MongoDB a "id" en Kotlin
    @SerializedName("_id")
    val id: Id,
    val plot: String,
    val genres: List<String>,
    val runtime: Int,
    val cast: List<String>,
    val poster: String?,
    val title: String,
    val fullplot: String,
    val languages: List<String>,
    val released: Released,
    val directors: List<String>,
    val rated: String,
    val awards: Awards,
    val lastupdated: String,
    val year: Int,
    val imdb: Imdb,
    val countries: List<String>,
    val type: String,
    val tomatoes: Tomatoes?,
    // 2. Mapear "num_mflix_comments" de MongoDB a "numMflixComments"
    @SerializedName("num_mflix_comments")
    val numMflixComments: Int
)

// Las clases anidadas también necesitan ajustes:

// --- CLASES ANIDADAS ---

data class Id(
    // 3. Mapear "$oid"
    @SerializedName("\$oid")
    val oid: String
)

data class Released(
    // 4. Mapear "$date"
    @SerializedName("\$date")
    val date: DateInfo
)

data class DateInfo(
    // 5. Mapear "$numberLong"
    @SerializedName("\$numberLong")
    val numberLong: String
)

data class Awards(
    val wins: Int,
    val nominations: Int,
    val text: String
)

data class Imdb(
    val rating: Double,
    val votes: Int,
    val id: Int
)

data class Tomatoes(
    val viewer: Viewer,
    val fresh: Int,
    val critic: Critic,
    val rotten: Int,
    val lastUpdated: LastUpdated
)

data class Viewer(
    val rating: Double,
    val numReviews: Int,
    val meter: Int
)

data class Critic(
    val rating: Double,
    val numReviews: Int,
    val meter: Int
)

data class LastUpdated(
    // 6. Mapear "$date" (También aplica aquí)
    @SerializedName("\$date")
    val date: String
)