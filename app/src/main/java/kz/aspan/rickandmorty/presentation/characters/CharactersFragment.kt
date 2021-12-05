package kz.aspan.rickandmorty.presentation.characters

import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kz.aspan.rickandmorty.R
import kz.aspan.rickandmorty.adapters.CharacterListAdapter
import kz.aspan.rickandmorty.common.navigateSafely
import kz.aspan.rickandmorty.databinding.FragmentCharactersBinding
import kz.aspan.rickandmorty.common.PaginationScrollListener
import kz.aspan.rickandmorty.common.Response
import kz.aspan.rickandmorty.common.snackbar
import javax.inject.Inject

@AndroidEntryPoint
class CharactersFragment : Fragment(R.layout.fragment_characters) {
    private var _binding: FragmentCharactersBinding? = null
    private val binding: FragmentCharactersBinding
        get() = _binding!!

    private val viewModel: CharactersViewModel by viewModels()

    @Inject
    lateinit var characterAdapter: CharacterListAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCharactersBinding.bind(view)
        setupRecyclerView()
        subscribeToObservers()

        characterAdapter.setOnCharacterClickListener { character ->
            findNavController().navigateSafely(
                R.id.action_charactersFragment_to_characterDetailFragment,
                Bundle().apply {
                    putSerializable("character", character)
                }
            )
        }

        binding.searchEt.addTextChangedListener {
//            viewLifecycleOwner.lifecycleScope.launch {
//            }
        }

    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun subscribeToObservers() {
        viewModel.characterMutableLiveData.observe(viewLifecycleOwner, { response ->
            when (response) {
                is Response.Loading -> {}
                is Response.Success -> {
                    response.data?.let { characters ->
                        characterAdapter.submitList(characters.toMutableList())
                    }
                }
                is Response.Error -> {
                    TODO("Agai dan surau kerek")
                }
            }
        })
    }

    private fun setupRecyclerView() {
        var isLoading = false
        val linearLayoutManager = LinearLayoutManager(requireContext())
        binding.charactersRv.apply {
            adapter = characterAdapter
            layoutManager = linearLayoutManager
            addOnScrollListener(object : PaginationScrollListener(linearLayoutManager) {
                override fun isLoading(): Boolean {
                    return isLoading
                }

                override fun loadMoreItems() {
                    isLoading = true
                    viewModel.nextPage()
                    isLoading = false
                }
            })
        }
    }
}