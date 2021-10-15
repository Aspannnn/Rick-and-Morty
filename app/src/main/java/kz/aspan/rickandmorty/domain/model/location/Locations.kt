package kz.aspan.rickandmorty.domain.model.location


data class Locations(
    val info: LocationInfo,
    val results: List<Location>
)