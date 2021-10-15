package kz.aspan.rickandmorty.domain.model.episode


data class EpisodeInfo(
    val count: Int,
    val next: String,
    val pages: Int,
    val prev: Any
)