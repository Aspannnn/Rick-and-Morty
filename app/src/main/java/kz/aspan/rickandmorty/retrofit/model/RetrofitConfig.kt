package kz.aspan.rickandmorty.retrofit.model

import kz.aspan.rickandmorty.retrofit.RickAndMortyServices
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitConfig {

    private const val BASE_URL = "https://rickandmortyapi.com/api/"

    private fun retrofit(): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
    }

    val rickAndMortyApi: RickAndMortyServices by lazy {
        retrofit().create(RickAndMortyServices::class.java)
    }
}