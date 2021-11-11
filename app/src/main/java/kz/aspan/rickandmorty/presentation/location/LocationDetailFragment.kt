package kz.aspan.rickandmorty.presentation.location

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kz.aspan.rickandmorty.R
import kz.aspan.rickandmorty.adapters.CharacterAdapter
import kz.aspan.rickandmorty.common.navigateSafely
import kz.aspan.rickandmorty.common.snackbar
import kz.aspan.rickandmorty.databinding.FragmentLocationDetailBinding
import javax.inject.Inject


@AndroidEntryPoint
class LocationDetailFragment : Fragment(R.layout.fragment_location_detail) {
    private var _binding: FragmentLocationDetailBinding? = null
    private val binding: FragmentLocationDetailBinding
        get() = _binding!!


    private val viewModel: LocationDetailViewModel by viewModels()

    @Inject
    lateinit var characterAdapter: CharacterAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentLocationDetailBinding.bind(view)
        subscribeToObservers()
        listenToEvent()
        setRecyclerView()

        characterAdapter.setOnCharacterClickListener { character ->
            findNavController().navigateSafely(
                R.id.action_locationDetailFragment_to_characterDetailFragment,
                Bundle().apply { putSerializable("character", character) }
            )
        }
    }


    private fun subscribeToObservers() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.location.collectLatest { event ->
                when (event) {
                    is LocationDetailViewModel.LocationEvent.Loading -> {
                    }
                    is LocationDetailViewModel.LocationEvent.GetLocation -> {
                        val location = event.location
                        binding.apply {
                            locationNameTv.text = location.name
                            dimensionTv.text = location.dimension
                            typeTv.text = location.type
                        }
                    }
                    else -> Unit
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.character.collectLatest { event ->
                when (event) {
                    is LocationDetailViewModel.LocationEvent.Loading -> {
                    }
                    is LocationDetailViewModel.LocationEvent.GetCharacter -> {
                        characterAdapter.updateDataset(event.characters)
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun listenToEvent() = viewLifecycleOwner.lifecycleScope.launchWhenStarted {
        viewModel.locationEvent.collect { event ->
            when (event) {
                is LocationDetailViewModel.LocationEvent.GetLocationError -> {
                    snackbar(event.error)
                }
                is LocationDetailViewModel.LocationEvent.GetCharacterError -> {
                    snackbar(event.error)
                }
                else -> Unit
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setRecyclerView() {
        binding.rvCharacters.apply {
            adapter = characterAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }
}