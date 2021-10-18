package kz.aspan.rickandmorty.domain.repository

import kotlinx.coroutines.flow.Flow
import kz.aspan.rickandmorty.common.Resource
import kz.aspan.rickandmorty.domain.model.character.Character
import kz.aspan.rickandmorty.domain.model.character.Characters

interface RickAndMortyRepository {
    fun getAllCharacters(): Flow<Resource<Characters>>
    fun getCharacterById(characterId: Int): Flow<Resource<Character>>
}