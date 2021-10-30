package kz.aspan.rickandmorty.domain.model.episode


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Episode(
    @SerializedName("air_date")
    val airDate: String,
    val characters: List<String>,
    val created: String,
    val episode: String,
    val id: Int,
    val name: String,
    val url: String
):Serializable