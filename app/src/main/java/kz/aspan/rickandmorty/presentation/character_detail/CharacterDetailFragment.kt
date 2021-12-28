package kz.aspan.rickandmorty.presentation.character_detail

import android.content.res.ColorStateList
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import dagger.hilt.android.AndroidEntryPoint
import kz.aspan.rickandmorty.R
import kz.aspan.rickandmorty.common.navigateSafely
import kz.aspan.rickandmorty.databinding.FragmentCharacterDetailBinding
import kz.aspan.rickandmorty.domain.model.character.Character
import kz.aspan.rickandmorty.adapters.EpisodeListAdapter
import kz.aspan.rickandmorty.common.Response
import javax.inject.Inject

@AndroidEntryPoint
class CharacterDetailFragment : Fragment(R.layout.fragment_character_detail) {
    private var _binding: FragmentCharacterDetailBinding? = null
    private val binding: FragmentCharacterDetailBinding
        get() = _binding!!

    private val viewModel: CharacterDetailViewModel by viewModels()

    private val args: CharacterDetailFragmentArgs by navArgs()

    @Inject
    lateinit var episodeAdapter: EpisodeListAdapter

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


    private fun subscribeToObservers() {
        viewModel.episodeMutableLiveData.observe(viewLifecycleOwner, { response ->
            when (response) {
                is Response.Loading -> {}
                is Response.Success -> {
                    response.data?.let { episode ->
                        episodeAdapter.submitList(episode)
                    }
                }
                is Response.Error -> {
                    TODO("Agai dan surau kerek")
                }
            }
        })
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
            addItemDecoration(
                DividerItemDecoration(
                    activity,
                    LinearLayoutManager.VERTICAL
                )
            )
        }
    }
}