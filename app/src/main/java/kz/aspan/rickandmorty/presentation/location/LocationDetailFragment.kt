package kz.aspan.rickandmorty.presentation.location

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import kz.aspan.rickandmorty.R
import kz.aspan.rickandmorty.databinding.FragmentLocationBinding


@AndroidEntryPoint
class LocationDetailFragment : Fragment(R.layout.fragment_location) {
//    private var _binding: Fragmentlo? = null
//    private val binding: FragmentLocationBinding
//        get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroy() {
        super.onDestroy()
//        _binding = null
    }
}