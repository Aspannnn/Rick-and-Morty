package kz.aspan.rickandmorty

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kz.aspan.rickandmorty.adapter.CharactersAdapter
import kz.aspan.rickandmorty.databinding.FragmentCharactersBinding
import kz.aspan.rickandmorty.retrofit.model.RetrofitConfig
import kz.aspan.rickandmorty.retrofit.model.character.Characters
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CharactersFragment : Fragment(R.layout.fragment_characters) {
    private var _binding: FragmentCharactersBinding? = null
    private val binding: FragmentCharactersBinding
        get() = _binding!!

    private lateinit var charactersAdapter: CharactersAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        _binding = FragmentCharactersBinding.bind(view)

        setupRecyclerView()

        getCharacters()


        val characterDetail = CharacterDetailFragment()
        charactersAdapter.setOnClickListener {
            val bundle = Bundle()
            bundle.putSerializable("character", it)
            characterDetail.arguments = bundle
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.container, characterDetail)
                .addToBackStack(null)
                .commit()
        }
    }


    private fun setupRecyclerView() {
        charactersAdapter = CharactersAdapter()
        binding.charactersRv.apply {
            adapter = charactersAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }


    private fun getCharacters() {
        RetrofitConfig.rickAndMortyApi.getCharacters().enqueue(object : Callback<Characters> {
            override fun onResponse(call: Call<Characters>, response: Response<Characters>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        charactersAdapter.differ.submitList(it.results)
                    }
                }
            }

            override fun onFailure(call: Call<Characters>, t: Throwable) {
                TODO("Show error message snackbar")
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}