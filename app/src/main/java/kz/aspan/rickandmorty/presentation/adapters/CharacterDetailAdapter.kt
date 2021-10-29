package kz.aspan.rickandmorty.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kz.aspan.rickandmorty.databinding.ItemEpisodeBinding
import kz.aspan.rickandmorty.domain.model.episode.Episode
import javax.inject.Inject

class CharacterDetailAdapter @Inject constructor() :
    RecyclerView.Adapter<CharacterDetailAdapter.CharacterDetailViewHolder>() {

    class CharacterDetailViewHolder(val binding: ItemEpisodeBinding) :
        RecyclerView.ViewHolder(binding.root)

    suspend fun updateDataset(newDataset: List<Episode>) = withContext(Dispatchers.Default) {
        val diff = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun getOldListSize(): Int {
                return episodes.size
            }

            override fun getNewListSize(): Int {
                return newDataset.size
            }

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return episodes[oldItemPosition].id == newDataset[newItemPosition].id
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return episodes[oldItemPosition].id == newDataset[newItemPosition].id
            }
        })
        withContext(Dispatchers.Main) {
            episodes = newDataset
            diff.dispatchUpdatesTo(this@CharacterDetailAdapter)
        }
    }

    var episodes = listOf<Episode>()
        private set

    override fun getItemCount(): Int {
        return episodes.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterDetailViewHolder {
        return CharacterDetailViewHolder(
            ItemEpisodeBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CharacterDetailViewHolder, position: Int) {
        val episode = episodes[position]
        holder.binding.apply {
            episodeTv.text = episode.episode
            episodeTitleTv.text = episode.name

            root.setOnClickListener {
                onEpisodeClickListener?.let { click ->
                    click(episode)
                }
            }
        }
    }

    private var onEpisodeClickListener: ((Episode) -> Unit)? = null

    fun setOnEpisodeClickListener(listener: (Episode) -> Unit) {
        onEpisodeClickListener = listener
    }


}