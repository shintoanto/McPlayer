package com.shinto.mcplayer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.shinto.mcplayer.databinding.ActivityFavouriteBinding
import com.shinto.mcplayer.databinding.ActivityPlayerBinding

class Favourite : AppCompatActivity() {
    private lateinit var adapter: FavouriteAdapter
    private lateinit var binding: ActivityFavouriteBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavouriteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val tempList = ArrayList<String>()
        tempList.add("song 1")
        binding.favouritesRV.setHasFixedSize(true)
        binding.favouritesRV.setItemViewCacheSize(13)
        binding.favouritesRV.layoutManager = GridLayoutManager(this,3)
        adapter = FavouriteAdapter(this@Favourite, tempList)
        binding.favouritesRV.adapter = adapter
    }
}