package com.example.mymediaplayer.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.example.mymediaplayer.R
import kotlinx.android.synthetic.main.music_navigation.view.*

class MusicNavigation : LinearLayout {

    private lateinit var rootView: LinearLayout
//    private lateinit var playPause : ImageButton
//    private lateinit var next : ImageButton
//    private lateinit var back : ImageButton
//    private lateinit var shuffle : ImageButton
//    private lateinit var pla

    lateinit var listener: OnMusicNavigationViewClickedListener

    private var isPlaying = false
    private var isShuffle = false
    private var playingMode = PLAY_MODE_REPEAT_NO

    constructor(context: Context?) : super(context) {
        context?.let {
            init(context)
        }
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        context?.let {
            init(context)
        }
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        context?.let {
            init(context)
        }
    }

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        context?.let {
            init(context)
        }
    }


    private fun init(context: Context) {
        rootView =
            LayoutInflater.from(context).inflate(R.layout.music_navigation, this) as LinearLayout
        playPause.setOnClickListener {
            listener?.let { listener.onPlayPauseClicked(isPlaying) }
            if (isPlaying) {
                playPause.setImageResource(R.drawable.ic_pause)
                isPlaying = false
            } else {
                playPause.setImageResource(R.drawable.ic_play)
                isPlaying = true
            }
        }
        next.setOnClickListener {
            listener?.let {
                listener.onNextClicked()
            }
        }
        back.setOnClickListener {
            listener.let {
                listener.onBackClicked()
            }
        }
        shuffle.setOnClickListener {
            listener.let {
                listener.onShuffleClicked(isShuffle)
            }
        }
        changePlayMode.setOnClickListener {
            listener.let {
                listener.onPlayModeClicked(playingMode)
            }
        }
    }

    interface OnMusicNavigationViewClickedListener {
        fun onNextClicked()

        fun onBackClicked()

        fun onPlayPauseClicked(isPlaying: Boolean)

        fun onShuffleClicked(isShuffle: Boolean)

        fun onPlayModeClicked(playMode: Int)
    }

    companion object {
        const val PLAY_MODE_REPEAT_ONE = 1001
        const val PLAY_MODE_REPEAT_ALL = 1002
        const val PLAY_MODE_REPEAT_NO = 1000
    }

}
