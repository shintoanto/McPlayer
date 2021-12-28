package com.shinto.mcplayer

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.shinto.mcplayer.databinding.ActivitySongsInPlaylistBinding
import com.shinto.mcplayer.databinding.PlaylistSongsRvBinding

class SongsInPlaylistAdapter(private val context: Context,private val playlistSongMusic:List<Music>):RecyclerView.Adapter<SongsInPlaylistAdapter.MyHolder>() {

  inner class MyHolder(binding: PlaylistSongsRvBinding ): RecyclerView.ViewHolder(binding.root){
      val imageVie = binding.imageMV
      val songName = binding.songNameMV
  }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongsInPlaylistAdapter.MyHolder {
        return MyHolder(PlaylistSongsRvBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun onBindViewHolder(holder: SongsInPlaylistAdapter.MyHolder, position: Int) {
        holder.songName.text = playlistSongMusic[position].title
        Glide.with(context).load(playlistSongMusic[position].artUri)
            .apply(RequestOptions().placeholder(R.drawable.musicanote).centerCrop()).into(holder.imageVie)

        holder.itemView.setOnClickListener {
            val intent = Intent(context,Player_activity::class.java)
            intent.putExtra("index",position)
            intent.putExtra("class","SongsInPlaylistAdapter")
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(context,intent,null)
        }
    }

    override fun getItemCount(): Int =playlistSongMusic.size

}