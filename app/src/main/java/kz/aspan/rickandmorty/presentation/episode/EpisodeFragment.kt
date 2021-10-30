package kz.aspan.rickandmorty.presentation.episode

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import kz.aspan.rickandmorty.R
import kz.aspan.rickandmorty.databinding.FragmentEpisodeBinding

@AndroidEntryPoint
class EpisodeFragment : Fragment(R.layout.fragment_episode) {
    private var _binding: FragmentEpisodeBinding? = null
    private val binding: FragmentEpisodeBinding
        get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentEpisodeBinding.bind(view)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}