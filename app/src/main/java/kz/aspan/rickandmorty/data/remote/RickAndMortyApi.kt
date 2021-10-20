package kz.aspan.rickandmorty.data.remote

import kz.aspan.rickandmorty.domain.model.character.Character
import kz.aspan.rickandmorty.domain.model.character.Characters
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RickAndMortyApi {
    @GET("character/{id}")
    suspend fun getCharacterById(@Path("id") characterId: Int): Character

    @GET("character/")
    suspend fun getAllCharacters(@Query("page") page: Int): Response<Characters>
}