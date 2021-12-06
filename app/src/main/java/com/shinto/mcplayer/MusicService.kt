package com.shinto.mcplayer

import android.app.PendingIntent
import android.app.Service
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.os.Binder
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.support.v4.media.session.MediaSessionCompat
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.shinto.mcplayer.databinding.ActivityPlayerBinding

class MusicService : Service(),ServiceConnection {

    private var myBinder = MyBinder()
    var repeat: Boolean = false
    var songPosition = -1
    var mediaPlayer: MediaPlayer? = null
    var MusicListMA = arrayListOf<Music>()
    private lateinit var runnable: Runnable
    private lateinit var mediaSession: MediaSessionCompat
    var playerActivity:Player_activity?=null
   // lateinit var binding: ActivityPlayerBinding
    // comes from the player activity
   var musicListPA= arrayListOf<Music>()

    // Media button on API 8+
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

    fun showNotification(playPauseButton:Int) {

        val prevIntent=Intent(baseContext,NotificationReciever::class.java).setAction(ApplicationClass.PREVIOUS)
        val prevPendingIntent = PendingIntent.getBroadcast(baseContext,0,prevIntent,PendingIntent.FLAG_UPDATE_CURRENT)

        val nextIntent=Intent(baseContext,NotificationReciever::class.java).setAction(ApplicationClass.NEXT)
        val nextPendingInt=PendingIntent.getBroadcast(baseContext,0,nextIntent,PendingIntent.FLAG_UPDATE_CURRENT)

        val playIntent=Intent(baseContext,NotificationReciever::class.java).setAction(ApplicationClass.PLAY)
        val playPendingInt=PendingIntent.getBroadcast(baseContext,0,playIntent,PendingIntent.FLAG_UPDATE_CURRENT)

        val exitIntent=Intent(baseContext,NotificationReciever::class.java).setAction(ApplicationClass.EXIT)
        val exitPendingInt=PendingIntent.getBroadcast(baseContext,0,exitIntent,PendingIntent.FLAG_UPDATE_CURRENT)


    if(!musicListPA.isEmpty()){
    var img = BitmapFactory.decodeResource(resources, R.drawable.mj)
    val imgArt = getImgArt(musicListPA[songPosition].path)
     if (imgArt != null) {
         img = BitmapFactory.decodeByteArray(imgArt, 0, imgArt.size)
     }
        val notification = NotificationCompat.Builder(baseContext, ApplicationClass.CHANNEL_ID)
             .setContentTitle(musicListPA[songPosition].title)
             .setContentText(musicListPA[songPosition].artist)
             .setSmallIcon(R.drawable.favourites)
             .setLargeIcon(img)
             .setStyle(
                 androidx.media.app.NotificationCompat.MediaStyle()
                     .setMediaSession(mediaSession.sessionToken)
             )
             .setPriority(NotificationCompat.PRIORITY_HIGH)
             .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
             .setOnlyAlertOnce(true)
             .addAction(R.drawable.previews, "Previous", prevPendingIntent)
             .addAction(playPauseButton, "Play", playPendingInt)
             .addAction(R.drawable.next, "Next", nextPendingInt)
             .addAction(R.drawable.close, "exit", exitPendingInt)
             .build()
         startForeground(13, notification)
}

    }

    fun createMediaPlayer() {
        try {
            if (musicService!!.mediaPlayer == null) musicService!!.mediaPlayer = MediaPlayer()
            musicService!!.mediaPlayer!!.reset()
            musicService!!.mediaPlayer!!.setDataSource(musicListPA[songPosition].path)
            musicService!!.mediaPlayer!!.prepare()
            playerActivity?.binding?.playPauseButton?.setIconResource(R.drawable.pause)
            playerActivity?.binding?.seekBarStartPA?.text = formatDuration(mediaPlayer!!.currentPosition.toLong())
            playerActivity?.binding?.seekBarEndPA?.text = formatDuration(mediaPlayer!!.duration.toLong())
            playerActivity?.binding?.seekBarPA?.progress = 0
            playerActivity?.binding?.seekBarPA?.max = mediaPlayer!!.duration
            playerActivity?.nowPlayingId=musicListPA[songPosition].id
        } catch (e: Exception) {
            return
        }
    }

     fun prevNextBtn(increment: Boolean,callback:()->Unit) {
        if (increment) {
            setSongPosition(increment = true)
            //   ++songPosition
            createMediaPlayer()
            playMusic()
           // playerActivity?.setLayout()
        } else {
            setSongPosition(increment = false)
           //  --songPosition
            createMediaPlayer()
           // playerActivity?.setLayout()
            playMusic()
        }
         callback()
    }

    fun setSongPosition(increment: Boolean) {

            if (increment) {
                if (musicService?.musicListPA!!.size - 1 == songPosition)
                    songPosition = 0
                else
                    ++songPosition
            } else {
                if (0 == songPosition)
                    songPosition = musicService?.musicListPA!!.size - 1
                else
                    --songPosition
            }

    }
    //playmusic:ArrayList<Music>,intex:Int

    fun playMusic() {
        Log.d("play", "playmusic")
        playerActivity?.binding?.playPauseButton?.setIconResource(R.drawable.pause)
        playerActivity?.isPlaying = true
        mediaPlayer!!.start()
        showNotification(R.drawable.pause)
    }

fun pauseMusic() {
    playerActivity?.binding?.playPauseButton?.setIconResource(R.drawable.play)
    musicService!!.showNotification(R.drawable.play)
    musicService!!.mediaPlayer!!.pause()
    playerActivity?.isPlaying = false
}


//    fun pauseMusic() {
//        Log.d("play", "pausemusic")
//        mediaPlayer?.pause()
//        Log.d("play", mediaPlayer.toString())
//        nowPlaying?.binding?.playPauseBtn?.setImageResource(R.drawable.play)
//        showNotification(R.drawable.play)
//        playerActivity?.binding?.nextBtnPA?.setIconResource(R.drawable.play)
//        playerActivity?.isPlaying = false
//    }


    override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
        TODO("Not yet implemented")
    }

    override fun onServiceDisconnected(p0: ComponentName?) {
        TODO("Not yet implemented")
    }
}