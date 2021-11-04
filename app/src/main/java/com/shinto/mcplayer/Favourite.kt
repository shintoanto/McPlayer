package com.shinto.mcplayer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.shinto.mcplayer.databinding.ActivityFavouriteBinding
import com.shinto.mcplayer.databinding.ActivityPlayerBinding

class Favourite : AppCompatActivity() {
    private lateinit var binding: ActivityFavouriteBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityFavouriteBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}