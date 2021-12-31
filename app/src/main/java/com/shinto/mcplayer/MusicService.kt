package com.shinto.mcplayer

import android.app.PendingIntent
import android.app.Service
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.BitmapFactory
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Binder
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.provider.Settings
import android.support.v4.media.session.MediaSessionCompat
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.shinto.mcplayer.databinding.ActivityPlayerBinding
import com.shinto.mcplayer.databinding.FragmentNowPlayingBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MusicService : Service(),ServiceConnection,AudioManager.OnAudioFocusChangeListener {
    var nowPlaying: NowPlaying? = null
    lateinit var audioManager: AudioManager
    private var myBinder = MyBinder()
    var repeat: Boolean = false
    var songPosition = -1
    var mediaPlayer: MediaPlayer? = null
    var MusicListMA = arrayListOf<Music>()
    private lateinit var runnable: Runnable
    private lateinit var mediaSession: MediaSessionCompat
    lateinit var favPlaylist : List<Music>
    var favMusic:List<Music> = mutableListOf()
    var playerActivity:Player_activity?=null
    var binding:FragmentNowPlayingBinding?=null
   var musicListPA= mutableListOf<Music>()
    var playlist=mutableListOf<String>()
     lateinit var playlistMusic: List<Music>
     lateinit var songsInsidePlaylist:List<Music>

    // Media button on API 8+
    override fun onBind(p0: Intent?): IBinder {
        mediaSession = MediaSessionCompat(baseContext, "My Music")
        Log.d("v","onBind")
        return myBinder
        }

    // Pass the context
    inner class MyBinder : Binder() {
        fun currentService(): MusicService {
            Log.d("v","innerClass")
            return this@MusicService
        }
    }

    fun showNotification(playPauseButton:Int) {
        Log.d("v","showNotification")

        val prevIntent=Intent(baseContext,NotificationReciever::class.java).setAction(ApplicationClass.PREVIOUS)
        val prevPendingIntent = PendingIntent.getBroadcast(baseContext,0,prevIntent,PendingIntent.FLAG_UPDATE_CURRENT)

        val nextIntent=Intent(baseContext,NotificationReciever::class.java).setAction(ApplicationClass.NEXT)
        val nextPendingInt=PendingIntent.getBroadcast(baseContext,0,nextIntent,PendingIntent.FLAG_UPDATE_CURRENT)

        val playIntent=Intent(baseContext,NotificationReciever::class.java).setAction(ApplicationClass.PLAY)
        val playPendingInt=PendingIntent.getBroadcast(baseContext,0,playIntent,PendingIntent.FLAG_UPDATE_CURRENT)

        val exitIntent=Intent(baseContext,NotificationReciever::class.java).setAction(ApplicationClass.EXIT)
        val exitPendingInt=PendingIntent.getBroadcast(baseContext,0,exitIntent,PendingIntent.FLAG_UPDATE_CURRENT)


    if(!musicListPA.isEmpty()){
    var img = BitmapFactory.decodeResource(resources, R.drawable.musicanote)
    val imgArt = musicListPA[songPosition].path.let { getImgArt(it) }
     if (imgArt != null) {
         img = BitmapFactory.decodeByteArray(imgArt, 0, imgArt.size)
     }
        val notification = NotificationCompat.Builder(baseContext, ApplicationClass.CHANNEL_ID)
             .setContentTitle(musicListPA[songPosition].title)
             .setContentText(musicListPA[songPosition].artist)
             .setSmallIcon(R.drawable.ic_baseline_disc_full_24)
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

    fun readPlayListNameFromDB(){
        Log.d("v","readPlaylistNameFromDB")
        val SongDao = MusicDatabase.getDatabase(application).songDao()
        GlobalScope.launch(Dispatchers.IO) {
            playlist = SongDao.readDistinctNames() as MutableList<String>
            if(playlist.contains("favourites")){
                playlist.remove("favourites")
            }
        }
    }

    fun readFavSongs(favourite:String){
        Log.d("v","readFavSongs")
        GlobalScope.launch(Dispatchers.IO) {
            val SongDao = MusicDatabase.getDatabase(application).songDao()
            favPlaylist = SongDao.readAllData(favourite)
            favMusic = favPlaylist
        }
    }

    fun readPlaylistSongs(name:String){
        Log.d("v","readPlaylistSongs")
        GlobalScope.launch(Dispatchers.IO) {
            val SongDao = MusicDatabase.getDatabase(application).songDao()
            playlistMusic = SongDao.readAllSongsFromPlaylist(name)
            withContext(Dispatchers.Main){
                songsInsidePlaylist = emptyList()
                songsInsidePlaylist = playlistMusic
            }
        }
    }

    fun deleteAllSongsInPlaylist(adapterPosition:Int){
        Log.d("v","deleteAllSongsInPlaylist")
        val songDao = MusicDatabase.getDatabase(application).songDao()
        songDao.deleteAllSongs(playlistM[adapterPosition])
        playlistM.removeAt(adapterPosition)
    }

    fun createMediaPlayer() {
        try {
            Log.d("v","createMediaPlayaer")
            if (musicService!!.mediaPlayer == null) musicService!!.mediaPlayer = MediaPlayer()
            musicService!!.mediaPlayer!!.reset()
            musicService!!.mediaPlayer!!.setDataSource(musicListPA[songPosition].path)
            musicService!!.mediaPlayer!!.prepare()
            playerActivity?.binding?.playPauseButton?.setIconResource(R.drawable.pause)
            playerActivity?.binding?.seekBarStartPA?.text = formatDuration(mediaPlayer!!.currentPosition.toLong())
            playerActivity?.binding?.seekBarEndPA?.text = formatDuration(mediaPlayer!!.duration.toLong())
            playerActivity?.binding?.seekBarPA?.progress = 0
            playerActivity?.binding?.seekBarPA?.max = mediaPlayer!!.duration
            playerActivity?.nowPlayingId=musicListPA[songPosition].id.toString()
        } catch (e: Exception) {
            return
        }
    }

     fun prevNextBtn(increment: Boolean,callback:()->Unit) {
         Log.d("v","prevNextBtn")
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
        Log.d("v","setSongPosition")
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
        Log.d("v","playMusic")
        playerActivity?.binding?.playPauseButton?.setIconResource(R.drawable.pause)
        playerActivity?.isPlaying = true
        mediaPlayer!!.start()
        showNotification(R.drawable.pause)
    }

fun pauseMusic() {
    Log.d("v","pauseMusic")
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

    override fun onAudioFocusChange(p0: Int) {
        Log.d("v","onAudioFocusChange")
        if (p0 <= 0){
            playerActivity?.binding?.playPauseButton?.setIconResource(R.drawable.play)
//            nowPlaying!!.binding.playPauseBtn.setImageResource(R.drawable.play)
            musicService!!.showNotification(R.drawable.play)
            musicService!!.mediaPlayer!!.pause()
            playerActivity?.isPlaying = false
        }else{
            playerActivity?.binding?.playPauseButton?.setIconResource(R.drawable.pause)
//            nowPlaying!!.binding.playPauseBtn.setImageResource(R.drawable.pause)
            playerActivity?.isPlaying = true
            mediaPlayer!!.start()
            showNotification(R.drawable.pause)
        }
    }
}