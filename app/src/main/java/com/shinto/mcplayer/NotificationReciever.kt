package com.shinto.mcplayer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class NotificationReciever : BroadcastReceiver() {

    var playerActivity: Player_activity? = null

    override fun onReceive(context: Context?, intent: Intent?) {
        when (intent?.action) {
            ApplicationClass.PREVIOUS -> preNextSong(increment = false, contex = context!!)
            ApplicationClass.PLAY -> if (playerActivity?.isPlaying!!) puaseMusic() else playMusic()
            ApplicationClass.NEXT -> preNextSong(increment = true, contex = context!!)
            ApplicationClass.EXIT -> {
                exitApplication()
            }
        }
    }

    private fun playMusic() {
        playerActivity?.isPlaying = true
        musicService!!.mediaPlayer!!.start()
        musicService!!.showNotification(R.drawable.pause)
        playerActivity?.binding?.playPauseButton?.setIconResource(R.drawable.pause)
        nowPlaying?.binding?.playPauseBtn?.setImageResource(R.drawable.pause)
    }

    private fun puaseMusic() {
        playerActivity?.isPlaying = false
        musicService!!.mediaPlayer!!.pause()
        musicService!!.showNotification(R.drawable.play)
        playerActivity?.binding?.playPauseButton?.setIconResource(R.drawable.play)
        nowPlaying?.binding?.playPauseBtn?.setImageResource(R.drawable.play)

    }

    private fun preNextSong(increment: Boolean, contex: Context) {
        musicService!!.setSongPosition(increment = increment)
        musicService!!.createMediaPlayer()

//        Glide.with(contex).load(musicService!!.musicListPA[musicService?.songPosition!!].artUri)
//            .apply(RequestOptions().placeholder(R.drawable.mj).centerCrop()).
//                // Imag setting
//            into(playerActivity?.binding!!.songImgPA)
        // Text setting
//        playerActivity?.binding?.songNamePA?.text =
//            musicService!!.musicListPA[musicService?.songPosition!!].title
//        Glide.with(contex).load(musicService!!.musicListPA[musicService?.songPosition!!].artUri)
//            .apply(RequestOptions().placeholder(R.drawable.mj).centerCrop()).
//                // Imag setting
//            into(nowPlaying?.binding!!.songImgNp)
//        nowPlaying?.binding?.songNameMp?.text =
//            musicService!!.musicListPA[musicService!!.songPosition].title
        playMusic()
        musicService!!.prevNextBtn(increment = true,{})
    }
}