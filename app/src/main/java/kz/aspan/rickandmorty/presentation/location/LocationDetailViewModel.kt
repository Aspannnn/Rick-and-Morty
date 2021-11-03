package kz.aspan.rickandmorty.presentation.location

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kz.aspan.rickandmorty.domain.repository.RickAndMortyRepository
import javax.inject.Inject

@HiltViewModel
class LocationDetailViewModel @Inject constructor(
    private val repository: RickAndMortyRepository
) : ViewModel() {
}