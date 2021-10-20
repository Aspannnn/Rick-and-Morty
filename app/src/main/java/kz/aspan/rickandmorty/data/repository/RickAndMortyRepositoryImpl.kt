package kz.aspan.rickandmorty.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kz.aspan.rickandmorty.common.Resource
import kz.aspan.rickandmorty.data.paging.CharactersPagingDataSource
import kz.aspan.rickandmorty.data.remote.RickAndMortyApi
import kz.aspan.rickandmorty.domain.model.character.Character
import kz.aspan.rickandmorty.domain.repository.RickAndMortyRepository
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class RickAndMortyRepositoryImpl @Inject constructor(
    private val api: RickAndMortyApi
) : RickAndMortyRepository {

    override fun getCharacterById(characterId: Int): Flow<Resource<Character>> = flow {
        try {
            emit(Resource.Loading<Character>())
            val character = api.getCharacterById(characterId)
            emit(Resource.Success<Character>(character))
        } catch (e: HttpException) {
            emit(Resource.Error<Character>(e.localizedMessage ?: "An unexpected error occured"))
        } catch (e: IOException) {
            emit(Resource.Error<Character>("Couldn't reach server. Check your internet connection."))
        }
    }

    override fun getAllCharacters(): Flow<PagingData<Character>> = Pager(
        config = PagingConfig(pageSize = 20, prefetchDistance = 2),
        pagingSourceFactory = { CharactersPagingDataSource(api) }
    ).flow
}