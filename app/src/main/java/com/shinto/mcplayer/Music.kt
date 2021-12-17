package com.shinto.mcplayer

import android.media.MediaMetadataRetriever
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import androidx.core.app.ActivityCompat.finishAffinity
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.shinto.mcplayer.databinding.FragmentNowPlayingBinding
import kotlinx.parcelize.Parcelize
import java.util.concurrent.TimeUnit

@Parcelize
@Entity(tableName = "Musictable")
data class Music(
    @PrimaryKey var timestamp: String,
    val id: Int,
    val title: String,
    var playListName:String,
    val album: String,
    val artist: String,
    val duration: Long = 0,
    val path: String,
    val artUri: String
) : Parcelable
//    : Parcelable {
//    constructor(parcel: Parcel) : this(
//        parcel.readInt(),
//        parcel.readString(),
//        parcel.readString(),
//        parcel.readString(),
//        parcel.readString(),
//        parcel.readLong(),
//        parcel.readString(),
//        parcel.readString()
//    ) {
//    }

//    override fun writeToParcel(parcel: Parcel, flags: Int) {
//        parcel.writeInt(id!!)
//        parcel.writeString(title)
//        parcel.writeString(album)
//        parcel.writeString(artist)
//        parcel.writeLong(duration)
//        parcel.writeString(path)
//        parcel.writeString(artUri)
//    }

//    override fun describeContents(): Int = 0
//
//    companion object CREATOR : Parcelable.Creator<Music> {
//        override fun createFromParcel(parcel: Parcel): Music = Music(parcel)
//
//        override fun newArray(size: Int): Array<Music?> = arrayOfNulls(size)
//
//    }
//}
//    :Parcelable

var playerActivity: Player_activity? = null
var musicService: MusicService? = null



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
    musicService!!.audioManager.abandonAudioFocus(musicService)
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
