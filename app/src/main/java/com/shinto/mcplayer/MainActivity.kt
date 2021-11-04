package com.shinto.mcplayer

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.Toast
import android.widget.ToggleButton
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.shinto.mcplayer.databinding.ActivityMainBinding
import java.io.File
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var musicAdapter: MusicAdapter

    companion object {
        lateinit var MusicListMA: ArrayList<Music>
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requstRuntimePermission()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        toggle = ActionBarDrawerToggle(this, binding.root, R.string.open, R.string.close)
        binding.root.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        MusicListMA = getAllAudio()
        val musicList = ArrayList<String>()
        musicList.add("First song")
        musicList.add("second song")

        binding.musicRv.setHasFixedSize(true)
        binding.musicRv.setItemViewCacheSize(13)
        binding.musicRv.layoutManager = LinearLayoutManager(this@MainActivity)
        musicAdapter = MusicAdapter(this@MainActivity, MusicListMA)
        binding.musicRv.adapter = musicAdapter
        binding.btnTotalSongs.text = "Total songs:" + musicAdapter.itemCount

        binding.shufflebtn.setOnClickListener {
            Toast.makeText(this@MainActivity, "Text button clicked", Toast.LENGTH_LONG).show()
        }

        binding.favouriteBtn.setOnClickListener {
            val intent = Intent(this@MainActivity, Favourite::class.java)
            startActivity(intent)
        }

//        binding.playlistBtn.setOnClickListener {
//            val intent= Intent(this@MainActivity,Player_activity::class.java)
//            startActivity(intent)
//        }

        binding.navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.item1 -> Toast.makeText(baseContext, "Feedback", Toast.LENGTH_LONG).show()
                R.id.item2 -> Toast.makeText(baseContext, "settings", Toast.LENGTH_LONG).show()
                R.id.item3 -> Toast.makeText(baseContext, "favourite", Toast.LENGTH_LONG).show()
                // R.id.navExit -> exitProcess(1)
            }
            true
        }

    }

    private fun requstRuntimePermission() {
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                13
            )
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 13) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                Toast.makeText(this, "", Toast.LENGTH_LONG).show()
            else
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    13
                )
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item))
            return true
        return super.onOptionsItemSelected(item)
    }


    private fun initLayout() {

    }

  // @SuppressLint("Range")
    private fun getAllAudio(): ArrayList<Music> {
        val tempList = ArrayList<Music>()
        // selection is using athe type data ane anne ariyan
        val selection = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.DATE_ADDED,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.ALBUM_ID
        )
//        val cursor = this.contentResolver.query(
//            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection,
//            selection, null, MediaStore.Audio.Media.DATE_ADDED + "DESC", null
//        )

        val cursor : Cursor? = this.contentResolver.query(
            selection,projection,null ,null, MediaStore.Audio.Media.TITLE
        )

        if (cursor != null) {
            if (cursor.moveToFirst())
                do {
                    val idC = cursor.getString(0)
                    val titleC =
                        cursor.getString(1)

                    val albumC =
                        cursor.getString(2)
                    val artistC =
                        cursor.getString(3)
                    val durationC =
                        cursor.getLong(4)
                    val pathC = cursor.getString(6)
                    val albumIdC=cursor.getString(7)
                    val uri= Uri.parse("content://media/external/audio/albumart")
                    val artUriC = Uri.withAppendedPath(uri,albumIdC).toString()

                    val music = Music(
                        id = idC,
                        title = titleC,
                        album = albumC,
                        artist = artistC,
                        path = pathC,
                        duration = durationC,
                        artUri = artUriC
                    )
                    val file = File(music.path)
                    if (file.exists())
                        tempList.add(music)
                } while (cursor.moveToNext())
            cursor.close()
        }
        return tempList
    }

}