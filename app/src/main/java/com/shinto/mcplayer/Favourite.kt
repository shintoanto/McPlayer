package com.shinto.mcplayer

import android.content.ComponentName
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.shinto.mcplayer.databinding.ActivityFavouriteBinding

class Favourite : AppCompatActivity(),ServiceConnection {

    private lateinit var adapter: FavouriteAdapter
    private lateinit var binding: ActivityFavouriteBinding
    private lateinit var tempList: ArrayList<Music>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavouriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        tempList = arrayListOf()
        if(tempList != null){
            tempList = musicService!!.favMusic as ArrayList<Music>
        }

      //  tempList.add("song 1")
        binding.favouritesRV.setHasFixedSize(true)
        binding.favouritesRV.setItemViewCacheSize(13)
        binding.favouritesRV.layoutManager = LinearLayoutManager(applicationContext)
        adapter = FavouriteAdapter(this@Favourite, tempList)
        binding.favouritesRV.adapter = adapter
    }

    private fun readAllSongs(){
        musicService!!.readFavSongs("favourite")
    }

    override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
        val binder = p1 as MusicService.MyBinder
        musicService = binder.currentService()
        readAllSongs()
    }

    override fun onServiceDisconnected(p0: ComponentName?) {
       musicService = null
    }
}