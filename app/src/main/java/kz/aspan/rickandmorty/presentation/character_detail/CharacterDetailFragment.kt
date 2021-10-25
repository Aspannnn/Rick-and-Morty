package kz.aspan.rickandmorty.presentation.character_detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.navigation.fragment.navArgs
import coil.load
import kz.aspan.rickandmorty.R
import kz.aspan.rickandmorty.databinding.FragmentCharacterDetailBinding
import kz.aspan.rickandmorty.domain.model.character.Character


class CharacterDetailFragment : Fragment(R.layout.fragment_character_detail) {
    private var _binding: FragmentCharacterDetailBinding? = null
    private val binding: FragmentCharacterDetailBinding
        get() = _binding!!

    private val args: CharacterDetailFragmentArgs by navArgs()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        _binding = FragmentCharacterDetailBinding.bind(view)
        val character: Character = args.character
        binding.apply {
            characterIv.load(character.image)
            collapsingToolBar.title = character.name
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}