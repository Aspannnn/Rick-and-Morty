package kz.aspan.rickandmorty.presentation.episode

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kz.aspan.rickandmorty.R
import kz.aspan.rickandmorty.common.navigateSafely
import kz.aspan.rickandmorty.databinding.FragmentEpisodeBinding
import kz.aspan.rickandmorty.domain.model.episode.Episode
import kz.aspan.rickandmorty.adapters.CharacterListAdapter
import kz.aspan.rickandmorty.common.GridSpacingItemDecoration
import kz.aspan.rickandmorty.common.Response
import javax.inject.Inject

@AndroidEntryPoint
class EpisodeFragment : Fragment(R.layout.fragment_episode) {
    private var _binding: FragmentEpisodeBinding? = null
    private val binding: FragmentEpisodeBinding
        get() = _binding!!

    private val viewModel: EpisodeViewModel by viewModels()

    private val args: EpisodeFragmentArgs by navArgs()

    @Inject
    lateinit var characterAdapter: CharacterListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentEpisodeBinding.bind(view)
        subscribeToObservers()
        setRecyclerView()

        val episode: Episode = args.episode
        binding.apply {
            titleTv.text = episode.name
            episodeTv.text = episode.episode
            airDateTv.text = episode.airDate
        }

        characterAdapter.setOnCharacterClickListener { character ->
            findNavController().navigateSafely(
                R.id.action_episodeFragment_to_characterDetailFragment,
                Bundle().apply { putSerializable("character", character) }
            )
        }
    }

    private fun subscribeToObservers() {
        viewModel.characterMutableLiveData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Response.Loading -> {}
                is Response.Success -> {
                    response.data?.let { episode ->
                        characterAdapter.submitList(episode)
                    }
                }
                is Response.Error -> {
                    TODO("Agai dan surau kerek")
                }
                else -> {}
            }
        }
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