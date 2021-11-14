package com.shinto.mcplayer

import android.media.MediaMetadataRetriever
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

fun formatDuration(duration: Long): String {
    val minutes = TimeUnit.MINUTES.convert(duration, TimeUnit.MILLISECONDS)
    val seconds = (TimeUnit.SECONDS.convert(
        duration,
        TimeUnit.MILLISECONDS
    )) - minutes * TimeUnit.SECONDS.convert(1, TimeUnit.MINUTES)
    return String.format("%02d:%02d", minutes, seconds)
}

fun setSongPosition(increment: Boolean) {
   if (!Player_activity.repeat){
       if (increment) {
           if (Player_activity.musicListPA.size - 1 == Player_activity.songPosition)
               Player_activity.songPosition = 0
           else
               ++Player_activity.songPosition
       } else {
           if (0 == Player_activity.songPosition)
               Player_activity.songPosition = Player_activity.musicListPA.size - 1
           else
               --Player_activity.songPosition
       }
   }
}

fun getImgArt(path: String): ByteArray? {
    val retriever = MediaMetadataRetriever()
    retriever.setDataSource(path)
    return retriever.embeddedPicture
}
