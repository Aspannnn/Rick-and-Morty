package kz.aspan.rickandmorty.domain.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kz.aspan.rickandmorty.common.Resource
import kz.aspan.rickandmorty.domain.model.character.Character
import kz.aspan.rickandmorty.domain.model.character.Characters

interface RickAndMortyRepository {
    fun getCharacterById(characterId: Int): Flow<Resource<Character>>
    fun getAllCharacters(): Flow<PagingData<Character>>
}