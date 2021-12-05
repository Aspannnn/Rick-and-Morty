package kz.aspan.rickandmorty.adapters

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import kz.aspan.rickandmorty.R
import kz.aspan.rickandmorty.databinding.ItemCharacterBinding
import kz.aspan.rickandmorty.domain.model.character.Character
import javax.inject.Inject

class CharacterListAdapter @Inject constructor() :
    ListAdapter<Character, CharacterListAdapter.CharacterViewHolder>(CharacterItemDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        return CharacterViewHolder(
            ItemCharacterBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        val character = getItem(position)
        holder.binding.apply {
            characterNameTv.text = character.name
            characterImageInCv.load(character.image)
            isAliveView.backgroundTintList = getColor(character.status, holder)
            statusTv.text = "${character.status} - ${character.species}"
            root.setOnClickListener {
                onCharacterClickListener?.let { click ->
                    click(character)
                }
            }
        }
    }


    class CharacterViewHolder(val binding: ItemCharacterBinding) :
        RecyclerView.ViewHolder(binding.root)

    class CharacterItemDiffCallback : DiffUtil.ItemCallback<Character>() {
        override fun areItemsTheSame(oldItem: Character, newItem: Character): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Character, newItem: Character): Boolean {
            return oldItem == newItem
        }
    }

    fun setOnCharacterClickListener(listener: (Character) -> Unit) {
        onCharacterClickListener = listener
    }


    private var onCharacterClickListener: ((Character) -> Unit)? = null

    private fun getColor(
        status: String,
        holder: CharacterViewHolder
    ): ColorStateList {
        return when (status) {
            "Alive" -> {
                holder.itemView.context.getColorStateList(R.color.green_snake)
            }
            "Dead" -> {
                holder.itemView.context.getColorStateList(R.color.jasper)
            }
            else -> {
                holder.itemView.context.getColorStateList(R.color.gray)
            }
        }
    }
}