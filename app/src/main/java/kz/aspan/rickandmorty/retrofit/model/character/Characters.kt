package kz.aspan.rickandmorty.retrofit.model.character


data class Characters(
    val info: CharacterInfo,
    val results: List<Character>
)