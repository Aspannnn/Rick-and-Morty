package kz.aspan.rickandmorty.presentation.characters

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import kz.aspan.rickandmorty.R
import kz.aspan.rickandmorty.presentation.adapters.CharactersAdapter
import kz.aspan.rickandmorty.databinding.FragmentCharactersBinding

class CharactersFragment : Fragment(R.layout.fragment_characters) {
    private var _binding: FragmentCharactersBinding? = null
    private val binding: FragmentCharactersBinding
        get() = _binding!!

    private lateinit var charactersAdapter: CharactersAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        _binding = FragmentCharactersBinding.bind(view)

    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}