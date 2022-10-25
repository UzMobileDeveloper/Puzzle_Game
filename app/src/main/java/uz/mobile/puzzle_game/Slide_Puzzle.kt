package uz.mobile.puzzle_game

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import uz.mobile.puzzle_game.databinding.ActivitySlidePuzzleBinding

class Slide_Puzzle : AppCompatActivity() {
    private lateinit var binding: ActivitySlidePuzzleBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivitySlidePuzzleBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}