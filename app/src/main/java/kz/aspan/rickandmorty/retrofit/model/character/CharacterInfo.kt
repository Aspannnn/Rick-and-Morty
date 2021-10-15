package kz.aspan.rickandmorty.retrofit.model.character


data class CharacterInfo(
    val count: Int,
    val next: String,
    val pages: Int,
    val prev: Any
)