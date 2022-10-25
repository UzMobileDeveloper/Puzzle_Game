package uz.mobile.puzzle_game

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import uz.mobile.puzzle_game.databinding.ActivitySelectionGameBinding

class Selection_Game : AppCompatActivity() {
    private lateinit var binding: ActivitySelectionGameBinding
    private lateinit var shared: SharedPreferences
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectionGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        shared = getSharedPreferences("game", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = shared.edit()
        sharedPreferences = getSharedPreferences("GameState", Context.MODE_PRIVATE)
        val editorSH: SharedPreferences.Editor = sharedPreferences.edit()

        binding.imagePuzzle.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    Image_Puzzle::class.java
                )
            )
            finish()
            editor.clear()
            editor.putString("gameState","Image_Puzzle")
            editor.apply()
            editorSH.clear()
            editorSH.apply()

        }
        binding.numberPuzzle.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    Number_Puzzle::class.java
                )
            )
            finish()
            editor.clear()
            editor.putString("gameState","Number_Puzzle")
            editor.apply()
            editorSH.clear()
            editorSH.apply()
        }
        binding.slidePuzzle.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    Slide_Puzzle::class.java
                )
            )
            finish()
            editor.clear()
            editor.putString("gameState","Slide_Puzzle")
            editor.apply()
            editorSH.clear()
            editorSH.apply()
        }


    }
}