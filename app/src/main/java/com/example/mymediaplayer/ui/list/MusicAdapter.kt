package com.example.mymediaplayer.ui.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.mymediaplayer.R
import com.example.mymediaplayer.databinding.MusicListItemBinding

class MusicAdapter(private val list: List<Music>, private val listener: OnMusicItemSelected) :
    RecyclerView.Adapter<MusicViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = DataBindingUtil.inflate<MusicListItemBinding>(
            inflater,
            R.layout.music_list_item,
            parent,
            false
        )
        return MusicViewHolder(view)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: MusicViewHolder, position: Int) {
        holder.bindData(list[position], listener)
    }

}

class MusicViewHolder(private val binding: MusicListItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    val container = binding.root.findViewById<ConstraintLayout>(R.id.container)

    fun bindData(music: Music, listener: OnMusicItemSelected) {
        binding.song = music
        container.setOnClickListener { listener.onMusicItemSelected(music) }
    }


}

interface OnMusicItemSelected {
    fun onMusicItemSelected(music: Music)
}