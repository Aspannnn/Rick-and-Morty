package kz.aspan.rickandmorty.presentation.character_detail

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kz.aspan.rickandmorty.common.Response
import kz.aspan.rickandmorty.domain.model.character.Character
import kz.aspan.rickandmorty.domain.model.episode.Episode
import kz.aspan.rickandmorty.domain.repository.RickAndMortyRepository
import javax.inject.Inject

@HiltViewModel
class CharacterDetailViewModel @Inject constructor(
    private val repository: RickAndMortyRepository,
    args: SavedStateHandle
) : ViewModel() {

    val episodeMutableLiveData = MutableLiveData<Response<List<Episode>?>?>()

    init {
        args.get<Character>("character")?.let {
            getEpisodes(it.episode)
        }
    }

    private fun getEpisodes(urls: List<String>) {
        viewModelScope.launch {
            episodeMutableLiveData.postValue(Response.Loading())
            val ids = getIds(urls)
            try {
                val result = if (urls.size > 1) {
                    repository.getMultipleEpisodes(ids)
                } else {
                    listOf(repository.getEpisode(ids))
                }
                episodeMutableLiveData.postValue(Response.Success(result))
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