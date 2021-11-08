package com.shinto.mcplayer

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.widget.SeekBar
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.shinto.mcplayer.databinding.ActivityPlayerBinding
import java.util.Collections.addAll

class Player_activity : AppCompatActivity(), ServiceConnection {

    companion object {
        lateinit var musicListPA: ArrayList<Music>
        var songPosition: Int = 0
        //        var mediaPlayer: MediaPlayer? = null
        var isPlaying: Boolean = false
        var musicService: MusicService? = null
    }

    private lateinit var binding: ActivityPlayerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val intent = Intent(this, MusicService::class.java)
        bindService(intent, this, BIND_AUTO_CREATE)
        startService(intent)
        initializeLayout()
        binding.playPauseButton.setOnClickListener {
            if (isPlaying) pauseMusic()
            else playMusic()
        }
        //   binding.seekBarPA.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
//            override fun onProgressChanged(p0: SeekBar?, progress: Int, fromUser: Boolean) {
//                if (fromUser)
//            }
//
//            override fun onStartTrackingTouch(p0: SeekBar?) = Unit
//
//            override fun onStopTrackingTouch(p0: SeekBar?) = Unit
//        })
    }

    private fun setLayout() {
        Glide.with(this).load(musicListPA[songPosition].artUri)
            .apply(RequestOptions().placeholder(R.drawable.mj).centerCrop()).
                // Imag setting
            into(binding.songImgPA)
        // Text setting
        binding.songNamePA.text = musicListPA[songPosition].title
    }

    private fun createMediaPlayer() {
        try {
            if (musicService!!.mediaPlayer == null) musicService!!.mediaPlayer = MediaPlayer()
            musicService!!.mediaPlayer!!.reset()
            musicService!!.mediaPlayer!!.setDataSource(musicListPA[songPosition].path)
            musicService!!.mediaPlayer!!.prepare()
            musicService!!.mediaPlayer!!.start()
            binding.playPauseButton.setIconResource(R.drawable.pause)
        } catch (e: Exception) {
            return
        }
    }

    private fun initializeLayout() {
        songPosition = intent.getIntExtra("index", 0)
        when (intent.getStringExtra("class")) {
            "MusicAdapter" -> {
                musicListPA = ArrayList()
                musicListPA.addAll(MainActivity.MusicListMA)
                setLayout()
            }
        }
    }

    private fun playMusic() {
        binding.playPauseButton.setIconResource(R.drawable.pause)
        isPlaying = true
        musicService!!.mediaPlayer!!.start()
    }

    private fun pauseMusic() {
        binding.playPauseButton.setIconResource(R.drawable.play)
        isPlaying = false
        musicService!!.mediaPlayer!!.pause()
    }

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        val binder = service as MusicService.MyBinder
        musicService = binder.currentService()
        createMediaPlayer()
    }

    override fun onServiceDisconnected(p0: ComponentName?) {
        musicService = null
    }
}