package kz.aspan.rickandmorty.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kz.aspan.rickandmorty.common.Resource
import kz.aspan.rickandmorty.data.paging.CharacterFilterPagingDataSource
import kz.aspan.rickandmorty.data.paging.CharactersPagingDataSource
import kz.aspan.rickandmorty.data.remote.RickAndMortyApi
import kz.aspan.rickandmorty.domain.model.character.Character
import kz.aspan.rickandmorty.domain.model.character.Characters
import kz.aspan.rickandmorty.domain.model.episode.Episode
import kz.aspan.rickandmorty.domain.model.location.Location
import kz.aspan.rickandmorty.domain.repository.RickAndMortyRepository
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

class RickAndMortyRepositoryImpl @Inject constructor(
    private val api: RickAndMortyApi
) : RickAndMortyRepository {

    override fun getAllCharacters(): Flow<PagingData<Character>> = Pager(
        config = PagingConfig(pageSize = 20, prefetchDistance = 2),
        pagingSourceFactory = { CharactersPagingDataSource(api) }
    ).flow


    override fun getCharacterByName(name: String, status: String): Flow<PagingData<Character>> =
        Pager(
            config = PagingConfig(pageSize = 20, prefetchDistance = 2),
            pagingSourceFactory = { CharacterFilterPagingDataSource(api, name, status) }
        ).flow

    override suspend fun getMultipleEpisodes(ids: String): Resource<List<Episode>> {
        val response = try {
            api.getMultipleEpisodes(ids)
        } catch (e: HttpException) {
            return Resource.Error("Something went wrong. Please try again later.")
        } catch (e: IOException) {
            return Resource.Error("Couldn\'t reach server. Check your internet connection")
        }

        return if (response.isSuccessful && response.body() != null) {
            Resource.Success(response.body()!!)
        } else {
            Resource.Error("Unknown Error")
        }
    }

    override suspend fun getEpisode(id: String): Resource<List<Episode>> {
        val response = try {
            api.getEpisode(id)
        } catch (e: HttpException) {
            return Resource.Error("Something went wrong. Please try again later.")
        } catch (e: IOException) {
            return Resource.Error("Couldn\'t reach server. Check your internet connection")
        }

        return if (response.isSuccessful && response.body() != null) {
            Resource.Success(listOf(response.body()!!))
        } else {
            Resource.Error("Unknown Error")
        }
    }

    override suspend fun getMultipleCharacters(ids: String): Resource<List<Character>> {
        val response = try {
            api.getMultipleCharacters(ids)
        } catch (e: HttpException) {
            return Resource.Error("Something went wrong. Please try again later.")
        } catch (e: IOException) {
            return Resource.Error("Couldn\'t reach server. Check your internet connection")
        }

        return if (response.isSuccessful && response.body() != null) {
            Resource.Success(response.body()!!)
        } else {
            Resource.Error("Unknown Error")
        }
    }

    override suspend fun getCharacterById(id: String): Resource<List<Character>> {
        val response = try {
            api.getCharacterById(id)
        } catch (e: HttpException) {
            return Resource.Error("Something went wrong. Please try again later.")
        } catch (e: IOException) {
            return Resource.Error("Couldn\'t reach server. Check your internet connection")
        }

        return if (response.isSuccessful && response.body() != null) {
            Resource.Success(listOf(response.body()!!))
        } else {
            Resource.Error("Unknown Error")
        }
    }

    override suspend fun getLocationById(id: String): Resource<Location> {
        val response = try {
            api.getLocationById(id)
        } catch (e: HttpException) {
            return Resource.Error("Something went wrong. Please try again later.")
        } catch (e: IOException) {
            return Resource.Error("Couldn\'t reach server. Check your internet connection")
        }

        return if (response.isSuccessful && response.body() != null) {
            Resource.Success(response.body()!!)
        } else {
            Resource.Error("Unknown Error")
        }
    }


}