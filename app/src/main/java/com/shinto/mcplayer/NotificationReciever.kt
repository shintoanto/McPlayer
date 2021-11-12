package com.shinto.mcplayer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlin.system.exitProcess

class NotificationReciever : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        when (intent?.action) {
            ApplicationClass.PREVIOUS -> preNextSong(increment = false, contex = context!!)
            ApplicationClass.PLAY -> if (Player_activity.isPlaying) puaseMusic() else playMusic()
            ApplicationClass.NEXT -> preNextSong(increment = true, contex = context!!)
            ApplicationClass.EXIT -> {
                Player_activity.musicService!!.stopForeground(true)
                Player_activity.musicService = null
                exitProcess(1)
            }
        }
    }

    private fun playMusic() {
        Player_activity.isPlaying = true
        Player_activity.musicService!!.mediaPlayer!!.start()
        Player_activity.musicService!!.showNotification(R.drawable.pause)
        Player_activity.binding.playPauseButton.setIconResource(R.drawable.pause)
    }

    private fun puaseMusic() {
        Player_activity.isPlaying = false
        Player_activity.musicService!!.mediaPlayer!!.pause()
        Player_activity.musicService!!.showNotification(R.drawable.play)
        Player_activity.binding.playPauseButton.setIconResource(R.drawable.play)
    }

    private fun preNextSong(increment: Boolean, contex: Context) {
        setSongPosition(increment = increment)
//        Player_activity.musicService!!.mediaPlayer!!.setDataSource(Player_activity.musicListPA[Player_activity.songPosition].path)
//        Player_activity.musicService!!.mediaPlayer!!.prepare()
//        Player_activity.binding.playPauseButton.setIconResource(R.drawable.pause)
//        Player_activity.musicService!!.showNotification(R.drawable.pause)

        Player_activity.musicService!!.createMediaPlayer()

        Glide.with(contex).load(Player_activity.musicListPA[Player_activity.songPosition].artUri)
            .apply(RequestOptions().placeholder(R.drawable.mj).centerCrop()).
                // Imag setting
            into(Player_activity.binding.songImgPA)
        // Text setting
        Player_activity.binding.songNamePA.text =
            Player_activity.musicListPA[Player_activity.songPosition].title
        playMusic()
    }
}