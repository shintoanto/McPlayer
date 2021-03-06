package com.shinto.mcplayer

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MusicDao {

    @Insert
    fun addMusic(music: Music)

    @Delete
    fun removeMusic(music: Music)

    @Query("SELECT * FROM `Musictable` WHERE playListName LIKE :favorite")
    fun readAllData(favorite: String): List<Music>

    @Query("SELECT  DISTINCT playListName  FROM `Musictable`")
    fun readDistinctNames(): List<String>

    @Query("SELECT * FROM `Musictable` WHERE playListName LIKE :name  AND Id LIKE :idOfSong")
    fun checkingSongsInPlaylist(idOfSong: Int?, name: String?): Boolean

    @Query("SELECT * FROM `Musictable` WHERE playListName LIKE :name")
    fun readAllSongsFromPlaylist(name: String): List<Music>

    @Query("DELETE FROM `Musictable` WHERE playListName LIKE :name")
    fun deleteAllSongs(name: String)
}