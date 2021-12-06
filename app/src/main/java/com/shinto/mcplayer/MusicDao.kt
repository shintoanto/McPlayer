package com.shinto.mcplayer

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MusicDao {

    @Insert
    fun addMusic(music:Music)

    @Delete
    fun removeMusic(music: Music)

    @Query("SELECT * FROM `Musictable` WHERE playListName LIKE :favorite")
    fun readAllData(favorite: String): List<Music>
}