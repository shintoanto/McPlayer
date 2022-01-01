package com.shinto.mcplayer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.coroutines.withContext

class NotificationReciever : BroadcastReceiver() {

    var playerActivity: Player_activity? = null
    var nowPlaying: NowPlaying? = null

    override fun onReceive(context: Context?, intent: Intent?) {
        when (intent?.action) {
            ApplicationClass.PREVIOUS -> preNextSong(increment = false, contex = context!!)
            ApplicationClass.PLAY -> if (musicService?.mediaPlayer?.isPlaying!!) puaseMusic() else playMusic()
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
        playerActivity?.binding?.playPauseButton?.setImageResource(R.drawable.pause)
        nowPlaying?.binding?.playPauseBtn?.setImageResource(R.drawable.pause)
    }

    private fun puaseMusic() {
        playerActivity?.isPlaying = false
        musicService!!.mediaPlayer!!.pause()
        musicService!!.showNotification(R.drawable.play)
        playerActivity?.binding?.playPauseButton?.setImageResource(R.drawable.play)
        nowPlaying?.binding?.playPauseBtn?.setImageResource(R.drawable.play)
    }
//  private  fun setLayout() {
//      //It shows the information in player activity place
//      Glide.with(view).load(musicService?.musicListPA!![musicService?.songPosition!!].artUri)
//          .apply(RequestOptions().placeholder(R.drawable.mj).centerCrop()).
//              // Image setting
//          into(playerActivity?.binding!!.songImgPA)
//      // Text setting
//      playerActivity?.binding!!.songNamePA.text = musicService?.musicListPA!![musicService?.songPosition!!].title
//  }
    private fun preNextSong(increment: Boolean, contex: Context) {
        musicService!!.setSongPosition(increment = increment)
        musicService!!.createMediaPlayer()
       // setLayout()
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
        musicService!!.prevNextBtn(increment = increment,{})
    }
}