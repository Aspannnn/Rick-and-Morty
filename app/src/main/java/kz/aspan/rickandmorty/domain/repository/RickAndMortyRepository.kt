package kz.aspan.rickandmorty.domain.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kz.aspan.rickandmorty.common.Resource
import kz.aspan.rickandmorty.domain.model.character.Character
import kz.aspan.rickandmorty.domain.model.character.Characters
import kz.aspan.rickandmorty.domain.model.episode.Episode
import retrofit2.Response

interface RickAndMortyRepository {
    fun getAllCharacters(): Flow<PagingData<Character>>
    suspend fun getMultipleEpisodes(ids: String): Resource<List<Episode>>
    suspend fun getEpisode(id: String): Resource<List<Episode>>
}