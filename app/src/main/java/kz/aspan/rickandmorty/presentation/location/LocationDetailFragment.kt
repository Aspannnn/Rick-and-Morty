package kz.aspan.rickandmorty.presentation.location

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kz.aspan.rickandmorty.R
import kz.aspan.rickandmorty.adapters.CharacterListAdapter
import kz.aspan.rickandmorty.common.GridSpacingItemDecoration
import kz.aspan.rickandmorty.common.Response
import kz.aspan.rickandmorty.common.navigateSafely
import kz.aspan.rickandmorty.databinding.FragmentLocationDetailBinding
import javax.inject.Inject


@AndroidEntryPoint
class LocationDetailFragment : Fragment(R.layout.fragment_location_detail) {
    private var _binding: FragmentLocationDetailBinding? = null
    private val binding: FragmentLocationDetailBinding
        get() = _binding!!


    private val viewModel: LocationDetailViewModel by viewModels()

    @Inject
    lateinit var characterAdapter: CharacterListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentLocationDetailBinding.bind(view)
        subscribeToObservers()
        setRecyclerView()

        characterAdapter.setOnCharacterClickListener { character ->
            findNavController().navigateSafely(
                R.id.action_locationDetailFragment_to_characterDetailFragment,
                Bundle().apply { putSerializable("character", character) }
            )
        }
    }


    private fun subscribeToObservers() {
        viewModel.locationMutableLiveData.observe(viewLifecycleOwner, { response ->
            when (response) {
                is Response.Loading -> {}
                is Response.Success -> {
                    val location = response.data
                    binding.apply {
                        locationNameTv.text = location.name
                        dimensionTv.text = location.dimension
                        typeTv.text = location.type
                    }
                }
                is Response.Error -> {
                    TODO("Agai dan surau kerek")
                }
            }
        })

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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setRecyclerView() {
        val spanCount = 2
        val spacing = 100
        val includeEdge = true

        binding.rvCharacters.apply {
            adapter = characterAdapter
            addItemDecoration(GridSpacingItemDecoration(spanCount, spacing, includeEdge))
        }
    }
}