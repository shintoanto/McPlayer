package com.shinto.mcplayer

import android.content.ComponentName
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import androidx.recyclerview.widget.GridLayoutManager
import com.shinto.mcplayer.databinding.ActivityPlayerBinding
import com.shinto.mcplayer.databinding.ActivityPlaylistScreenBinding
import com.shinto.mcplayer.databinding.PlaylistsBinding

class Playlist_screen : AppCompatActivity(),ServiceConnection {

    private lateinit var binding: ActivityPlaylistScreenBinding
    private lateinit var adapter: PlaylistAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityPlaylistScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.favouritesRV.setHasFixedSize(true)
        binding.favouritesRV.setItemViewCacheSize(13)
        binding.favouritesRV.layoutManager = GridLayoutManager(this,2)
        adapter = PlaylistAdapter(this@Playlist_screen, playlistM)
        binding.favouritesRV.adapter = adapter
    }

    private fun readAllPlaylistSongs(){
        musicService!!.readPlaylistSongs("name")
    }

    override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
        val binder = p1 as MusicService.MyBinder
        musicService = binder.currentService()
        readAllPlaylistSongs()
    }

    override fun onServiceDisconnected(p0: ComponentName?){
        musicService = null
    }


}