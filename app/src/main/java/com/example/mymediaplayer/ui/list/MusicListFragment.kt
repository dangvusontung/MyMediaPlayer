package com.example.mymediaplayer.ui.list

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.mymediaplayer.R
import kotlinx.android.synthetic.main.fragment_music_list.*

/**
 * A simple [Fragment] subclass.
 */

private const val TAG = "MusicListFragment"
class MusicListFragment : Fragment(), OnMusicItemSelected {

    private val viewModel : MusicViewModel by viewModels()

    private lateinit var musicAdapter: MusicAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_music_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val musicList = viewModel.getMusicListByName()

        musicAdapter = MusicAdapter(musicList, this)

        with(music_list) {
            adapter = musicAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    override fun onMusicItemSelected(music: Music) {
        TODO("Not yet implemented")
    }
}
