package kz.aspan.rickandmorty.domain.model.character


data class Characters(
    val info: CharacterInfo,
    val results: List<Character>
)