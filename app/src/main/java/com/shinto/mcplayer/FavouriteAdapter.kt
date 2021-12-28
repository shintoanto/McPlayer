package com.shinto.mcplayer

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.shinto.mcplayer.databinding.FavCardBinding

class FavouriteAdapter(private val context: Context, private var musicList: ArrayList<Music>) :
    RecyclerView.Adapter<FavouriteAdapter.ViewHolder>() {

    class ViewHolder(binding: FavCardBinding) : RecyclerView.ViewHolder(binding.root) {
        val image = binding.songImgFav
        val title = binding.songNameFav
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(FavCardBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(context).load(musicList[position].artUri)
            .apply(RequestOptions().placeholder(R.drawable.musicanote).centerCrop()).into(holder.image)

        holder.title.text = musicList[position].title
        holder.itemView.setOnClickListener {
            val intent = Intent(context,Player_activity::class.java)
            intent.putExtra("index",position)
            intent.putExtra("class","FavouriteAdapter")
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            ContextCompat.startActivity(context, intent, null)
        }
    }

    override fun getItemCount(): Int {
        return musicList.size
    }

}