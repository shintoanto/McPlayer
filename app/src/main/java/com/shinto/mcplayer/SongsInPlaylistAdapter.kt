package com.shinto.mcplayer

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.shinto.mcplayer.databinding.PlaylistSongsRvBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SongsInPlaylistAdapter(private val context: Context,private val playlistSongMusic:List<Music>,val onClick:(Int)->Unit):RecyclerView.Adapter<SongsInPlaylistAdapter.MyHolder>() {

  inner class MyHolder(binding: PlaylistSongsRvBinding ): RecyclerView.ViewHolder(binding.root){
      val imageVie = binding.imageMV
      val songName = binding.songNameMV
  }
    private fun deleteBox(adapterposition:Int):Boolean{
        val builder = MaterialAlertDialogBuilder(context)
        builder.setTitle("Delete music")
        .setMessage("Do you want to delete this music")
        .setPositiveButton("Yes"){_,_ ->
            removePlaylist(adapterposition)
        }
            .setNegativeButton("No"){dialoge,_ ->
                dialoge.dismiss()
            }
        val customAlertButton = builder.create()
        customAlertButton.show()
        customAlertButton.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.GREEN)
        customAlertButton.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.RED)
        return true
    }

    private fun removePlaylist(position: Int){
        GlobalScope.launch(Dispatchers.IO){
           musicService!!.deleteAllSongsInPlaylist(position)
            withContext(Dispatchers.Main){
                this@SongsInPlaylistAdapter.notifyDataSetChanged()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongsInPlaylistAdapter.MyHolder {
        return MyHolder(PlaylistSongsRvBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun onBindViewHolder(holder: SongsInPlaylistAdapter.MyHolder, position: Int) {
        Log.d("sone",playlistSongMusic[position].toString())
        holder.songName.text = playlistSongMusic[position].title
        Glide.with(context).load(playlistSongMusic[position].artUri)
            .apply(RequestOptions().placeholder(R.drawable.musicanote).centerCrop()).into(holder.imageVie)

        holder.itemView.setOnClickListener {
            onClick(position)

        }
        holder.itemView.setOnLongClickListener {
            deleteBox(position)
        }
    }

    override fun getItemCount(): Int =playlistSongMusic.size

}