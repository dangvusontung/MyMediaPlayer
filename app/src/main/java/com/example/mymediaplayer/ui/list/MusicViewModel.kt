package com.example.mymediaplayer.ui.list

import android.app.Application
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.AndroidViewModel


class MusicViewModel(application: Application) : AndroidViewModel(application) {

    private val context : Context

    init {
        context = application
    }

    fun getMusicListByName() : List<Music>{
        val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        val selection = "${MediaStore.Audio.Media.IS_MUSIC} != 0"
        val sortOrder = "${MediaStore.Audio.Media.TITLE}  ASC"
        return getMusic(uri, selection, sortOrder)
    }

    private fun getMusic(uri : Uri, selection : String, sortOrder : String) : List<Music> {
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
}