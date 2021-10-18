package kz.aspan.rickandmorty.domain.model.episode

import com.google.gson.annotations.SerializedName


data class Episodes(
    val info: EpisodeInfo,
    @SerializedName("results")
    val listOfEpisode: List<Episode>
)