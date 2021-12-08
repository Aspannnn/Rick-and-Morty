package kz.aspan.rickandmorty.presentation.characters

import android.os.Bundle
import android.view.View
import android.widget.AbsListView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kz.aspan.rickandmorty.R
import kz.aspan.rickandmorty.adapters.CharacterListAdapter
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

    private var isFilterCharacter = false


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

//        binding.searchEt.addTextChangedListener {
//            viewLifecycleOwner.lifecycleScope.launch {
//                delay(3000)
//                if (it.isNullOrEmpty()) {
//                    isFilterCharacter = false
//                    viewModel.returnToCharacter()
//                } else {
//                    isFilterCharacter = true
//                    viewModel.filterCharacter(name = it.toString())
//                    LinearLayoutManager(requireContext()).scrollToPosition(0)
//                }
//            }
//        }
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
                        val totalPages = viewModel.charactersInfo?.pages
                        isLastPage = viewModel.page == totalPages
                    }
                }
                is Response.Error -> {
                    hideProgressBar()
                    TODO("Agai dan surau kerek")
                }
            }
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
        val linearLayoutManager = LinearLayoutManager(requireActivity())
        binding.charactersRv.apply {
            adapter = characterAdapter
            layoutManager = linearLayoutManager

            addOnScrollListener(object : PaginationScrollListener(linearLayoutManager) {
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
//                    if (isFilterCharacter) {
//                        viewModel.filterNextPage()
//                    } else {
                    viewModel.nextPage()
//                    }
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