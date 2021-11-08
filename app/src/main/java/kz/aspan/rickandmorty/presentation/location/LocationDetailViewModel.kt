package kz.aspan.rickandmorty.presentation.location

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kz.aspan.rickandmorty.common.Resource
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

    sealed class LocationEvent() {
        data class GetLocation(val location: Location) : LocationEvent()
        data class GetLocationError(val error: String) : LocationEvent()

        data class GetCharacter(val characters: List<Character>) : LocationEvent()
        data class GetCharacterError(val error: String) : LocationEvent()

        object Loading : LocationEvent()
        object EmptyObject : LocationEvent()
    }

    private val _location = MutableStateFlow<LocationEvent>(LocationEvent.EmptyObject)
    val location: MutableStateFlow<LocationEvent> = _location

    private val _character = MutableStateFlow<LocationEvent>(LocationEvent.EmptyObject)
    val character: StateFlow<LocationEvent> = _character

    private val _locationEvent = MutableSharedFlow<LocationEvent>()
    val locationEvent: SharedFlow<LocationEvent> = _locationEvent


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
            val result = repository.getLocationById(id)
            if (result is Resource.Success) {
                _location.value = LocationEvent.GetLocation(result.data ?: return@launch)
                getCharacters(result.data.residents)
            } else {
                locationEvent
            }
        }
    }

    private fun getCharacters(urls: List<String>) {
        viewModelScope.launch {
            val ids = getIds(urls)
            val result = if (urls.size > 1) {
                repository.getMultipleCharacters(ids)
            } else {
                repository.getCharacterById(ids)
            }

            if (result is Resource.Success) {
                _character.value = LocationEvent.GetCharacter(result.data ?: return@launch)
            } else {
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