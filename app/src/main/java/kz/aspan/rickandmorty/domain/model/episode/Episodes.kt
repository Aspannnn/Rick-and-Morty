package kz.aspan.rickandmorty.domain.model.episode


data class Episodes(
    val info: EpisodeInfo,
    val results: List<Episode>
)