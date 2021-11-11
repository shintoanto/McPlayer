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

class Player_activity : AppCompatActivity(), ServiceConnection, MediaPlayer.OnCompletionListener {
// normal object aakuka
    companion object {
        lateinit var musicListPA: ArrayList<Music>
        var songPosition: Int = 0

        var mediaPlayer: MediaPlayer? = null
        var isPlaying: Boolean = false
        var musicService: MusicService? = null
        lateinit var binding: ActivityPlayerBinding
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // starting service
        val intent = Intent(this, MusicService::class.java)
        bindService(intent, this, BIND_AUTO_CREATE)
        startService(intent)
        initializeLayout()
        binding.playPauseButton.setOnClickListener {
            if (isPlaying) pauseMusic()
            else playMusic()
        }
        binding.seekBarPA.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, position: Int, fromUser: Boolean) {
                if (fromUser) musicService!!.mediaPlayer!!.seekTo(position)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) = Unit

            override fun onStopTrackingTouch(p0: SeekBar?) = Unit
        })

//        binding.previewsBtnPA.setOnClickListener { prevNextBtn(increment = false) }
//        binding.nextBtnPA.setOnClickListener { prevNextBtn(increment = true) }
    }

    private fun setLayout() {
        Glide.with(this).load(musicListPA[songPosition].artUri)
            .apply(RequestOptions().placeholder(R.drawable.mj).centerCrop()).
                // Imag setting
            into(binding.songImgPA)
        // Text setting
        binding.songNamePA.text = musicListPA[songPosition].title
    }


     fun initializeLayout() {
        songPosition = intent.getIntExtra("index", 0)
        when (intent.getStringExtra("class")) {
            "MusicAdapter" -> {
                musicListPA = ArrayList()
                musicListPA.addAll(MainActivity.MusicListMA)
                setLayout()
            }
            "MainActivity"->{
                musicListPA= ArrayList()
                musicListPA.addAll(MainActivity.MusicListMA)
                musicListPA.shuffle()
                setLayout()
                createMediaPlayer()
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
        musicService!!.mediaPlayer!!.pause()
        isPlaying = false
    }

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        val binder = service as MusicService.MyBinder
        musicService = binder.currentService()
        createMediaPlayer()
        musicService!!.seekBarSetup()
        musicService!!.showNotification()
    }

    override fun onServiceDisconnected(p0: ComponentName?) {
        musicService = null
    }

    private fun createMediaPlayer() {
        try {
            if (musicService!!.mediaPlayer == null) musicService!!.mediaPlayer = MediaPlayer()
            musicService!!.mediaPlayer!!.reset()
            musicService!!.mediaPlayer!!.setDataSource(musicListPA[songPosition].path)
            musicService!!.mediaPlayer!!.prepare()
            musicService!!.mediaPlayer!!.start()
            isPlaying=true
            binding.playPauseButton.setIconResource(R.drawable.pause)
            binding.seekBarStartPA.text = formatDuration(mediaPlayer!!.currentPosition.toLong())
            binding.seekBarEndPA.text = formatDuration(mediaPlayer!!.duration.toLong())
            binding.seekBarPA.progress = 0
            binding.seekBarPA.max = musicService!!.mediaPlayer!!.duration
            musicService!!.mediaPlayer!!.setOnCompletionListener  (this)
        } catch (e: Exception) {
            return
        }
    }

    private fun prevNextBtn(increment: Boolean) {
        if (increment) {
            setSongPosition(increment = true)
            ++songPosition
            setLayout()
            createMediaPlayer()
        } else {
            setSongPosition(increment = false)
            --songPosition
            setLayout()
            createMediaPlayer()
        }

    }

    private fun setSongPosition(increment: Boolean) {
        if (increment) {
            if (musicListPA.size - 1 == songPosition)
                songPosition = 0
            else
                ++songPosition
        } else {
            if (0 == songPosition)
                songPosition = musicListPA.size - 1
            else
                --songPosition
        }
    }

    override fun onCompletion(p0: MediaPlayer?) {
        setSongPosition(increment = true)
        createMediaPlayer()
    }

}