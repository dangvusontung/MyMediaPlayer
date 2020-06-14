package com.example.mymediaplayer.ui.list

class Music(
    val data : String,
    val title : String,
    val album : String,
    val artist : String
) {
    override fun toString(): String {
        return "Music(data='$data', title='$title', album='$album', artist='$artist')"
    }
}