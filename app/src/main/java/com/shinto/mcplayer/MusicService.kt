package com.shinto.mcplayer

import android.app.Notification
import android.app.Service
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.os.Binder
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.support.v4.media.session.MediaSessionCompat
import androidx.core.app.NotificationCompat

class MusicService : Service() {

    private var myBinder = MyBinder()
    var mediaPlayer: MediaPlayer? = null
    private lateinit var runnable: Runnable
    private lateinit var mediaSession: MediaSessionCompat

    override fun onBind(p0: Intent?): IBinder {
        mediaSession = MediaSessionCompat(baseContext, "My Music")
        return myBinder
    }

    // Pass the context
    inner class MyBinder : Binder() {
        fun currentService(): MusicService {
            return this@MusicService
        }
    }

    fun showNotification() {
        val notification = NotificationCompat.Builder(baseContext, ApplicationClass.CHANNEL_ID)
            .setContentTitle(Player_activity.musicListPA[Player_activity.songPosition].title)
            .setContentText(Player_activity.musicListPA[Player_activity.songPosition].artist)
            .setSmallIcon(R.drawable.favourites)
            .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.play))
            .setStyle(androidx.media.app.NotificationCompat.MediaStyle().setMediaSession(mediaSession.sessionToken))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setOnlyAlertOnce(true)
            .addAction(R.drawable.previews, "Previous", null)
            .addAction(R.drawable.play, "Play", null)
            .addAction(R.drawable.next, "Next", null)
            .addAction(R.drawable.back, "back", null)
            .build()

        startForeground(13,notification)
    }

//    fun createMediaPlayer() {
//        try {
//            if (Player_activity.musicService!!.mediaPlayer == null) Player_activity.musicService!!.mediaPlayer =
//                MediaPlayer()
//            Player_activity.musicService!!.mediaPlayer!!.reset()
//            Player_activity.musicService!!.mediaPlayer!!.setDataSource(Player_activity.musicListPA[Player_activity.songPosition].path)
//            Player_activity.musicService!!.mediaPlayer!!.prepare()
//            //  Player_activity.musicService!!.mediaPlayer!!.start()
//            Player_activity.binding.playPauseButton.setIconResource(R.drawable.pause)
////            Player_activity.binding.seekBarStartPA.text =
////                formatDuration(mediaPlayer!!.currentPosition.toLong())
////            Player_activity.binding.seekBarEndPA.text =
////                formatDuration(mediaPlayer!!.duration.toLong())
//            Player_activity.binding.seekBarPA.progress = 0
//            Player_activity.binding.seekBarPA.max =
//                Player_activity.musicService!!.mediaPlayer!!.duration
//        } catch (e: Exception) {
//            return
//        }
//    }

    fun createMediaPlayer() {
        try {
            if (Player_activity.musicService!!.mediaPlayer == null) Player_activity.musicService!!.mediaPlayer =
                MediaPlayer()
            Player_activity.musicService!!.mediaPlayer!!.reset()
            Player_activity.musicService!!.mediaPlayer!!.setDataSource(Player_activity.musicListPA[Player_activity.songPosition].path)
            Player_activity.musicService!!.mediaPlayer!!.prepare()

            Player_activity.binding.playPauseButton.setIconResource(R.drawable.pause)
            Player_activity.binding.seekBarStartPA.text =
                formatDuration(mediaPlayer!!.currentPosition.toLong())
            Player_activity.binding.seekBarEndPA.text =
                formatDuration(mediaPlayer!!.duration.toLong())
            Player_activity.binding.seekBarPA.progress = 0
            Player_activity.binding.seekBarPA.max = mediaPlayer!!.duration
        } catch (e: Exception) {
            return
        }
    }

    fun seekBarSetup() {
        runnable = Runnable {
            Player_activity.binding.seekBarStartPA.text =
                formatDuration(mediaPlayer!!.currentPosition.toLong())
            Player_activity.binding.seekBarPA.progress = mediaPlayer!!.currentPosition
            Player_activity.binding.seekBarPA.max = mediaPlayer!!.duration
            Handler(Looper.getMainLooper()).postDelayed(runnable, 200)
        }
        Handler(Looper.getMainLooper()).postDelayed(runnable, 0)
    }
}