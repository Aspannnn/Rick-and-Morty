package kz.aspan.rickandmorty.data.repository

import kz.aspan.rickandmorty.data.remote.RickAndMortyApi
import kz.aspan.rickandmorty.domain.model.character.Character
import kz.aspan.rickandmorty.domain.model.character.Characters
import kz.aspan.rickandmorty.domain.model.episode.Episode
import kz.aspan.rickandmorty.domain.model.location.Location
import kz.aspan.rickandmorty.domain.repository.RickAndMortyRepository
import javax.inject.Inject

class RickAndMortyRepositoryImpl @Inject constructor(
    private val api: RickAndMortyApi
) : RickAndMortyRepository {

    override suspend fun getAllCharacters(page: Int): Characters {
        return api.getAllCharacters(page)
    }

    override suspend fun getMultipleEpisodes(ids: String): List<Episode> {
        return api.getMultipleEpisodes(ids)
    }

    override suspend fun getEpisode(id: String): Episode {
        return api.getEpisode(id)
    }

    override suspend fun getMultipleCharacters(ids: String): List<Character> {
        return api.getMultipleCharacters(ids)
    }

    override suspend fun getCharacterById(id: String): Character {
        return api.getCharacterById(id)
    }

    override suspend fun getLocationById(id: String): Location {
        return api.getLocationById(id)
    }


}