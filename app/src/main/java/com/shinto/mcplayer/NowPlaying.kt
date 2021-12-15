package com.shinto.mcplayer

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.Request
import com.bumptech.glide.request.RequestOptions
import com.shinto.mcplayer.databinding.FragmentNowPlayingBinding
import java.lang.NullPointerException

class NowPlaying : Fragment(),MediaPlayer.OnCompletionListener,ServiceConnection {

    lateinit var binding: FragmentNowPlayingBinding
    lateinit var runnable:Runnable

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_now_playing, container, false)
        binding = FragmentNowPlayingBinding.bind(view)

        // The code of text moving
        binding.root.visibility = View.INVISIBLE

        binding.playPauseBtn.setOnClickListener {
            if (musicService?.mediaPlayer!!.isPlaying) {
                musicService?.pauseMusic()
                binding.playPauseBtn.setImageResource(R.drawable.play)
            } else {
                musicService?.playMusic()
                binding.playPauseBtn.setImageResource(R.drawable.pause)
            }
        }
        binding.seekBarNow.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                if (p2) musicService!!.mediaPlayer?.seekTo(p1)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) = Unit

            override fun onStopTrackingTouch(p0: SeekBar?) = Unit

        })

        binding.nextBtnNp.setOnClickListener {
            musicService!!.setSongPosition(increment = true)
            musicService!!.createMediaPlayer()
            Glide.with(this).load(musicService!!.musicListPA[musicService!!.songPosition].artUri)
                .apply(RequestOptions().placeholder(R.drawable.mj).centerCrop()).
                    // Imag setting
                into(binding.songImgNp)
            binding.songNameMp.text = musicService!!.musicListPA[musicService!!.songPosition].title
            // musicService!!.showNotification(R.drawable.pause)
            musicService?.playMusic()
        }

        binding.prevtBtnNp.setOnClickListener {
            musicService!!.setSongPosition(increment = false)
            musicService!!.createMediaPlayer()
            Glide.with(this).load(musicService!!.musicListPA[musicService!!.songPosition].artUri)
                .apply(RequestOptions().placeholder(R.drawable.mj).centerCrop())
                .into(binding.songImgNp)
            binding.songNameMp.text = musicService!!.musicListPA[musicService!!.songPosition].title
            musicService!!.playMusic()
        }

        // The code of song countinusly playing
        binding.root.setOnClickListener {
            val intent = Intent(requireContext(), Player_activity::class.java)
            intent.putExtra("index", musicService!!.songPosition)
            intent.putExtra("class", "NowPlaying")
            ContextCompat.startActivity(requireContext(), intent, null)
        }
        return view
    }
    fun seekBarSetupN() {
        runnable = Runnable {
//                binding.seekBarStartPA.text = formatDuration(musicService?.mediaPlayer!!.currentPosition.toLong())
//                binding.seekBarEndPA.text = formatDuration(musicService?.mediaPlayer!!.duration.toLong())
            binding.seekBarNow.progress = musicService?.mediaPlayer!!.currentPosition
            binding.seekBarNow.max = musicService?.mediaPlayer!!.duration
            Handler(Looper.getMainLooper()).postDelayed(runnable, 200)
        }
        Handler(Looper.getMainLooper()).postDelayed(runnable, 0)
    }

    override fun onResume() {
        super.onResume()
        if (musicService != null) {
            binding.root.visibility = View.VISIBLE
            binding.songNameMp.isSelected = true

            Glide.with(this).load(musicService!!.musicListPA[musicService!!.songPosition].artUri)
                .apply(RequestOptions().placeholder(R.drawable.mj).centerCrop()).
                    // Imag setting
                into(binding.songImgNp)
            binding.songNameMp.text = musicService!!.musicListPA[musicService?.songPosition!!].title
            seekBarSetupN()

//            if (playerActivity?.isPlaying!!)
//               binding.playPauseBtn.setImageResource(R.drawable.pause)
//            else binding.playPauseBtn.setImageResource(R.drawable.play)
        }
    }

    override fun onCompletion(p0: MediaPlayer?) {
        seekBarSetupN()
    }

    override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
        val binder = p1 as MusicService.MyBinder
        musicService = binder.currentService()
        seekBarSetupN()
    }

    override fun onServiceDisconnected(p0: ComponentName?) {
        TODO("Not yet implemented")
    }


//    private fun playMusic() {
//        musicService!!.mediaPlayer!!.start()
//        binding.playPauseBtn.setImageResource(R.drawable.pause)
//        musicService!!.showNotification(R.drawable.pause)
//        playerActivity?.binding?.nextBtnPA?.setIconResource(R.drawable.pause)
//        playerActivity?.isPlaying = true
//    }

//    private fun pauseMusic() {
//        musicService!!.mediaPlayer!!.pause()
//        binding.playPauseBtn.setImageResource(R.drawable.play)
//        musicService!!.showNotification(R.drawable.play)
//        playerActivity?.binding?.nextBtnPA?.setIconResource(R.drawable.play)
//        playerActivity?.isPlaying = false
//    }
}