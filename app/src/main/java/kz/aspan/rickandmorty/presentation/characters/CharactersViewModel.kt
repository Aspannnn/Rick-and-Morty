package kz.aspan.rickandmorty.presentation.characters

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kz.aspan.rickandmorty.common.Resource
import kz.aspan.rickandmorty.domain.model.character.Character
import kz.aspan.rickandmorty.domain.repository.RickAndMortyRepository
import javax.inject.Inject

@HiltViewModel
class CharactersViewModel @Inject constructor(
    private val repository: RickAndMortyRepository
) : ViewModel() {

    sealed class CharactersEvent {
        data class GetAllCharactersEvent(val characters: List<Character>) : CharactersEvent()
        data class GetAllCharactersErrorEvent(val error: String) : CharactersEvent()
        object GetAllCharactersLoadingEvent : CharactersEvent()
        object GetAllCharactersEmptyEvent : CharactersEvent()
    }

    private val _characters =
        MutableStateFlow<CharactersEvent>(CharactersEvent.GetAllCharactersEmptyEvent)

    val characters: StateFlow<CharactersEvent> = _characters

    private val _charactersEvent = MutableSharedFlow<CharactersEvent>()
    val charactersEvent: SharedFlow<CharactersEvent> = _charactersEvent

//    init {
//        getCharacters()
//    }

    fun getCharacters() {
        repository.getAllCharacters().onEach { result ->
            when (result) {
                is Resource.Loading -> {
                    _characters.value = CharactersEvent.GetAllCharactersLoadingEvent
                }
                is Resource.Success -> {
                    _characters.value =
                        CharactersEvent.GetAllCharactersEvent(
                            result.data?.listOfCharacter ?: return@onEach
                        )
                }
                is Resource.Error -> {
                    _charactersEvent.emit(
                        CharactersEvent.GetAllCharactersErrorEvent(result.message ?: return@onEach)
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

}