package kz.aspan.rickandmorty.presentation.episode

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kz.aspan.rickandmorty.R
import kz.aspan.rickandmorty.common.navigateSafely
import kz.aspan.rickandmorty.databinding.FragmentEpisodeBinding
import kz.aspan.rickandmorty.domain.model.episode.Episode
import kz.aspan.rickandmorty.presentation.adapters.CharacterAdapter
import javax.inject.Inject

@AndroidEntryPoint
class EpisodeFragment : Fragment(R.layout.fragment_episode) {
    private var _binding: FragmentEpisodeBinding? = null
    private val binding: FragmentEpisodeBinding
        get() = _binding!!

    private val viewModel: EpisodeViewModel by viewModels()

    private val args: EpisodeFragmentArgs by navArgs()

    @Inject
    lateinit var characterAdapter: CharacterAdapter

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


    private fun subscribeToObservers() = viewLifecycleOwner.lifecycleScope.launchWhenStarted {
        viewModel.character.collectLatest{ event ->
            when (event) {
                is EpisodeViewModel.EpisodeEvent.Loading -> {
                }
                is EpisodeViewModel.EpisodeEvent.GetCharacter -> {
                    characterAdapter.updateDataset(event.characters)
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