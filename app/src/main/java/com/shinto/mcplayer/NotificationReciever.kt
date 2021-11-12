package com.shinto.mcplayer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import kotlin.system.exitProcess

class NotificationReciever:BroadcastReceiver() {
    override fun onReceive(p0: Context?, intent: Intent?) {
       when(intent?.action){
           ApplicationClass.PREVIOUS ->if (Player_activity.isPlaying)  puaseMusic() else playMusic()
               ApplicationClass.PLAY -> if (Player_activity.isPlaying)  puaseMusic() else playMusic()
           ApplicationClass.PREVIOUS ->if (Player_activity.isPlaying)  puaseMusic() else playMusic()
           ApplicationClass.EXIT -> {
               Player_activity.musicService!!.stopForeground(true)
           Player_activity.musicService=null
           exitProcess(1)
           }
       }
    }

    private fun  playMusic(){
        Player_activity.isPlaying=true
        Player_activity.musicService!!.mediaPlayer!!.start()
        Player_activity.musicService!!.showNotification(R.drawable.pause)
        Player_activity.binding.playPauseButton.setIconResource(R.drawable.pause)
    }
    private fun  puaseMusic(){
        Player_activity.isPlaying=false
        Player_activity.musicService!!.mediaPlayer!!.pause()
        Player_activity.musicService!!.showNotification(R.drawable.play)
        Player_activity.binding.playPauseButton.setIconResource(R.drawable.play)
    }
}