package kz.aspan.rickandmorty.data.remote

import kz.aspan.rickandmorty.domain.model.character.Character
import kz.aspan.rickandmorty.domain.model.character.Characters
import kz.aspan.rickandmorty.domain.model.episode.Episode
import kz.aspan.rickandmorty.domain.model.location.Location
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RickAndMortyApi {

    @GET("character/")
    suspend fun getAllCharacters(@Query("page") page: Int): Characters

    @GET("episode/{id}")
    suspend fun getMultipleEpisodes(@Path("id") ids: String): List<Episode>

    @GET("episode/{id}")
    suspend fun getEpisode(@Path("id") id: String): Episode

    @GET("character/{id}")
    suspend fun getMultipleCharacters(@Path("id") ids: String): List<Character>

    @GET("character/{id}")
    suspend fun getCharacterById(@Path("id") ids: String): Character

    @GET("character/")
    suspend fun getFilterCharacter(
        @Query("page") page: Int,
        @Query("name") name: String,
        @Query("status") status: String
    ): Characters


    @GET("location/{id}")
    suspend fun getLocationById(@Path("id") id: String): Location
}