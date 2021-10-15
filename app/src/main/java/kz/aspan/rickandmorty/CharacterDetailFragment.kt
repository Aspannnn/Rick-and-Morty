package kz.aspan.rickandmorty

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import coil.load
import kz.aspan.rickandmorty.databinding.FragmentCharacterDetailBinding
import kz.aspan.rickandmorty.retrofit.model.character.Character

class CharacterDetailFragment : Fragment(R.layout.fragment_character_detail) {
    private var _binding: FragmentCharacterDetailBinding? = null
    private val binding: FragmentCharacterDetailBinding
        get() = _binding!!


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        _binding = FragmentCharacterDetailBinding.bind(view)
        val character: Character = arguments?.getSerializable("character") as Character
        binding.apply {
            characterIv.load(character.image)
            characterNameTv.text = character.name
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}