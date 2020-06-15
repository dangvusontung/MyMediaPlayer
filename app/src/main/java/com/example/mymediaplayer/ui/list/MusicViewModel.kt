package com.example.mymediaplayer.ui.list

import android.app.Application
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.AndroidViewModel
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util


class MusicViewModel(application: Application) : AndroidViewModel(application) {

    val context: Context

    init {
        context = application
    }

    fun getMusicListByName(): List<Music> {
        val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        val selection = "${MediaStore.Audio.Media.IS_MUSIC} != 0"
        val sortOrder = "${MediaStore.Audio.Media.TITLE}  ASC"
        return getMusic(uri, selection, sortOrder)
    }

    private fun getMusic(uri: Uri, selection: String, sortOrder: String): List<Music> {
        val list = ArrayList<Music>()
        val cursor = context.contentResolver.query(uri, null, selection, null, sortOrder);
        if (cursor != null && cursor.count > 0) {
            while (cursor.moveToNext()) {
                val data =
                    cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA))
                val title =
                    cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE))
                val album =
                    cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM))
                val artist =
                    cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST))

                list.add(Music(data, title, album, artist))
            }
        }
        return list
    }

    fun extractMediaSourceFromUriPath(uriPath: String): MediaSource {
        val userAgent = Util.getUserAgent(context, "Exo")
        val uri: Uri = Uri.parse(uriPath)
        return ExtractorMediaSource.Factory(DefaultDataSourceFactory(context, userAgent))
            .setExtractorsFactory(DefaultExtractorsFactory()).createMediaSource(uri)
    }

    fun extractMediaSourceFromUri(uri: Uri): MediaSource {
        val userAgent = Util.getUserAgent(context, "Exo")
        return ExtractorMediaSource.Factory(DefaultDataSourceFactory(context, userAgent))
            .setExtractorsFactory(DefaultExtractorsFactory()).createMediaSource(uri)
    }

    private val dataSourceFactory = DefaultDataSourceFactory(context, "exo-player")
    private val mediaSourceFactory = ProgressiveMediaSource.Factory(dataSourceFactory)

    fun createMediaSource(): MediaSource {
        val mediaSource = ConcatenatingMediaSource()
        for (music in getMusicListByName()) {
            mediaSource.addMediaSource(mediaSourceFactory.createMediaSource(Uri.parse(music.data)))
        }

        return mediaSource
    }

}