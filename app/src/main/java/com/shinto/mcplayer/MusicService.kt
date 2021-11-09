package com.shinto.mcplayer

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.Handler
import android.os.IBinder
import android.os.Looper

class MusicService : Service() {

    private var myBinder = MyBinder()
    var mediaPlayer: MediaPlayer? = null
    private lateinit var runnable: Runnable

    override fun onBind(p0: Intent?): IBinder {
        return myBinder
    }

    inner class MyBinder : Binder() {
        fun currentService(): MusicService {
            return this@MusicService
        }
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
            if (Player_activity.musicService!!.mediaPlayer == null) Player_activity.musicService!!.mediaPlayer = MediaPlayer()
            Player_activity.musicService!!.mediaPlayer!!.reset()
            Player_activity.musicService!!.mediaPlayer!!.setDataSource(Player_activity.musicListPA[Player_activity.songPosition].path)
            Player_activity.musicService!!.mediaPlayer!!.prepare()

            Player_activity.binding.playPauseButton.setIconResource(R.drawable.pause)
            Player_activity.binding.seekBarStartPA.text = formatDuration(mediaPlayer!!.currentPosition.toLong())
            Player_activity.binding.seekBarEndPA.text = formatDuration(mediaPlayer!!.duration.toLong())
            Player_activity.binding.seekBarPA.progress = 0
            Player_activity.binding.seekBarPA.max =  mediaPlayer!!.duration
        } catch (e: Exception) {
            return
        }
    }

    fun seekBarSetup() {
        runnable = Runnable {
           Player_activity.binding.seekBarStartPA.text = formatDuration(mediaPlayer!!.currentPosition.toLong())
            Player_activity.binding.seekBarPA.progress = mediaPlayer!!.currentPosition
            Player_activity.binding.seekBarPA.max=mediaPlayer!!.duration
            Handler(Looper.getMainLooper()).postDelayed(runnable, 200)
        }
        Handler(Looper.getMainLooper()).postDelayed(runnable, 0)
    }
}