package com.example.mymediaplayer.ui.list

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.net.Uri
import android.os.Bundle
import android.os.IBinder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mymediaplayer.PlayMusicService
import com.example.mymediaplayer.R
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.audio.AudioAttributes
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import kotlinx.android.synthetic.main.fragment_music_list.*

/**
 * A simple [Fragment] subclass.
 */

private const val TAG = "MusicListFragment"

class MusicListFragment : Fragment(), OnMusicItemSelected {

    private val viewModel: MusicViewModel by viewModels()

    private lateinit var musicAdapter: MusicAdapter

    private lateinit var exoPlayer: ExoPlayer

    private var musicService: PlayMusicService? = null

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

        setUpPlayer()

        player_view.player = exoPlayer

        context?.let {
            val intent = Intent(it, PlayMusicService::class.java).also { intent ->
                it.bindService(intent, connection, Context.BIND_AUTO_CREATE)
            }
        }

    }

    override fun onMusicItemSelected(music: Music) {
        val mediaSource = viewModel.extractMediaSourceFromUriPath(music.data)
//        exoPlayer.prepare(mediaSource)
//        exoPlayer.playWhenReady = true

        musicService?.play(Uri.parse(music.data), 0, 0.toFloat())
    }

    override fun onDetach() {
        super.onDetach()
        context?.let {
            it.unbindService(connection)
        }
    }

    private fun setUpPlayer() {
        exoPlayer = ExoPlayerFactory.newSimpleInstance(
            viewModel.context,
            DefaultRenderersFactory(viewModel.context),
            DefaultTrackSelector(),
            DefaultLoadControl()
        )
        val attr = AudioAttributes.Builder().setUsage(C.USAGE_MEDIA)
            .setContentType(C.CONTENT_TYPE_MUSIC)
            .build();
        (exoPlayer as SimpleExoPlayer?)?.setAudioAttributes(attr, true)

    }

    private val connection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {
            musicService = null
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as PlayMusicService.PlayMusicServiceBinder
            musicService = binder.service

            player_view.player = binder.exoPlayer
        }
    }
}
