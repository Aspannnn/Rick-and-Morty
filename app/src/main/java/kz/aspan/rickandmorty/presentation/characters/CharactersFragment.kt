package kz.aspan.rickandmorty.presentation.characters

import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kz.aspan.rickandmorty.R
import kz.aspan.rickandmorty.common.navigateSafely
import kz.aspan.rickandmorty.databinding.FragmentCharactersBinding
import kz.aspan.rickandmorty.adapters.CharacterPagingAdapter
import javax.inject.Inject

@AndroidEntryPoint
class CharactersFragment : Fragment(R.layout.fragment_characters) {
    private var _binding: FragmentCharactersBinding? = null
    private val binding: FragmentCharactersBinding
        get() = _binding!!

    private val viewModel: CharactersViewModel by viewModels()


    @Inject
    lateinit var charactersAdapter: CharacterPagingAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCharactersBinding.bind(view)
        setupRecyclerView()
        subscribeToObservers()
        charactersAdapter.setOnClickListener { character ->
            findNavController().navigateSafely(
                R.id.action_charactersFragment_to_characterDetailFragment,
                Bundle().apply {
                    putSerializable("character", character)
                }
            )
        }

        binding.searchEt.addTextChangedListener {
            viewLifecycleOwner.lifecycleScope.launch {
                delay(300L)
                viewModel.filterCharacterByName(it.toString())
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


    private fun subscribeToObservers() = viewLifecycleOwner.lifecycleScope.launchWhenStarted {
        viewModel.characters.collectLatest { event ->
            when (event) {
                is CharactersViewModel.CharactersEvent.GetAllCharactersLoadingEvent -> {
                }
                is CharactersViewModel.CharactersEvent.GetAllCharactersEvent -> {
                    event.characters.collectLatest {
                        charactersAdapter.submitData(viewLifecycleOwner.lifecycle, it)
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