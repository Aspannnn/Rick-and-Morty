package kz.aspan.rickandmorty.presentation.episode

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kz.aspan.rickandmorty.domain.repository.RickAndMortyRepository
import javax.inject.Inject

@HiltViewModel
class EpisodeViewModel @Inject constructor(
    private val repository: RickAndMortyRepository
) : ViewModel() {

}