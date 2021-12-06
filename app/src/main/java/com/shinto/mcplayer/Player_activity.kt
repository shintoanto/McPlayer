package com.shinto.mcplayer

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.Color
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.shinto.mcplayer.databinding.ActivityPlayerBinding

class Player_activity : AppCompatActivity(), ServiceConnection, MediaPlayer.OnCompletionListener {

    // The use of service connection
    // Interface for monitoring the state of an application service.
    //  See android.app.Service and android.content.Context#bindService for more information.
    //  Like many callbacks from the system, the methods on this class are called from the
    //  main thread of your process.
    // normal object aakuka

//        lateinit var musicListPA: ArrayList<Music>
//        var songPosition: Int = 0
//        var mediaPlayer: MediaPlayer? = null
//        var isPlaying: Boolean = false
//
//        var mainActivity: MainActivity? = null
//        lateinit var binding: ActivityPlayerBinding
//        var repeat: Boolean = false
//
//        var min15: Boolean = false
//        var min30: Boolean = false
//        var min60: Boolean = false
//
//        var nowPlayingId: String = " "

    companion object{
       val MUSIC_NAME = "music name"
    }

    //var songPosition: Int = 0
    //var musicService: MusicService? = null
    var isPlaying: Boolean = false
    lateinit var binding: ActivityPlayerBinding
    var min15: Boolean = false
    var min30: Boolean = false
    var min60: Boolean = false
    lateinit var runnable:Runnable
    var nowPlayingId: String = " "
    private lateinit var musicDao:MusicDao


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // starting service
        initializeLayout()

        binding.timerBtnPA.setOnClickListener {
            val timer = min15 || min30 || min60
            if (!timer) bottomSheetDialogue()
            else {
                val builder = MaterialAlertDialogBuilder(this)
                builder.setTitle("Exit")
                    .setMessage("Do you want to close this app")
                    .setPositiveButton("Yes") { _, _ ->
                        min15 = false
                        min30 = false
                        min60 = false
                    }
                    .setNegativeButton("No") { dialog, _ ->
                        dialog.dismiss()
                    }
                val customDialogue = builder.create()
                customDialogue.show()
                customDialogue.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.RED)
                customDialogue.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.RED)
            }
        }

        binding.sendBtnPA.setOnClickListener {
            val sharIntent = Intent()
            sharIntent.action = Intent.ACTION_SEND
            sharIntent.type = "audio/*"
            Log.d("service___", musicService?.musicListPA?.size.toString())
            sharIntent.putExtra(
                Intent.EXTRA_STREAM,
                Uri.parse(musicService?.musicListPA!![musicService?.songPosition!!].path)
            )
            startActivity(Intent.createChooser(sharIntent, "Music file sending"))
        }

        binding.playPauseButton.setOnClickListener {
            Log.d("play", "playPauseButtonSetONclickListner")
            if (musicService?.mediaPlayer?.isPlaying!!) musicService?.pauseMusic()
            else musicService?.playMusic()
        }

        binding.seekBarPA.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, position: Int, fromUser: Boolean) {
                if (fromUser) musicService!!.mediaPlayer!!.seekTo(position)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) = Unit

            override fun onStopTrackingTouch(p0: SeekBar?) = Unit
        })

        binding.previewsBtnPA.setOnClickListener {
            musicService!!.prevNextBtn(increment = false,callback) }

        binding.nextBtnPA.setOnClickListener {
            Log.d("set","next button")
            musicService!!.prevNextBtn(increment = true,callback) }

        binding.repeatBtnPA.setOnClickListener {

            Log.d("timerbtn","timeerbrtton")
            if (!musicService?.repeat!!) {
                musicService!!.repeat = true
                binding.repeatBtnPA.setColorFilter(ContextCompat.getColor(this, R.color.teal_200))
            } else {
                musicService!!.repeat = false
                binding.repeatBtnPA.setColorFilter(ContextCompat.getColor(this,
                    android.R.color.holo_red_dark
                ))
            }
        }
    }
    val callback:() ->Unit= {
        setLayout()
    }

    fun initializeLayout() {
        musicService?.songPosition = intent.getIntExtra("index", 0)
        setLayout()
        when (intent.getStringExtra("class")) {
            "NowPlaying" -> {
                setLayout()
                binding.seekBarStartPA.text = formatDuration(musicService!!.mediaPlayer!!.currentPosition.toLong())
                binding.seekBarEndPA.text = formatDuration(musicService!!.mediaPlayer!!.duration.toLong())
                binding.seekBarPA.progress = musicService!!.mediaPlayer!!.currentPosition
                binding.seekBarPA.max = musicService!!.mediaPlayer!!.duration
                if (isPlaying) binding.playPauseButton.setIconResource(R.drawable.pause)
                else binding.playPauseButton.setIconResource(R.drawable.play)
            }

            "MusicAdapter" -> {
                val intent = Intent(this, MusicService::class.java)
                this.bindService(intent, this, BIND_AUTO_CREATE)
                this.startService(intent)
                Log.d("play","intent")
               // musicService?.musicListPA = ArrayList()
                musicService?.musicListPA?.addAll(musicService!!.MusicListMA)
//                Log.d("musicLoad",musicService!!.musicListPA[musicService!!.songPosition].title)
             //   setLayout()
            }

//            "MainActivity" -> {
//                musicListPA = ArrayList()
//                musicListPA.addAll(musicService!!.MusicListMA)
//                musicListPA.shuffle()
//                setLayout()
//                createMediaPlayer()
//            }
        }
    }

     fun setLayout() {
        //It shows the information in player activity place
      Glide.with(this).load(musicService?.musicListPA!![musicService?.songPosition!!].artUri)
        .apply(RequestOptions().placeholder(R.drawable.mj).centerCrop()).
            // Image setting
        into(binding.songImgPA)
        // Text setting
        binding.songNamePA.text = musicService?.musicListPA!![musicService?.songPosition!!].title
        if (musicService!!.repeat) binding.repeatBtnPA.setColorFilter(
            ContextCompat.getColor(
                this,
                R.color.teal_200
            )
        )
        if (min15 || min30 || min60) binding.timerBtnPA.setColorFilter(
            ContextCompat.getColor(
                this,
                R.color.purple_500
            )
        )
    }

