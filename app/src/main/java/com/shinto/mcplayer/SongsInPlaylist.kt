package com.shinto.mcplayer

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.shinto.mcplayer.databinding.ActivityMainBinding
import com.shinto.mcplayer.databinding.ActivitySongsInPlaylistBinding

class SongsInPlaylist : AppCompatActivity(),ServiceConnection {

    private lateinit var binding:ActivitySongsInPlaylistBinding
    private lateinit var adapter:SongsInPlaylistAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       binding = ActivitySongsInPlaylistBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.playlistRV.setHasFixedSize(true)
        binding.playlistRV.setItemViewCacheSize(13)
        binding.playlistRV.layoutManager= LinearLayoutManager(applicationContext)
        adapter = SongsInPlaylistAdapter(applicationContext, musicService!!.songsInsidePlaylist,::onClick)
        Log.d("sonPlam", musicService!!.songsInsidePlaylist.toString())
        binding.playlistRV.adapter= adapter
    }

     fun  onClick(positon:Int){
         musicService!!.musicListPA = musicService!!.songsInsidePlaylist.toMutableList()
        val intent = Intent(this,Player_activity::class.java)
        intent.putExtra("index",positon)
        intent.putExtra("class","SongsInPlaylistAdapter")
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        ContextCompat.startActivity(this, intent, null)
    }

    override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
        val binder = p1 as MusicService.MyBinder
        musicService = binder.currentService()

    }

    override fun onServiceDisconnected(p0: ComponentName?) {
        TODO("Not yet implemented")
    }
}