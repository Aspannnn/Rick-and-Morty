package kz.aspan.rickandmorty.adapter

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import kz.aspan.rickandmorty.R
import kz.aspan.rickandmorty.databinding.CharacterCardViewBinding
import kz.aspan.rickandmorty.retrofit.model.character.Character

class CharactersAdapter :
    RecyclerView.Adapter<CharactersAdapter.CharactersViewHolder>() {


    class CharactersViewHolder(val binding: CharacterCardViewBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val differCallback = object : DiffUtil.ItemCallback<Character>() {
        override fun areItemsTheSame(oldItem: Character, newItem: Character): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Character, newItem: Character): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharactersViewHolder {
        return CharactersViewHolder(
            CharacterCardViewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: CharactersViewHolder, position: Int) {
        val character = differ.currentList[position]
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
        return differ.currentList.size
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