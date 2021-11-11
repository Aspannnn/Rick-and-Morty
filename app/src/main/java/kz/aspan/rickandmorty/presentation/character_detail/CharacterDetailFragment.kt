package kz.aspan.rickandmorty.presentation.character_detail

import android.content.res.ColorStateList
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kz.aspan.rickandmorty.R
import kz.aspan.rickandmorty.common.navigateSafely
import kz.aspan.rickandmorty.databinding.FragmentCharacterDetailBinding
import kz.aspan.rickandmorty.domain.model.character.Character
import kz.aspan.rickandmorty.adapters.EpisodeAdapter
import kz.aspan.rickandmorty.common.snackbar
import javax.inject.Inject

@AndroidEntryPoint
class CharacterDetailFragment : Fragment(R.layout.fragment_character_detail) {
    private var _binding: FragmentCharacterDetailBinding? = null
    private val binding: FragmentCharacterDetailBinding
        get() = _binding!!

    private val viewModel: CharacterDetailViewModel by viewModels()

    private val args: CharacterDetailFragmentArgs by navArgs()

    @Inject
    lateinit var episodeAdapter: EpisodeAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCharacterDetailBinding.bind(view)
        val character: Character = args.character
        subscribeToObservers()
        listenToEvents()
        setupRecyclerView()
        binding.apply {
            characterIv.load(character.image)
            collapsingToolBar.title = character.name
            isAliveView.backgroundTintList = getColor(character.status)
            statusTv.text = character.status
            speciesTv.text = character.species
            typeTv.text = character.type
            lastLocationTv.text = character.location.name

            lastLocationTv.setOnClickListener {
                findNavController().navigateSafely(
                    R.id.action_characterDetailFragment_to_locationDetailFragment,
                    Bundle().apply {
                        putString("locationUrl", character.location.url)
                    }
                )
            }
        }

        episodeAdapter.setOnEpisodeClickListener { episode ->
            findNavController().navigateSafely(
                R.id.action_characterDetailFragment_to_episodeFragment,
                Bundle().apply { putSerializable("episode", episode) }
            )
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


    private fun subscribeToObservers() = viewLifecycleOwner.lifecycleScope.launchWhenStarted {
        viewModel.episodes.collectLatest { event ->
            when (event) {
                is CharacterDetailViewModel.DetailEvent.GetEpisodeLoading -> {
                }
                is CharacterDetailViewModel.DetailEvent.GetEpisodes -> {
                    episodeAdapter.updateDataset(event.episodes)
                }
                else -> Unit
            }

        }
    }

    private fun listenToEvents() = viewLifecycleOwner.lifecycleScope.launchWhenStarted {
        viewModel.detailEvent.collect { event ->
            if (event is CharacterDetailViewModel.DetailEvent.GetEpisodesError) {
                snackbar(event.error)
            }
        }
    }


    private fun getColor(
        status: String,
    ): ColorStateList {
        return when (status) {
            "Alive" -> {
                requireActivity().getColorStateList(R.color.green_snake)
            }
            "Dead" -> {
                requireActivity().getColorStateList(R.color.jasper)
            }
            else -> {
                requireActivity().getColorStateList(R.color.gray)
            }
        }
    }

    private fun setupRecyclerView() {
        binding.rvEpisode.apply {
            adapter = episodeAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }
}