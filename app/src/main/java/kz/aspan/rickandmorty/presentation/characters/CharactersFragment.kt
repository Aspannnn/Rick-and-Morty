package kz.aspan.rickandmorty.presentation.characters

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kz.aspan.rickandmorty.R
import kz.aspan.rickandmorty.presentation.adapters.CharactersAdapter
import kz.aspan.rickandmorty.databinding.FragmentCharactersBinding
import javax.inject.Inject

@AndroidEntryPoint
class CharactersFragment : Fragment(R.layout.fragment_characters) {
    private var _binding: FragmentCharactersBinding? = null
    private val binding: FragmentCharactersBinding
        get() = _binding!!

    private val viewModel: CharactersViewModel by viewModels()

    @Inject
    lateinit var charactersAdapter: CharactersAdapter

    private var updateCharactersJob: Job? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        _binding = FragmentCharactersBinding.bind(view)
        viewModel.getCharacters()
        setupRecyclerView()
        subscribeToObservers()
        charactersAdapter.setOnClickListener {

        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


    private fun subscribeToObservers() = lifecycleScope.launchWhenCreated {
        viewModel.characters.collect { event ->
            when (event) {
                is CharactersViewModel.CharactersEvent.GetAllCharactersLoadingEvent -> {
                }
                is CharactersViewModel.CharactersEvent.GetAllCharactersEvent -> {
                    updateCharactersJob?.cancel()
                    updateCharactersJob = lifecycleScope.launch {
                        charactersAdapter.updateDataset(event.characters)
                    }
                }
                else -> Unit
            }
        }
    }


    private fun setupRecyclerView() {
        binding.charactersRv.apply {
            adapter = charactersAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }
}