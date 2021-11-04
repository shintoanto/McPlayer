package com.shinto.mcplayer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.shinto.mcplayer.databinding.ActivityPlayerBinding

class Playlist_screen : AppCompatActivity() {

    private lateinit var binding:ActivityPlayerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}