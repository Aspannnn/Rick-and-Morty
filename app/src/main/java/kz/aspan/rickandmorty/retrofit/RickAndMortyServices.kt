package kz.aspan.rickandmorty.retrofit

import kz.aspan.rickandmorty.retrofit.model.character.Character
import kz.aspan.rickandmorty.retrofit.model.character.Characters
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RickAndMortyServices {

    @GET("character")
    fun getCharacters(): Call<Characters>

    @GET("character/")
    fun getCharactersPage(@Query("page") page: Int): Call<Characters>

    @GET("character/{id}")
    fun getCharacterById(@Path("id") characterId: Int): Call<Character>
}