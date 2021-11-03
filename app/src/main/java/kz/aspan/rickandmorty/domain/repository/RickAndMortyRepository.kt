package kz.aspan.rickandmorty.domain.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kz.aspan.rickandmorty.common.Resource
import kz.aspan.rickandmorty.domain.model.character.Character
import kz.aspan.rickandmorty.domain.model.episode.Episode

interface RickAndMortyRepository {
    fun getAllCharacters(): Flow<PagingData<Character>>
    fun getCharacterByName(name: String, status: String): Flow<PagingData<Character>>
    suspend fun getMultipleEpisodes(ids: String): Resource<List<Episode>>
    suspend fun getEpisode(id: String): Resource<List<Episode>>
    suspend fun getMultipleCharacters(ids: String): Resource<List<Character>>
    suspend fun getCharacterById(id: String): Resource<List<Character>>
}