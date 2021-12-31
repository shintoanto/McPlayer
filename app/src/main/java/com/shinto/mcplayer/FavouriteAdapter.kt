package com.shinto.mcplayer

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.shinto.mcplayer.databinding.FavCardBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavouriteAdapter(private val context: Context, private var musicList: ArrayList<Music>) :
    RecyclerView.Adapter<FavouriteAdapter.ViewHolder>() {

    class ViewHolder(binding: FavCardBinding) : RecyclerView.ViewHolder(binding.root) {
        val image = binding.songImgFav
        val title = binding.songNameFav
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
                this@FavouriteAdapter.notifyDataSetChanged()
            }
        }
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
        holder.itemView.setOnLongClickListener {
            deleteBox(position)
        }
    }

    override fun getItemCount(): Int {
        return musicList.size
    }

}