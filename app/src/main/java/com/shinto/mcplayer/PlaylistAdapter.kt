package com.shinto.mcplayer

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.shinto.mcplayer.databinding.PlaylistcardBinding

class PlaylistAdapter(private val context: Context,private val playlistMusic:ArrayList<Music>):
    RecyclerView.Adapter<PlaylistAdapter.MyHolder>() {

    class MyHolder(binding: PlaylistcardBinding):RecyclerView.ViewHolder(binding.root){
        val imagePlay = binding.songImgPlay
        val namePlay = binding.songNamePlay
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
     return  MyHolder(PlaylistcardBinding.inflate(LayoutInflater.from(context),parent,false))
    }

    override fun onBindViewHolder(holder: PlaylistAdapter.MyHolder, position: Int) {
        Glide.with(context).load(playlistMusic[position].artUri)
            .apply(RequestOptions().placeholder(R.drawable.mj).centerCrop()).into(holder.imagePlay)
        holder.namePlay.text = playlistMusic[position].title
    }

    override fun getItemCount(): Int = playlistMusic.size
}