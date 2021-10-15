package kz.aspan.rickandmorty.domain.model.location


data class LocationInfo(
    val count: Int,
    val next: String,
    val pages: Int,
    val prev: Any
)