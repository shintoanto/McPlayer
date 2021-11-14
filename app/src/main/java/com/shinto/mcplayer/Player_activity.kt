package com.shinto.mcplayer

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.shinto.mcplayer.databinding.ActivityPlayerBinding

class Player_activity : AppCompatActivity(), ServiceConnection, MediaPlayer.OnCompletionListener {

    // The use of service connection
   // Interface for monitoring the state of an application service.
  //  See android.app.Service and android.content.Context#bindService for more information.

  //  Like many callbacks from the system, the methods on this class are called from the
    //  main thread of your process.

    // normal object aakuka
    companion object {
        lateinit var musicListPA: ArrayList<Music>
        var songPosition: Int = 0
        var mediaPlayer: MediaPlayer? = null
        var isPlaying: Boolean = false
        var musicService: MusicService? = null
        lateinit var binding: ActivityPlayerBinding
        var repeat: Boolean = false

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

        binding.timerBtnPA.setOnClickListener {
            bottomSheetDialogue()
        }

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

        binding.previewsBtnPA.setOnClickListener { prevNextBtn(increment = false) }
        binding.nextBtnPA.setOnClickListener { prevNextBtn(increment = true) }
        binding.repeatBtnPA.setOnClickListener {
         //   repeat = !repeat
            if (!repeat) {
                repeat = true
                binding.repeatBtnPA.setColorFilter(ContextCompat.getColor(this,R.color.teal_200))
            } else {
                repeat=false
            }
        }
    }

    private fun setLayout() {
        //It shows the information in player activity place
        Glide.with(this).load(musicListPA[songPosition].artUri)
            .apply(RequestOptions().placeholder(R.drawable.mj).centerCrop()).
                // Imag setting
            into(binding.songImgPA)
        // Text setting
        binding.songNamePA.text = musicListPA[songPosition].title
        if (repeat) binding.repeatBtnPA.setColorFilter(ContextCompat.getColor(this,R.color.teal_200))
    }

    fun initializeLayout() {
        songPosition = intent.getIntExtra("index", 0)
        when (intent.getStringExtra("class")) {
            "MusicAdapter" -> {
                musicListPA = ArrayList()
                musicListPA.addAll(MainActivity.MusicListMA)
                setLayout()
            }
            "MainActivity" -> {
                musicListPA = ArrayList()
                musicListPA.addAll(MainActivity.MusicListMA)
                musicListPA.shuffle()
                setLayout()
                createMediaPlayer()
            }
        }
    }

    private fun playMusic() {
        binding.playPauseButton.setIconResource(R.drawable.pause)
        musicService!!.showNotification(R.drawable.pause)
        isPlaying = true
        musicService!!.mediaPlayer!!.start()
    }

    override fun onServiceDisconnected(p0: ComponentName?) {
        musicService = null
    }

    private fun createMediaPlayer() {
        try {
            if (musicService!!.mediaPlayer == null) musicService!!.mediaPlayer = MediaPlayer()
            Log.d("onCompletion","createdMediaPlayer")

            musicService!!.mediaPlayer!!.reset()
            musicService!!.mediaPlayer!!.setDataSource(musicListPA[songPosition].path)
            musicService!!.mediaPlayer!!.prepare()
            musicService!!.mediaPlayer!!.start()
            isPlaying = true
            binding.playPauseButton.setIconResource(R.drawable.pause)
            musicService!!.showNotification(R.drawable.pause)
            binding.seekBarStartPA.text = formatDuration(mediaPlayer!!.currentPosition.toLong())
            binding.seekBarEndPA.text = formatDuration(mediaPlayer!!.duration.toLong())
            binding.seekBarPA.progress = 0
            binding.seekBarPA.max = musicService!!.mediaPlayer!!.duration
//            musicService!!.mediaPlayer!!.setOnCompletionListener {
//                Log.i("Checking","working")
//            }
            musicService!!.mediaPlayer!!.setOnCompletionListener(this)
            Log.d("onCompletion","setONcompletionlistenr")

        } catch (e: Exception) {
            return
        }
    }

    private fun pauseMusic() {
        binding.playPauseButton.setIconResource(R.drawable.play)
        musicService!!.showNotification(R.drawable.play)
        musicService!!.mediaPlayer!!.pause()
        isPlaying = false
    }

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        val binder = service as MusicService.MyBinder
        musicService = binder.currentService()
        createMediaPlayer()
        musicService!!.seekBarSetup()

    }

    private fun prevNextBtn(increment: Boolean) {
        if (increment) {
            setSongPosition(increment = true)
            // ++songPosition
            setLayout()
            createMediaPlayer()
        } else {
            setSongPosition(increment = false)
            // --songPosition
            setLayout()
            createMediaPlayer()
        }
    }

    override fun onCompletion(p0: MediaPlayer?) {
        Log.d("onCompletion","msgsmsg")
        setSongPosition(increment = true)
        createMediaPlayer()
        try {
            setLayout()
        }catch (e:Exception){
            return
        }
    }

    private fun bottomSheetDialogue(){
        val dialog= BottomSheetDialog(this)
        dialog.setContentView(R.layout.bottom_sheet_layout)
        dialog.show()
        dialog.findViewById<LinearLayout>(R.id.min_15)?.setOnClickListener {
            Toast.makeText(baseContext,"15 minutes timer set",Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }
        dialog.findViewById<LinearLayout>(R.id.min_30)?.setOnClickListener {
            Toast.makeText(baseContext,"30 minutes timer set",Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }
        dialog.findViewById<LinearLayout>(R.id.min_60)?.setOnClickListener {
            Toast.makeText(baseContext,"60 minutes timer set",Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }
    }


}