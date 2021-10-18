package kz.aspan.rickandmorty.data.remote

import kz.aspan.rickandmorty.domain.model.character.Character
import kz.aspan.rickandmorty.domain.model.character.Characters
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RickAndMortyApi {

    @GET("character")
    suspend fun getCharacters(): Characters

    @GET("character/{id}")
    suspend fun getCharacterById(@Path("id") characterId: Int): Character
}