package com.shinto.mcplayer

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.shinto.mcplayer.databinding.PlaylistcardBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PlaylistAdapter(private val context: Context, private val playlistMusic:MutableList<String>):
    RecyclerView.Adapter<PlaylistAdapter.MyHolder>() {

    lateinit var songsInPlaylistAdapter: SongsInPlaylistAdapter

    inner class MyHolder(binding: PlaylistcardBinding):RecyclerView.ViewHolder(binding.root){
        val namePlay = binding.songNamePlay
      //  val imagePlay = binding.songImgPlay
        val playlistRv = binding.playlistCardView
    }

    private fun deleteBox(adapterPosition: Int): Boolean {
        val builder = MaterialAlertDialogBuilder(context)
        Log.i("hello","working")
        builder.setTitle("Delete Playlist?")
            .setMessage("Do you want to delete this playlist?")
            .setPositiveButton("Yes") { _, _ ->
                removingPlaylist(adapterPosition)
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
        val customDialog = builder.create()
        customDialog.show()
        customDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.RED)
        customDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.RED)
        return true
    }

    fun removingPlaylist(adapterPosition:Int){
       GlobalScope.launch(Dispatchers.IO)  {
           musicService!!.deleteAllSongsInPlaylist(adapterPosition)
           withContext(Dispatchers.Main){
               this@PlaylistAdapter.notifyDataSetChanged()
           }
       }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
     return  MyHolder(PlaylistcardBinding.inflate(LayoutInflater.from(context),parent,false))
    }

    override fun onBindViewHolder(holder: PlaylistAdapter.MyHolder, position: Int) {
//        Glide.with(context).load(playlistMusic[position].artUri)
//            .apply(RequestOptions().placeholder(R.drawable.mj).centerCrop()).into(holder.imagePlay)
        holder.namePlay.text = playlistMusic[position]
        holder.playlistRv.setOnClickListener {
            GlobalScope.launch(Dispatchers.IO) {
                musicService!!.readPlaylistSongs(playlistMusic[position])
                withContext(Dispatchers.Main){
                    val intent =  Intent(context,SongsInPlaylist::class.java)
                    context.startActivity(intent)
                }
            }
        }
        holder.playlistRv.setOnLongClickListener {
            deleteBox(position)
        }

    }

    override fun getItemCount(): Int = playlistMusic.size
}