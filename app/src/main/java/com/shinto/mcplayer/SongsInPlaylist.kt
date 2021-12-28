package com.shinto.mcplayer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.shinto.mcplayer.databinding.ActivityMainBinding
import com.shinto.mcplayer.databinding.ActivitySongsInPlaylistBinding

class SongsInPlaylist : AppCompatActivity() {

    private lateinit var binding:ActivitySongsInPlaylistBinding
    private lateinit var adapter:SongsInPlaylistAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       binding = ActivitySongsInPlaylistBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.playlistRV.setHasFixedSize(true)
        binding.playlistRV.setItemViewCacheSize(13)
        binding.playlistRV.layoutManager= LinearLayoutManager(applicationContext)
        adapter = SongsInPlaylistAdapter(applicationContext, musicService!!.songsInsidePlaylist)
        binding.playlistRV.adapter= adapter
    }
}