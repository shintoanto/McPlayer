package com.shinto.mcplayer

import android.media.MediaMetadataRetriever
import android.util.Log
import androidx.core.app.ActivityCompat.finishAffinity
import java.util.concurrent.TimeUnit

data class Music(
    val id: String,
    val title: String,
    val album: String,
    val artist: String,
    val duration: Long = 0,
    val path: String,
    val artUri: String
)

var playerActivity: Player_activity? = null
var musicService: MusicService? = null
var nowPlaying: NowPlaying? = null

fun formatDuration(duration: Long): String {
    val minutes = TimeUnit.MINUTES.convert(duration, TimeUnit.MILLISECONDS)
    val seconds = (TimeUnit.SECONDS.convert(
        duration,
        TimeUnit.MILLISECONDS
    )) - minutes * TimeUnit.SECONDS.convert(1, TimeUnit.MINUTES)
    return String.format("%02d:%02d", minutes, seconds)
}

//fun setSongPosition(increment: Boolean) {
//    if (playerActivity.repeat) {
//        if (increment) {
//            if (musicService?.musicListPA!!.size - 1 == playerActivity.songPosition)
//                playerActivity.songPosition = 0
//            else
//                ++playerActivity.songPosition
//        } else {
//            if (0 == playerActivity.songPosition)
//                playerActivity.songPosition = musicService?.musicListPA!!.size - 1
//            else
//                --playerActivity.songPosition
//        }
//    }
//}

fun exitApplication() {
    musicService?.stopForeground(true)
    musicService?.mediaPlayer!!.release()
    //  musicService = null
    finishAffinity(playerActivity!!)
    System.exit(0)
}

fun getImgArt(path: String): ByteArray? {
    val retriever = MediaMetadataRetriever()
    Log.d("msg", "retriever")
    retriever.setDataSource(path)
    return retriever.embeddedPicture
}

//fun pauseMusic() {
//    playerActivity?.binding?.playPauseButton?.setIconResource(R.drawable.play)
//    musicService!!.showNotification(R.drawable.play)
//    musicService!!.mediaPlayer!!.pause()
//    playerActivity?.isPlaying = false
//}
//fun playMusic() {
//    Log.d("play", "playmusic")
//    musicService?.mediaPlayer?.start()
//    nowPlaying?.binding?.playPauseBtn?.setImageResource(R.drawable.pause)
//    musicService?.showNotification(R.drawable.pause)
//


//    playerActivity.binding.nextBtnPA.setIconResource(R.drawable.pause)
//    playerActivity.isPlaying = true
//}

//fun pauseMusic() {
//    Log.d("play", "pausemusic")
//    musicService?.mediaPlayer!!.pause()
//    nowPlaying?.binding?.playPauseBtn?.setImageResource(R.drawable.play)
//    musicService?.showNotification(R.drawable.play)
//    playerActivity.binding.nextBtnPA.setIconResource(R.drawable.play)
//    playerActivity.isPlaying = false
//}
