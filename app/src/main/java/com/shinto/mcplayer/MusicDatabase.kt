package com.shinto.mcplayer

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Music::class], exportSchema = false, version = 1)
abstract class MusicDatabase : RoomDatabase() {
    abstract fun songDao(): MusicDao

    companion object {
        @Volatile
        private var INSTANCE: MusicDatabase? = null
        fun getDatabase(context: Context): MusicDatabase {
          //  if(INSTANCE == null)
            return INSTANCE ?: synchronized(this) {

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MusicDatabase::class.java,
                    "music_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}