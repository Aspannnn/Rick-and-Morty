package kz.aspan.rickandmorty.retrofit.model.location


data class Locations(
    val info: LocationInfo,
    val results: List<Location>
)