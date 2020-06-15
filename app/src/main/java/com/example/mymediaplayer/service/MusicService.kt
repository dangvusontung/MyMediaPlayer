package com.example.mymediaplayer.service

import android.net.Uri
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.media.MediaBrowserServiceCompat
import com.example.mymediaplayer.ui.list.MusicViewModel
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.audio.AudioAttributes
import com.google.android.exoplayer2.source.MediaSource

class MusicService(private val viewModel: MusicViewModel) : MediaBrowserServiceCompat() {

    private var mediaSession: MediaSessionCompat? = null
    private lateinit var stateBuilder: PlaybackStateCompat.Builder
    private var exoPlayer: SimpleExoPlayer? = null
    private var oldUri: Uri? = null
    private var callback = object : MediaSessionCompat.Callback() {
        override fun onPlayFromUri(uri: Uri?, extras: Bundle?) {
            super.onPlayFromUri(uri, extras)
            uri?.let {
                val mediaSource = viewModel.extractMediaSourceFromUri(uri)
                if (it != oldUri) play(mediaSource) else play()
                oldUri = uri
            }
        }

        override fun onPause() {
            super.onPause()

        }

        override fun onStop() {
            super.onStop()

        }

    }

    private fun play(mediaSource: MediaSource) {

    }

    private fun play() {

    }

    override fun onLoadChildren(
        parentId: String,
        result: Result<MutableList<MediaBrowserCompat.MediaItem>>
    ) {
        TODO("Not yet implemented")
    }

    override fun onGetRoot(
        clientPackageName: String,
        clientUid: Int,
        rootHints: Bundle?
    ): BrowserRoot? {
        TODO("Not yet implemented")
    }

    override fun onCreate() {
        super.onCreate()
    }

    private val attrs: AudioAttributes? = null

}
