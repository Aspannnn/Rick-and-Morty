package kz.aspan.rickandmorty.presentation.episode

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kz.aspan.rickandmorty.common.Resource
import kz.aspan.rickandmorty.domain.model.character.Character
import kz.aspan.rickandmorty.domain.model.episode.Episode
import kz.aspan.rickandmorty.domain.repository.RickAndMortyRepository
import javax.inject.Inject

@HiltViewModel
class EpisodeViewModel @Inject constructor(
    private val repository: RickAndMortyRepository,
    args: SavedStateHandle
) : ViewModel() {


    sealed class EpisodeEvent() {
        data class GetCharacter(val characters: List<Character>) : EpisodeEvent()
        data class GetCharacterError(val error: String) : EpisodeEvent()
        object Loading : EpisodeEvent()
        object EmptyCharacter : EpisodeEvent()
    }

    private val _character = MutableStateFlow<EpisodeEvent>(EpisodeEvent.EmptyCharacter)
    val character: StateFlow<EpisodeEvent> = _character

    private val _episodeEvent = MutableSharedFlow<EpisodeEvent>()
    val episodeEvent: SharedFlow<EpisodeEvent> = _episodeEvent

    init {
        args.get<Episode>("episode")?.let {
            getCharacters(it.characters)
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
                _character.value = EpisodeEvent.GetCharacter(result.data ?: return@launch)
            } else {
                _episodeEvent.emit(EpisodeEvent.GetCharacterError(result.message ?: return@launch))
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