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
import javax.inject.Inject
import kotlin.Exception

@HiltViewModel
class CharactersViewModel @Inject constructor(
    private val repository: RickAndMortyRepository,
) : ViewModel() {
    val characterMutableLiveData = MutableLiveData<Response<MutableList<Character>?>?>()

    var charactersInfo: CharacterInfo? = null
    private var oldCharacterList = mutableListOf<Character>()

    private var filterCharacterInfo: CharacterInfo? = null
    private var oldFilteredCharacterList = mutableListOf<Character>()
    private var oldName: String = ""

    var page = 1
    init {
        getCharacters()
    }


    fun filterCharacter(page: Int = 1, name: String, status: String = "") {
        if (oldName != name) {
            oldFilteredCharacterList.clear()
        }
        viewModelScope.launch {
            characterMutableLiveData.postValue(Response.Loading())
            try {
                val result = repository.getFilterCharacter(page, name, status)
                oldFilteredCharacterList.addAll(result.listOfCharacter)
                characterMutableLiveData.postValue(Response.Success(oldFilteredCharacterList))
                filterCharacterInfo = result.info
                oldName = name
            } catch (e: Exception) {
                characterMutableLiveData.postValue(Response.Error(e))
                characterMutableLiveData.postValue(null)
            }
        }
    }

    fun filterNextPage() {
        filterCharacterInfo?.let {
            if (it.next != null) {
                val uri = Uri.parse(it.next)
                val nextPageQuery = uri.getQueryParameter("page")!!.toInt()
                filterCharacter(page = nextPageQuery, name = oldName)
            }
        }
    }

    fun returnToCharacter() {
        characterMutableLiveData.postValue(Response.Success(oldCharacterList))
    }

    fun nextPage() {
        charactersInfo?.let {
            if (it.next != null) {
                val uri = Uri.parse(it.next)
                val nextPageQuery = uri.getQueryParameter("page")!!.toInt()
                getCharacters(nextPageQuery)
                page = nextPageQuery
                println("TEST GET $nextPageQuery")
            }
        }
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
}

