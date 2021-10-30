package kz.aspan.rickandmorty.presentation.character_detail

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kz.aspan.rickandmorty.common.Resource
import kz.aspan.rickandmorty.domain.model.episode.Episode
import kz.aspan.rickandmorty.domain.repository.RickAndMortyRepository
import javax.inject.Inject

@HiltViewModel
class CharacterDetailViewModel @Inject constructor(
    private val repository: RickAndMortyRepository
) : ViewModel() {

    sealed class DetailEvent() {
        data class GetEpisodes(val episodes: List<Episode>) : DetailEvent()
        data class GetEpisodesError(val error: String) : DetailEvent()
        object GetEpisodeLoading : DetailEvent()
        object GetEpisodeEmpty : DetailEvent()
    }

    private val _detailEvent = MutableSharedFlow<DetailEvent>()
    val detailEvent: SharedFlow<DetailEvent> = _detailEvent

    private val _episodes = MutableStateFlow<DetailEvent>(DetailEvent.GetEpisodeEmpty)
    val episodes: StateFlow<DetailEvent> = _episodes


    fun getEpisodes(urls: List<String>) {
        _episodes.value = DetailEvent.GetEpisodeLoading
        viewModelScope.launch {
            val ids = getIds(urls)

            val result = if (urls.size > 1) {
                repository.getMultipleEpisodes(ids)
            } else {
                repository.getEpisode(ids)
            }

            if (result is Resource.Success) {
                _episodes.value = DetailEvent.GetEpisodes(result.data ?: return@launch)
            } else {
                _detailEvent.emit(DetailEvent.GetEpisodesError(result.message ?: return@launch))
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