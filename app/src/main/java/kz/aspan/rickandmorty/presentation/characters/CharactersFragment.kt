package kz.aspan.rickandmorty.presentation.characters

import android.os.Bundle
import android.view.View
import android.widget.AbsListView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kz.aspan.rickandmorty.R
import kz.aspan.rickandmorty.adapters.CharacterListAdapter
import kz.aspan.rickandmorty.common.Constants.CHARACTERS
import kz.aspan.rickandmorty.common.Constants.FILTER_CHARACTER
import kz.aspan.rickandmorty.common.navigateSafely
import kz.aspan.rickandmorty.databinding.FragmentCharactersBinding
import kz.aspan.rickandmorty.common.PaginationScrollListener
import kz.aspan.rickandmorty.common.Response
import javax.inject.Inject

@AndroidEntryPoint
class CharactersFragment : Fragment(R.layout.fragment_characters) {


    private var _binding: FragmentCharactersBinding? = null
    private val binding: FragmentCharactersBinding
        get() = _binding!!

    private val viewModel: CharactersViewModel by viewModels()

    @Inject
    lateinit var characterAdapter: CharacterListAdapter

    private var who = CHARACTERS

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

        var searchJob: Job? = null
        binding.searchEt.addTextChangedListener {
            searchJob?.cancel()
            searchJob = lifecycleScope.launch {
                delay(3000)
                viewModel.filterCharacter(page = 1, name = it.toString())
                LinearLayoutManager(requireContext()).scrollToPosition(0)
                isLastPage = viewModel.isFilterLastPage
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun subscribeToObservers() {
        viewModel.characterMutableLiveData.observe(viewLifecycleOwner, { response ->
            when (response) {
                is Response.Loading -> {
                    showProgressBar()
                }
                is Response.Success -> {
                    hideProgressBar()
                    response.data?.let { characters ->
                        characterAdapter.submitList(characters.toList())

                        isLastPage = if (who == FILTER_CHARACTER) {
                            viewModel.isFilterLastPage
                        } else {
                            viewModel.isCharacterLastPage
                        }
                    }
                }
                is Response.Error -> {
                    hideProgressBar()
                    TODO("Agai dan surau kerek")
                }
            }
        })

        viewModel.whoMakesTheRequest.observe(viewLifecycleOwner, {
            who = it
        })
    }

    private fun hideProgressBar() {
        isLoading = false
    }

    private fun showProgressBar() {
        isLoading = true
    }

    var isLoading = false
    var isLastPage = false
    var isScrolling = false
    private fun setupRecyclerView() {
        val gridLayoutManager = GridLayoutManager(requireActivity(), 2)
        binding.charactersRv.apply {
            adapter = characterAdapter
            layoutManager = gridLayoutManager

            addOnScrollListener(object : PaginationScrollListener(gridLayoutManager) {
                override fun isLoading(): Boolean {
                    return isLoading
                }

                override fun isLastPage(): Boolean {
                    return isLastPage
                }

                override fun isScrolling(): Boolean {
                    return isScrolling
                }

                override fun loadMoreItems() {
                    if (who == CHARACTERS) {
                        viewModel.getCharacters()
                    } else {
                        viewModel.filterCharacter()
                    }
                    isLoading = false
                }

                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                        isScrolling = true
                    }
                }
            })
        }
    }
}