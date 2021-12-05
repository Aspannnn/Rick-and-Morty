package kz.aspan.rickandmorty.presentation.location

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kz.aspan.rickandmorty.common.Resource
import kz.aspan.rickandmorty.common.Response
import kz.aspan.rickandmorty.domain.model.character.Character
import kz.aspan.rickandmorty.domain.model.location.Location
import kz.aspan.rickandmorty.domain.repository.RickAndMortyRepository
import kz.aspan.rickandmorty.presentation.episode.EpisodeViewModel
import javax.inject.Inject

@HiltViewModel
class LocationDetailViewModel @Inject constructor(
    private val repository: RickAndMortyRepository,
    args: SavedStateHandle
) : ViewModel() {

    val characterMutableLiveData = MutableLiveData<Response<List<Character>?>?>()
    val locationMutableLiveData = MutableLiveData<Response<Location>?>()


    init {
        args.get<String>("locationUrl")?.let {
            val uri = Uri.parse(it)
            val id = uri.lastPathSegment
            println(id)
            getLocationById(id.toString())
        }
    }

    private fun getLocationById(id: String) {
        viewModelScope.launch {
            locationMutableLiveData.postValue(Response.Loading())
            try {
                val result = repository.getLocationById(id)
                locationMutableLiveData.postValue(Response.Success(result))
                getCharacters(result.residents)
            } catch (e: Exception) {

            }
        }
    }

    private fun getCharacters(urls: List<String>) {
        viewModelScope.launch {
            characterMutableLiveData.postValue(Response.Loading())
            val ids = getIds(urls)
            try {
                val result = if (urls.size > 1) {
                    repository.getMultipleCharacters(ids)
                } else {
                    listOf(repository.getCharacterById(ids))
                }
                characterMutableLiveData.postValue(Response.Success(result))
            } catch (e: Exception) {

            }
        }
    }

    private fun getIds(urls: List<String>): String {
        return buildString {
            for (i in urls.indices) {
                val uri = Uri.parse(urls[i])
                append(uri.lastPathSegment)
                if (i != urls.size - 1 && urls.size != 1) {
                    append(",")
                }
            }
        }
    }

}