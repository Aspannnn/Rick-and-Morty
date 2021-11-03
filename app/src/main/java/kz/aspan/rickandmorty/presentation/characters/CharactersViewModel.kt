package kz.aspan.rickandmorty.presentation.characters

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kz.aspan.rickandmorty.common.Resource
import kz.aspan.rickandmorty.domain.model.character.Character
import kz.aspan.rickandmorty.domain.repository.RickAndMortyRepository
import timber.log.Timber
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class CharactersViewModel @Inject constructor(
    private val repository: RickAndMortyRepository,
) : ViewModel() {


    sealed class CharactersEvent {
        data class GetAllCharactersEvent(val characters: Flow<PagingData<Character>>) :
            CharactersEvent()

        data class GetAllCharactersErrorEvent(val error: String) : CharactersEvent()
        object GetAllCharactersLoadingEvent : CharactersEvent()
        object GetAllCharactersEmptyEvent : CharactersEvent()
    }

    private val _characters =
        MutableStateFlow<CharactersEvent>(CharactersEvent.GetAllCharactersEmptyEvent)
    val characters: StateFlow<CharactersEvent> = _characters

    private val _charactersEvent = MutableSharedFlow<CharactersEvent>()
    val charactersEvent: SharedFlow<CharactersEvent> = _charactersEvent

    private val _filteredCharacters =
        MutableStateFlow<CharactersEvent>(CharactersEvent.GetAllCharactersEmptyEvent)
    val filteredCharacters: StateFlow<CharactersEvent> = _filteredCharacters

    init {
        getAllCharacters()
    }

    fun filterCharacterByName(name: String, status: String = "") {
        viewModelScope.launch {
            try {
                val result = repository.getCharacterByName(name, status).cachedIn(viewModelScope)
                _filteredCharacters.value = CharactersEvent.GetAllCharactersEvent(result)
            } catch (e: Exception) {
                _charactersEvent.emit(CharactersEvent.GetAllCharactersErrorEvent(e.localizedMessage))
            }
        }
    }

    private fun getAllCharacters() {
        viewModelScope.launch {
            try {
                val result = repository.getAllCharacters().cachedIn(viewModelScope)
                _characters.value = CharactersEvent.GetAllCharactersEvent(result)
            } catch (e: Exception) {
                _charactersEvent.emit(CharactersEvent.GetAllCharactersErrorEvent(e.localizedMessage))
            }
        }
    }


}