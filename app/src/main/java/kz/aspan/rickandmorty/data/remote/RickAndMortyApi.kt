package kz.aspan.rickandmorty.data.remote

import kz.aspan.rickandmorty.domain.model.character.Characters
import kz.aspan.rickandmorty.domain.model.episode.Episode
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RickAndMortyApi {

    @GET("character/")
    suspend fun getAllCharacters(@Query("page") page: Int): Response<Characters>

    @GET("episode/{id}")
    suspend fun getMultipleEpisodes(@Path("id") ids: String): Response<List<Episode>>

    @GET("episode/{id}")
    suspend fun getEpisode(@Path("id") id: String): Response<Episode>
}