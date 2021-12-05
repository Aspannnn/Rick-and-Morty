package kz.aspan.rickandmorty.domain.model.character

import com.google.gson.annotations.SerializedName


data class Characters(
    val info: CharacterInfo,
    @SerializedName("results")
    val listOfCharacter: MutableList<Character>
)