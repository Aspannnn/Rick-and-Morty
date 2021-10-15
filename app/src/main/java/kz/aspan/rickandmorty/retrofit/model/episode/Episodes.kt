package kz.aspan.rickandmorty.retrofit.model.episode


data class Episodes(
    val info: EpisodeInfo,
    val results: List<Episode>
)