package com.shinto.mcplayer

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.shinto.mcplayer.databinding.FavCardBinding

class FavouriteAdapter(private val context: Context, private var musicList: ArrayList<String>) :
    RecyclerView.Adapter<FavouriteAdapter.ViewHolder>() {

    class ViewHolder(binding: FavCardBinding) : RecyclerView.ViewHolder(binding.root) {
        val image = binding.songImgFav
        val title = binding.songNameFav
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(FavCardBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.title.text = musicList[position]
    }

    override fun getItemCount(): Int {
        return musicList.size
    }
}