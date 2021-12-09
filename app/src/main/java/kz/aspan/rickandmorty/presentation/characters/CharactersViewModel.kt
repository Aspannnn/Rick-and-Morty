package kz.aspan.rickandmorty.presentation.characters

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kz.aspan.rickandmorty.common.Constants.CHARACTERS
import kz.aspan.rickandmorty.common.Constants.FILTER_CHARACTER
import kz.aspan.rickandmorty.common.Response
import kz.aspan.rickandmorty.domain.model.character.Character
import kz.aspan.rickandmorty.domain.repository.RickAndMortyRepository
import javax.inject.Inject
import kotlin.Exception

@HiltViewModel
class CharactersViewModel @Inject constructor(
    private val repository: RickAndMortyRepository,
) : ViewModel() {
    val characterMutableLiveData = MutableLiveData<Response<MutableList<Character>?>?>()

    private var oldCharacterList = mutableListOf<Character>()
    private var curPage = 1
    var isCharacterLastPage = false


    private var oldFilteredCharacterList = mutableListOf<Character>()
    private var oldName: String = ""
    var isFilterLastPage = false
    private var filterCurPage = 1

    val whoMakesTheRequest = MutableLiveData<Int>()

    init {
        getCharacters(curPage)
    }


    fun filterCharacter(page: Int = filterCurPage, name: String = oldName, status: String = "") {
        if (oldName != name) {
            oldFilteredCharacterList.clear()
            isFilterLastPage = false
        } else {
            characterMutableLiveData.postValue(Response.Success(oldFilteredCharacterList))
        }
        if (name.isEmpty()) {
            characterMutableLiveData.postValue(Response.Success(oldCharacterList))
            whoMakesTheRequest.postValue(CHARACTERS)
        } else if ((oldName != name && page == 1) || (oldName == name && page != 1)) {
            whoMakesTheRequest.postValue(FILTER_CHARACTER)
            viewModelScope.launch {
                characterMutableLiveData.postValue(Response.Loading())
                try {
                    val result = repository.getFilterCharacter(page, name, status)

                    oldFilteredCharacterList.addAll(result.listOfCharacter)
                    characterMutableLiveData.postValue(Response.Success(oldFilteredCharacterList))

                    val filterInfo = result.info
                    if (filterInfo.next != null) {
                        val uri = Uri.parse(filterInfo.next)
                        filterCurPage = uri.getQueryParameter("page")!!.toInt()
                    } else {
                        isFilterLastPage = true
                    }

                    oldName = name
                } catch (e: Exception) {
                    characterMutableLiveData.postValue(Response.Error(e))
                    characterMutableLiveData.postValue(null)
                }
            }
        }
    }


    fun getCharacters(page: Int = curPage) {
        whoMakesTheRequest.postValue(CHARACTERS)
        viewModelScope.launch {
            characterMutableLiveData.postValue(Response.Loading())
            try {
                val result = repository.getAllCharacters(page)

                oldCharacterList.addAll(result.listOfCharacter)
                characterMutableLiveData.postValue(Response.Success(oldCharacterList))

                val charactersInfo = result.info
                if (charactersInfo.next != null) {
                    val uri = Uri.parse(charactersInfo.next)
                    curPage = uri.getQueryParameter("page")!!.toInt()
                } else {
                    isCharacterLastPage = true
                }
            } catch (e: Exception) {
                characterMutableLiveData.postValue(Response.Error(e))
                characterMutableLiveData.postValue(null)
            }
        }
    }
}

