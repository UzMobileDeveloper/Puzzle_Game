package uz.mobile.puzzle_game

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import uz.mobile.puzzle_game.databinding.ActivityImagePuzzleBinding

class Image_Puzzle : AppCompatActivity() {
    private lateinit var binding: ActivityImagePuzzleBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImagePuzzleBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}