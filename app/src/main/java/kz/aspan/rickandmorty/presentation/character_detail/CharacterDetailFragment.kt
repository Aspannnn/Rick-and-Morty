package kz.aspan.rickandmorty.presentation.character_detail

import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.google.android.material.appbar.AppBarLayout
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
        setupTitle(character.name)
        setInformation(character)

        binding.lastLocation.setOnClickListener {
            findNavController().navigateSafely(
                R.id.action_characterDetailFragment_to_locationDetailFragment,
                Bundle().apply {
                    putString("locationUrl", character.location.url)
                }
            )
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

    private fun setupRecyclerView() {

        binding.rvEpisode.apply {
            adapter = episodeAdapter
            layoutManager = LinearLayoutManager(requireContext())
            val itemDecoration = DividerItemDecoration(activity, DividerItemDecoration.VERTICAL)
            val drawable: Drawable =
                ResourcesCompat.getDrawable(resources, R.drawable.devider, null)!!
            itemDecoration.setDrawable(drawable)
//            addItemDecoration(itemDecoration)
        }
    }

    private fun setupTitle(characterName: String) {
        var isShow = true
        var scrollRange = -1

        binding.appBarLayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { barLayout, verticalOffset ->
            if (scrollRange == -1) {
                scrollRange = barLayout?.totalScrollRange!!
            }
            if (scrollRange + verticalOffset == 0) {
                binding.collapsingToolBar.title = characterName
                isShow = true
            } else if (isShow) {
                binding.collapsingToolBar.title = " "
                isShow = false
            }
        })
    }

    private fun setInformation(character: Character) {
        binding.apply {
            characterIv.load(character.image)
            characterNameTv.text = character.name
            statusTv.text = character.status
            speciesTv.text = character.species.uppercase()
            lastLocationTv.text = character.location.name

            genderLayout.subtitleTv.text = "Gender"
            genderLayout.secondaryTextTV.text = character.gender

            originLayout.subtitleTv.text = "Origin"
            originLayout.secondaryTextTV.text = character.origin.name

            typeLayout.subtitleTv.text = "Type"
            val type =
                if (character.type.isEmpty() && character.species == "Human") "Human" else character.type
            typeLayout.secondaryTextTV.text = type
        }
    }
}