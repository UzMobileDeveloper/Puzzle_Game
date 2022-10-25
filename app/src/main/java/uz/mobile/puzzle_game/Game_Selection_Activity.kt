package uz.mobile.puzzle_game

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import uz.mobile.puzzle_game.databinding.ActivityGameSelectionBinding
import java.sql.Time
import java.util.*

class Game_Selection_Activity : AppCompatActivity() {
    private lateinit var binding: ActivityGameSelectionBinding
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var shared: SharedPreferences
    private var music: String = ""
    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameSelectionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadAnimations()
        shared = getSharedPreferences("game", Context.MODE_PRIVATE)
        val gameState = shared.getString("gameState","")


        binding.resume.setOnClickListener{
            getStateGame()
        }

        binding.settings.setOnClickListener {
            startActivity(Intent(this,Settings::class.java))
        }
        binding.restart.setOnClickListener {
            startActivity(Intent(this,Selection_Game::class.java))
        }
        getMusicPlayerState()


    }

    private fun getStateGame() {
        shared = getSharedPreferences("game", Context.MODE_PRIVATE)
        val gameState = shared.getString("gameState","")
        if (gameState=="Image_Puzzle"){
            startActivity(
                Intent(
                    this,
                    Image_Puzzle::class.java
                )
            )

        }
        if (gameState=="Number_Puzzle"){
            startActivity(
                Intent(
                    this,
                    Number_Puzzle::class.java
                )
            )

        }
        if (gameState=="Slide_Puzzle"){
            startActivity(
                Intent(
                    this,
                    Slide_Puzzle::class.java
                )
            )

        }

    }

    private fun loadAnimations() {
        val _left_move = AnimationUtils.loadAnimation(this, R.anim.left_move)
        val _right_move = AnimationUtils.loadAnimation(this, R.anim.righ_move)
        binding.resume.startAnimation(_left_move)
        binding.settings.startAnimation(_left_move)
        binding.restart.startAnimation(_right_move)

    }
     fun getMusicPlayerState() {
         shared = getSharedPreferences("settings", Context.MODE_PRIVATE)
         if ("off" == shared.getString("isPlaying", "").toString()) {
             shared = getSharedPreferences("settings", Context.MODE_PRIVATE)
             val editor: SharedPreferences.Editor = shared.edit()
             editor.putString("isPlaying", "on")
             editor.apply()
             startService(Intent(this, MyService::class.java))
         }


    }


}