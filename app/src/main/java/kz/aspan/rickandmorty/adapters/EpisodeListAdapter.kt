package kz.aspan.rickandmorty.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kz.aspan.rickandmorty.databinding.ItemEpisodeBinding
import kz.aspan.rickandmorty.domain.model.episode.Episode
import javax.inject.Inject

class EpisodeListAdapter @Inject constructor() :
    ListAdapter<Episode, EpisodeListAdapter.EpisodeViewHolder>(EpisodeItemDiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EpisodeViewHolder {
        return EpisodeViewHolder(
            ItemEpisodeBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: EpisodeViewHolder, position: Int) {
        val episode = getItem(position)
        holder.binding.apply {
            episodeTv.text = episode.episode.replace("S", "Season:").replace("E", " Episode:")

            episodeTitleTv.text = episode.name

            root.setOnClickListener {
                onEpisodeClickListener?.let { click ->
                    click(episode)
                }
            }
        }
    }

    class EpisodeViewHolder(val binding: ItemEpisodeBinding) : RecyclerView.ViewHolder(binding.root)

    class EpisodeItemDiffCallback : DiffUtil.ItemCallback<Episode>() {
        override fun areItemsTheSame(oldItem: Episode, newItem: Episode): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Episode, newItem: Episode): Boolean {
            return oldItem == newItem
        }
    }


    private var onEpisodeClickListener: ((Episode) -> Unit)? = null

    fun setOnEpisodeClickListener(listener: (Episode) -> Unit) {
        onEpisodeClickListener = listener
    }
}