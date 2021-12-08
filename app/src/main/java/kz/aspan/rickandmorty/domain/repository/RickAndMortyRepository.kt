package kz.aspan.rickandmorty.domain.repository

import kz.aspan.rickandmorty.domain.model.character.Character
import kz.aspan.rickandmorty.domain.model.character.Characters
import kz.aspan.rickandmorty.domain.model.episode.Episode
import kz.aspan.rickandmorty.domain.model.location.Location

interface RickAndMortyRepository {
    suspend fun getAllCharacters(page: Int): Characters
    suspend fun getMultipleEpisodes(ids: String): List<Episode>
    suspend fun getEpisode(id: String): Episode
    suspend fun getMultipleCharacters(ids: String): List<Character>
    suspend fun getCharacterById(id: String): Character
    suspend fun getLocationById(id: String): Location
    suspend fun getFilterCharacter(page: Int, name: String, status: String): Characters
}