package uz.mobile.puzzle_game

import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.os.IBinder

class MyService : Service() {
    lateinit var mediaPlayer: MediaPlayer
    private lateinit var shared: SharedPreferences
    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    @SuppressLint("CommitPrefEdits")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        mediaPlayer = MediaPlayer.create(applicationContext, R.raw.rain)
        shared = getSharedPreferences("settings", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = shared.edit()
        mediaPlayer.isLooping = true
        if ("on" == shared.getString("isPlaying", "").toString()) {
            editor.putString("isPlaying", "off")
            editor.apply()
            mediaPlayer.start()
        }
        return START_STICKY
    }


    override fun onDestroy() {

        mediaPlayer.stop()
        super.onDestroy()
    }
}