package kz.aspan.rickandmorty.presentation.adapters

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import kz.aspan.rickandmorty.R
import kz.aspan.rickandmorty.databinding.ItemCharacterBinding
import kz.aspan.rickandmorty.domain.model.character.Character
import javax.inject.Inject

class CharacterAdapter @Inject constructor() :
    PagingDataAdapter<Character, CharacterAdapter.CharacterViewHolder>(CharacterComparator) {



    class CharacterViewHolder(val binding: ItemCharacterBinding) :
        RecyclerView.ViewHolder(binding.root)

    object CharacterComparator : DiffUtil.ItemCallback<Character>() {
        override fun areItemsTheSame(oldItem: Character, newItem: Character): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Character, newItem: Character): Boolean {
            return oldItem.id == newItem.id
        }

    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        getItem(position)?.let { character ->
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
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        return CharacterAdapter.CharacterViewHolder(
            ItemCharacterBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }

    private var onItemClickListener: ((Character) -> Unit)? = null

    fun setOnClickListener(listener: (Character) -> Unit) {
        onItemClickListener = listener
    }


    private fun getColor(
        status: String,
        holder: CharacterAdapter.CharacterViewHolder
    ): ColorStateList {
        return if (status == "Alive") {
            holder.itemView.context.getColorStateList(R.color.green_snake)
        } else {
            holder.itemView.context.getColorStateList(R.color.jasper)
        }
    }


}