//    private fun playMusic() {
//        binding.playPauseButton.setIconResource(R.drawable.pause)
//        musicService!!.showNotification(R.drawable.pause)
//        isPlaying = true
//        musicService!!.mediaPlayer!!.start()
//    }
//
//    private fun pauseMusic() {
//        binding.playPauseButton.setIconResource(R.drawable.play)
//        musicService!!.showNotification(R.drawable.play)
//        musicService!!.mediaPlayer!!.pause()
//        isPlaying = false
//    }

//    private fun addToFavourates(){
//      val SongDao = musicDao.addMusic(musicService?.musicListPA!![musicService?.songPosition!!])
//        Log.d("dao",SongDao.toString())
//    }

    private fun createMediaPlayer() {
        try {
            if (musicService!!.mediaPlayer == null) musicService!!.mediaPlayer = MediaPlayer()
            Log.d("onCompletion", "createdMediaPlayer")
            musicService!!.mediaPlayer!!.reset()
            musicService!!.mediaPlayer!!.setDataSource(musicService?.musicListPA?.get(musicService?.songPosition!!)?.path)
            musicService!!.mediaPlayer!!.prepare()
            musicService!!.mediaPlayer!!.start()
            isPlaying = true
            binding.playPauseButton.setIconResource(R.drawable.pause)
            musicService!!.showNotification(R.drawable.pause)
            binding.seekBarStartPA.text = formatDuration(musicService?.mediaPlayer!!.currentPosition.toLong())
            binding.seekBarEndPA.text = formatDuration(musicService?.mediaPlayer!!.duration.toLong())
            binding.seekBarPA.progress = 0
           binding.seekBarPA.max = musicService!!.mediaPlayer!!.duration
//            musicService!!.mediaPlayer!!.setOnCompletionListener {
//                Log.i("Checking","working")
//            }
            musicService!!.mediaPlayer!!.setOnCompletionListener(this)
            nowPlayingId = musicService!!.musicListPA[musicService?.songPosition!!].id.toString()
            setLayout()
        } catch (e: Exception) {
            return
        }
    }

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        val binder = service as MusicService.MyBinder
        musicService = binder.currentService()
        seekBarSetup()
        setLayout()
        createMediaPlayer()
    }

    override fun onServiceDisconnected(p0: ComponentName?) {
        musicService = null
    }


    private fun seekBarSetup() {
      runnable = Runnable {
            binding.seekBarStartPA.text = formatDuration(musicService?.mediaPlayer!!.currentPosition.toLong())
            binding.seekBarEndPA.text = formatDuration(musicService?.mediaPlayer!!.duration.toLong())
            binding.seekBarPA.progress = musicService?.mediaPlayer!!.currentPosition
            binding.seekBarPA.max = musicService?.mediaPlayer!!.duration
            Handler(Looper.getMainLooper()).postDelayed(runnable, 200)
        }
        Handler(Looper.getMainLooper()).postDelayed(runnable, 0)
    }

//    private fun prevNextBtn(increment: Boolean) {
//        if (increment) {
//            musicService!!.setSongPosition(increment = true)
//            // ++songPosition
//            createMediaPlayer()
//            musicService!!.setLayout()
//
//        } else {
//            musicService!!.setSongPosition(increment = false)
//            // --songPosition
//            musicService!!.setLayout()
//            createMediaPlayer()
//        }
//    }

    override fun onCompletion(p0: MediaPlayer?) {
        Log.d("onCompletion", "msgsmsg")
        musicService!!.setSongPosition(increment = true)
        createMediaPlayer()
        callback()
        try {
            setLayout()
        } catch (e: Exception) {
            return
        }
    }

    private fun bottomSheetDialogue() {
        val dialog = BottomSheetDialog(this)
        dialog.setContentView(R.layout.bottom_sheet_layout)
        dialog.show()
        dialog.findViewById<LinearLayout>(R.id.min_15)?.setOnClickListener {
            Toast.makeText(baseContext, "15 minutes timer set", Toast.LENGTH_SHORT).show()
            binding.timerBtnPA.setColorFilter(ContextCompat.getColor(this, R.color.purple_500))
            min15 = true
            Thread {
                Thread.sleep((60000 * 15).toLong())
                if (min15) exitApplication()
            }.start()
            dialog.dismiss()
        }
        dialog.findViewById<LinearLayout>(R.id.min_30)?.setOnClickListener {
            Toast.makeText(baseContext, "30 minutes timer set", Toast.LENGTH_SHORT).show()
            binding.timerBtnPA.setColorFilter(ContextCompat.getColor(this, R.color.purple_500))
            min30 = true
            Thread {
                Thread.sleep((60000 * 30).toLong())
                if (min30) exitApplication()
            }.start()
            dialog.dismiss()
        }
        dialog.findViewById<LinearLayout>(R.id.min_60)?.setOnClickListener {
            Toast.makeText(baseContext, "60 minutes timer set", Toast.LENGTH_SHORT).show()
            binding.timerBtnPA.setColorFilter(ContextCompat.getColor(this, R.color.purple_500))
            min15 = true
            Thread {
                Thread.sleep((6000 * 60).toLong())
                if (min60) exitApplication()
            }.start()
            dialog.dismiss()
        }
    }
}