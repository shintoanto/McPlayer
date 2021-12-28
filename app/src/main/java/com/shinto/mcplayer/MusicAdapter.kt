package com.shinto.mcplayer

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.PopupMenu
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.shinto.mcplayer.databinding.MusicViewBinding
import kotlinx.coroutines.*

class MusicAdapter(private val context: Context, private val musicList: MutableList<Music>) :
    RecyclerView.Adapter<MusicAdapter.MyHolder>() {

     lateinit var alertBoxAdapter: AlertBoxAdapter
     private lateinit var songDao:MusicDao
     var listOfMusic= musicList
     private lateinit var playlistAdapter: PlaylistAdapter
     private lateinit var main:MainActivity

    class MyHolder(binding: MusicViewBinding) : RecyclerView.ViewHolder(binding.root) {
        val title = binding.songNameMV
        val album = binding.songAlbumMV
        val duration = binding.songDuration
        val image = binding.imageMV
        val root = binding.root
        val threeDot = binding.popUp
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        return MyHolder(MusicViewBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.title.text = listOfMusic[position].title
        holder.album.text = listOfMusic[position].album
        holder.duration.text = formatDuration(musicList[position].duration)
        holder.threeDot.setOnClickListener {
            showPop(it,position)
        }

        // image fetching and setting in music
        Glide.with(context).load(listOfMusic[position].artUri)
            .apply(RequestOptions().placeholder(R.drawable.musicanote).centerCrop()).into(holder.image)

        holder.root.setOnClickListener {
            val music = listOfMusic[position]
            val intent = Intent(context, Player_activity::class.java)

            intent.putExtra("index", position)
            intent.putExtra("class", "MusicAdapter")
            intent.putExtra(Player_activity.MUSIC_NAME,music)
            ContextCompat.startActivity(context, intent, null)
           // (holder.root.context as MainActivity)
        }
    }

    override fun getItemCount(): Int = musicList.size

 private fun showPop(view:View,adapterPosion: Int){
     val popup = PopupMenu(context,view)
     popup.inflate(R.menu.popup_menu)

     popup.setOnMenuItemClickListener { item: MenuItem ->
         when (item.itemId) {
             R.id.popup_add_to_playlist -> {
                 idOne(adapterPosion)
             }
             R.id.popup_delete -> {
                 Toast.makeText(context, item.title, Toast.LENGTH_LONG).show()
             }

         }
         true
     }
     popup.show()
 }
    private fun idOne(adapterPosion:Int){
        // alert box showing
        val alertView = View.inflate(context,R.layout.playlist_screen,null)
        val builder =  AlertDialog.Builder(context)
        builder.setView(alertView)
       val dialog= builder.create()

        dialog.show()

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        musicService!!.readPlayListNameFromDB()

        alertView.findViewById<RecyclerView>(R.id.alertRecyclerViewForPlaylist).apply {
            alertBoxAdapter = AlertBoxAdapter(alertView.findViewById(R.id.write_playlist_name))
            adapter = alertBoxAdapter
            layoutManager= GridLayoutManager(context,2)
        }

       // Add button
                alertView.findViewById<Button>(R.id.add_Btn).setOnClickListener {
                    val name = alertView.findViewById<EditText>(R.id.write_playlist_name).text.toString()
                    var isChecked:Boolean
                        GlobalScope.launch(Dispatchers.IO) {
                            songDao= MusicDatabase.getDatabase(context).songDao()
                            isChecked =
                                songDao.checkingSongsInPlaylist(listOfMusic[adapterPosion].id, name)
                            Log.d("this", isChecked.toString())
                            withContext(Dispatchers.IO) {
                                if (isChecked) {
                                    main.toast(context,"this is something")
                                    dialog.dismiss()
                                } else {
                                    listOfMusic[adapterPosion].playListName = name
                                    playlistM.add(name)
                                    val timestampId = System.currentTimeMillis()
                                    listOfMusic[adapterPosion].timestamp =
                                        timestampId.toString() + listOfMusic[adapterPosion].id
                                    if (listOfMusic[adapterPosion].playListName.contains("favourite")) {
                                        listOfMusic.remove(listOfMusic[adapterPosion])
                                    }
                                    addTooPlaylist(adapterPosion)
                                  main.toast(context,"this is added to the playlist")
                                    dialog.dismiss()
                                }
                                playlistAdapter = PlaylistAdapter(context, playlistM)
                            }
                        }
                }

        alertView.findViewById<Button>(R.id.cancelBtn).setOnClickListener {
            dialog.dismiss()
        }
    }

    private fun addTooPlaylist(adapterPosion: Int){
        System.currentTimeMillis()
        CoroutineScope(Dispatchers.IO).launch {
            songDao.addMusic(listOfMusic[adapterPosion])
        }
    }



}