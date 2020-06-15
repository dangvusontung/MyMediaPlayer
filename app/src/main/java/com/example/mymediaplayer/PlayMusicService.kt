package com.example.mymediaplayer

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Binder
import android.os.IBinder
import android.util.Log
import androidx.annotation.DrawableRes
import androidx.annotation.MainThread
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.lifecycle.LifecycleService
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.audio.AudioAttributes
import com.google.android.exoplayer2.core.BuildConfig
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util

private const val TAG = "PlayMusicService"

class PlayMusicService : LifecycleService() {

    private lateinit var exoPlayer: SimpleExoPlayer

    private var playerNotificationManager: PlayerNotificationManager? = null

    override fun onCreate() {
        super.onCreate()
        exoPlayer = ExoPlayerFactory.newSimpleInstance(this, DefaultTrackSelector())
        val audioAttributes = AudioAttributes.Builder()
            .setContentType(C.CONTENT_TYPE_MUSIC)
            .setUsage(C.USAGE_MEDIA)
            .build()

        exoPlayer.setAudioAttributes(audioAttributes, true)
        playerNotificationManager = PlayerNotificationManager.createWithNotificationChannel(
            applicationContext,
            PLAYBACK_CHANNEL_ID,
            R.string.channel_name,
            PLAYBACK_NOTIFICATION_ID,
            mediaDescriptionAdapter,
            notificationListener
        ).apply {

        }

        exoPlayer.addListener(PlayEventListener())

    }

    private val mediaDescriptionAdapter =
        object : PlayerNotificationManager.MediaDescriptionAdapter {
            override fun createCurrentContentIntent(player: Player?): PendingIntent? {
                return PendingIntent.getActivity(
                    applicationContext,
                    0,
                    Intent(applicationContext, MainActivity::class.java),
                    PendingIntent.FLAG_UPDATE_CURRENT
                )
            }

            override fun getCurrentContentText(player: Player?): String? = null

            override fun getCurrentContentTitle(player: Player?): String {
                TODO("Not yet implemented")
            }

            override fun getCurrentLargeIcon(
                player: Player?,
                callback: PlayerNotificationManager.BitmapCallback?
            ): Bitmap? {
                return getBitmapFromVectorDrawable(
                    applicationContext,
                    R.drawable.ic_music_note_black_24dp
                )
            }
        }

    private val notificationListener = object : PlayerNotificationManager.NotificationListener {
        override fun onNotificationCancelled(notificationId: Int) {
            stopSelf()
        }

        override fun onNotificationStarted(notificationId: Int, notification: Notification?) {
            startForeground(notificationId, notification)
        }

        override fun onNotificationPosted(
            notificationId: Int,
            notification: Notification?,
            ongoing: Boolean
        ) {
            if (ongoing) startForeground(notificationId, notification) else stopForeground(false)
        }

    }

    @MainThread
    fun play(uri: Uri, startPosition: Long, playbackSpeed: Float? = null) {
        Log.d(TAG, "service play")
        val userAgent = Util.getUserAgent(applicationContext, BuildConfig.APPLICATION_ID)
        val mediaSource = ExtractorMediaSource(
            uri,
            DefaultDataSourceFactory(applicationContext, userAgent),
            DefaultExtractorsFactory(),
            null,
            null
        )
        val haveStartPosition = startPosition != C.POSITION_UNSET.toLong()
        if (haveStartPosition) {
            exoPlayer.seekTo(startPosition)
        }
        exoPlayer.prepare(mediaSource, !haveStartPosition, false)
        exoPlayer.playWhenReady = true
    }

    @MainThread
    fun resume() {
        exoPlayer.playWhenReady = true
    }

    @MainThread
    fun stop() {
        exoPlayer.playWhenReady = false
    }

    override fun onDestroy() {
        super.onDestroy()
        exoPlayer.release()
    }

    @MainThread
    private fun getBitmapFromVectorDrawable(
        context: Context,
        @DrawableRes drawableId: Int
    ): Bitmap? {
        return ContextCompat.getDrawable(context, drawableId)?.let {
            val drawable = DrawableCompat.wrap(it).mutate()
            val bitmap = Bitmap.createBitmap(
                drawable.intrinsicWidth,
                drawable.intrinsicHeight,
                Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
            bitmap
        }
    }

    companion object {

        private const val ARG_URI = "uri_string"
        private const val ARG_TITLE = "title"
        private const val ARG_START_POSITION = "start_position"

        private const val PLAYBACK_CHANNEL_ID = "playback_channel"
        private const val PLAYBACK_NOTIFICATION_ID = 1

        @MainThread
        fun newIntent(
            context: Context,
            title: String,
            uri: String,
            startPosition: Long
        ) = Intent(
            context,
            PlayMusicService::class.java
        ).apply {
            putExtra(ARG_URI, uri)
            putExtra(ARG_TITLE, title)
            putExtra(ARG_START_POSITION, startPosition)
        }

    }

    private inner class PlayEventListener : Player.EventListener {
        override fun onPlayerError(error: ExoPlaybackException?) {

        }

        override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
            if (playbackState == Player.STATE_READY) {
                if (exoPlayer.playWhenReady) {
                    // In Playing state
                } else {
                    // In Paused state
                }
            } else if (playbackState == Player.STATE_ENDED) {
                // In Ended state
            }
        }
    }

    inner class PlayMusicServiceBinder : Binder() {
        val service
            get() = this@PlayMusicService
        val exoPlayer
            get() = this@PlayMusicService.exoPlayer
    }

    override fun onBind(intent: Intent): IBinder? {
        super.onBind(intent)
        return PlayMusicServiceBinder()
    }
}