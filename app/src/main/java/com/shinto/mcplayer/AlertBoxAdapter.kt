package com.shinto.mcplayer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AlertBoxAdapter(recyclrviewEdit:EditText):RecyclerView.Adapter<AlertBoxAdapter.AlertBoxViewHolder>() {

    var recycleEdit = recyclrviewEdit

    inner class AlertBoxViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val songNameInAlert:TextView = itemView.findViewById(R.id.alertBox_playList_name)
        var edit = recycleEdit
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlertBoxAdapter.AlertBoxViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.alert_box_rv,parent, false)
        return AlertBoxViewHolder(view)
    }

    override fun onBindViewHolder(holder: AlertBoxAdapter.AlertBoxViewHolder, position: Int) {
       holder.itemView.apply {
           holder.songNameInAlert.text = playlistM[position]
       }

        holder.songNameInAlert.setOnClickListener {
            holder.edit.setText(playlistM[position])
        }

    }

    override fun getItemCount(): Int = playlistM.size
}