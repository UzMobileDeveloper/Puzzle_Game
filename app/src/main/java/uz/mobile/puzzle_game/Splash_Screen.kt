package uz.mobile.puzzle_game

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.HandlerCompat.postDelayed
import uz.mobile.puzzle_game.databinding.ActivitySplashScreenBinding


class Splash_Screen : AppCompatActivity() {
    private lateinit var _binding: ActivitySplashScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(_binding.root)

        loadAnimations()
        startNextScreen()



    }

    private fun startNextScreen() {
        Handler().postDelayed({
            val intent = Intent(this,Game_Selection_Activity::class.java)
            startActivity(intent)
            finish()
        },2000)    }

    private fun loadAnimations() {
        val _left_move = AnimationUtils.loadAnimation(this, R.anim.left_move)
        val _right_move = AnimationUtils.loadAnimation(this, R.anim.righ_move)
        _binding.gameText.startAnimation(_left_move)
        _binding.puzzleText.startAnimation(_right_move)    }
}