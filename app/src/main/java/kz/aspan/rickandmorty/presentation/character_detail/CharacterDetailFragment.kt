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
import kz.aspan.rickandmorty.R
import kz.aspan.rickandmorty.common.navigateSafely
import kz.aspan.rickandmorty.databinding.FragmentCharacterDetailBinding
import kz.aspan.rickandmorty.domain.model.character.Character
import kz.aspan.rickandmorty.presentation.adapters.CharacterDetailAdapter
import javax.inject.Inject

@AndroidEntryPoint
class CharacterDetailFragment : Fragment(R.layout.fragment_character_detail) {
    private var _binding: FragmentCharacterDetailBinding? = null
    private val binding: FragmentCharacterDetailBinding
        get() = _binding!!

    private val viewModel: CharacterDetailViewModel by viewModels()

    private val args: CharacterDetailFragmentArgs by navArgs()

    @Inject
    lateinit var episodeAdapter: CharacterDetailAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getEpisodes(args.character.episode)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCharacterDetailBinding.bind(view)
        val character: Character = args.character
        subscribeToObservers()
        setupRecyclerView()
        binding.apply {
            characterIv.load(character.image)
            collapsingToolBar.title = character.name
            isAliveView.backgroundTintList = getColor(character.status)
            statusTv.text = character.status
            speciesTv.text = character.species
            typeTv.text = character.type
            lastLocationTv.text = character.location.name
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


    private fun subscribeToObservers() = viewLifecycleOwner.lifecycleScope.launchWhenCreated {
        viewModel.episodes.collect { event ->
            when (event) {
                is CharacterDetailViewModel.DetailEvent.GetEpisodeLoading -> {
                }
                is CharacterDetailViewModel.DetailEvent.GetEpisodes -> {
                    episodeAdapter.updateDataset(event.episodes)
                    binding.firstSeenInTv.text = event.episodes[0].name
                }
                else -> Unit
            }

        }
    }


    private fun getColor(
        status: String,
    ): ColorStateList {
        return if (status == "Alive") {
            requireActivity().getColorStateList(R.color.green_snake)
        } else {
            requireActivity().getColorStateList(R.color.jasper)
        }
    }

    private fun setupRecyclerView() {
        binding.rvEpisode.apply {
            adapter = episodeAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }
}