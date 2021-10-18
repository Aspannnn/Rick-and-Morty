package kz.aspan.rickandmorty.presentation.adapters

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kz.aspan.rickandmorty.R
import kz.aspan.rickandmorty.databinding.CharacterCardViewBinding
import kz.aspan.rickandmorty.domain.model.character.Character
import javax.inject.Inject

class CharactersAdapter @Inject constructor() :
    RecyclerView.Adapter<CharactersAdapter.CharactersViewHolder>() {


    class CharactersViewHolder(val binding: CharacterCardViewBinding) :
        RecyclerView.ViewHolder(binding.root)


    suspend fun updateDataset(newDataset: List<Character>) = withContext(Dispatchers.Default) {
        val diff = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun getOldListSize(): Int {
                return characters.size
            }

            override fun getNewListSize(): Int {
                return newDataset.size
            }

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return characters[oldItemPosition].id == newDataset[newItemPosition].id
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return characters[oldItemPosition].id == newDataset[newItemPosition].id
            }
        })
        withContext(Dispatchers.Main) {
            characters = newDataset
            diff.dispatchUpdatesTo(this@CharactersAdapter)
        }
    }

    var characters = listOf<Character>()
        private set

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharactersViewHolder {
        return CharactersViewHolder(
            CharacterCardViewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: CharactersViewHolder, position: Int) {
        val character = characters[position]
        holder.binding.apply {
            characterNameTv.text = character.name
            characterImageInCv.load(character.image)
            isAliveView.backgroundTintList = getColor(character.status, holder)
            statusTv.text = "${character.status} - ${character.species}"
            root.setOnClickListener {
                onItemClickListener?.let { click ->
                    click(character)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return characters.size
    }

    private var onItemClickListener: ((Character) -> Unit)? = null

    fun setOnClickListener(listener: (Character) -> Unit) {
        onItemClickListener = listener
    }


    private fun getColor(status: String, holder: CharactersViewHolder): ColorStateList {
        return if (status == "Alive") {
            holder.itemView.context.getColorStateList(R.color.green_snake)
        } else {
            holder.itemView.context.getColorStateList(R.color.jasper)
        }
    }
}