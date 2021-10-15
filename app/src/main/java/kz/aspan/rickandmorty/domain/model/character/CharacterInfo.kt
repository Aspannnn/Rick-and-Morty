package kz.aspan.rickandmorty.domain.model.character


data class CharacterInfo(
    val count: Int,
    val next: String,
    val pages: Int,
    val prev: Any
)