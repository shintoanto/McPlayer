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
import com.shinto.mcplayer.databinding.MusicViewBinding

class MusicAdapter(private val context: Context, private val musicList: ArrayList<Music>) :
    RecyclerView.Adapter<MusicAdapter.MyHolder>() {

    class MyHolder(binding: MusicViewBinding) : RecyclerView.ViewHolder(binding.root) {
        val title = binding.songNameMV
        val album = binding.songAlbumMV
        val duration = binding.songDuration
        val image = binding.imageMV
        val root = binding.root
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        return MyHolder(MusicViewBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.title.text = musicList[position].title
        holder.album.text = musicList[position].album
        holder.duration.text = formatDuration(musicList[position].duration)

        // image fetching and setting in music
        Glide.with(context).load(musicList[position].artUri)
            .apply(RequestOptions().placeholder(R.drawable.mj).centerCrop()).into(holder.image)

        holder.root.setOnClickListener {
            val music = musicList[position]
            val intent = Intent(context, Player_activity::class.java)
            intent.putExtra("index", position)
            Log.d("pass1",position.toString())
            intent.putExtra("class", "MusicAdapter")
            intent.putExtra(Player_activity.MUSIC_NAME,music)
            ContextCompat.startActivity(context, intent, null)
           // (holder.root.context as MainActivity)
        }
    }

    override fun getItemCount(): Int = musicList.size

}