package kz.aspan.rickandmorty.domain.model.location

import com.google.gson.annotations.SerializedName


data class Locations(
    val info: LocationInfo,
    @SerializedName("results")
    val listOfLocation: List<Location>
)