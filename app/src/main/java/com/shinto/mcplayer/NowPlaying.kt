package com.shinto.mcplayer

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.shinto.mcplayer.databinding.FragmentNowPlayingBinding
import java.lang.NullPointerException

class NowPlaying : Fragment() {

        lateinit var binding: FragmentNowPlayingBinding
         private var playerActivity:Player_activity?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_now_playing, container, false)
        binding = FragmentNowPlayingBinding.bind(view)

        // The code of text moving
        binding.root.visibility = View.INVISIBLE

        binding.playPauseBtn.setOnClickListener {
            Log.d("nowPlay","now playing action")
            try{
                if (playerActivity!!.isPlaying) {
                    Log.d("nowPlay","true")
                    musicService?.pauseMusic()
                } else {
                    Log.d("nowPlay","false")
                    musicService?.playMusic()
                }
            }catch (ignore:NullPointerException){
                Log.d("catch",ignore.toString())
            }
        }

        binding.nextBtnNp.setOnClickListener {
            musicService!!.setSongPosition(increment = true)
            musicService!!.createMediaPlayer()
            Glide.with(this).load(musicService!!.musicListPA[musicService!!.songPosition].artUri)
                .apply(RequestOptions().placeholder(R.drawable.mj).centerCrop()).
                    // Imag setting
                into(binding.songImgNp)

            binding.songNameMp.text = musicService!!.musicListPA[musicService!!.songPosition].title
            musicService!!.showNotification(R.drawable.pause)
            musicService?.playMusic()
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

//            if (playerActivity?.isPlaying!!)
//               binding.playPauseBtn.setImageResource(R.drawable.pause)
//            else binding.playPauseBtn.setImageResource(R.drawable.play)
        }
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