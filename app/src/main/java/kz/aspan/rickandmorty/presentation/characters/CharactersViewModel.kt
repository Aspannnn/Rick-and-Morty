package kz.aspan.rickandmorty.presentation.characters

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kz.aspan.rickandmorty.common.Response
import kz.aspan.rickandmorty.domain.model.character.Character
import kz.aspan.rickandmorty.domain.model.character.CharacterInfo
import kz.aspan.rickandmorty.domain.repository.RickAndMortyRepository
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class CharactersViewModel @Inject constructor(
    private val repository: RickAndMortyRepository,
) : ViewModel() {
    val characterMutableLiveData = MutableLiveData<Response<MutableList<Character>?>?>()
    private var charactersInfo: CharacterInfo? = null
    private var oldCharacterList = mutableListOf<Character>()

    init {
        getCharacters()
    }


    private fun getCharacters(page: Int = 1) {
        viewModelScope.launch {
            characterMutableLiveData.postValue(Response.Loading())
            try {
                val result = repository.getAllCharacters(page)
                oldCharacterList.addAll(result.listOfCharacter)
                characterMutableLiveData.postValue(Response.Success(oldCharacterList))
                charactersInfo = result.info
            } catch (e: Exception) {
                characterMutableLiveData.postValue(Response.Error(e))
                characterMutableLiveData.postValue(null)
            }
        }
    }

    fun nextPage() {
        charactersInfo?.let {
            if (it.next != null) {
                val uri = Uri.parse(it.next)
                println("TEST   ${uri.lastPathSegment}")
                val nextPageQuery = uri.getQueryParameter("page")!!.toInt()
                getCharacters(nextPageQuery)
            }
        }
    }
}

