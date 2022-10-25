package uz.mobile.puzzle_game

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.GridLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.emitter.Emitter
import nl.dionsegijn.konfetti.xml.KonfettiView
import uz.mobile.puzzle_game.databinding.ActivityNumberPuzzleBinding
import java.util.concurrent.TimeUnit
import kotlin.math.abs

class Number_Puzzle : AppCompatActivity() {
    private var _numbers = ArrayList<Int>()
    private var _x = 3
    private var _y = 3
    private var _moveCounter = 0;
    private lateinit var _emptyButton: Button
    private lateinit var _gridLayout: GridLayout
    private lateinit var _sharedPreferences: SharedPreferences
    private lateinit var binding: ActivityNumberPuzzleBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNumberPuzzleBinding.inflate(layoutInflater)
        setContentView(binding.root)
        _gridLayout = binding.container
        setTrueColor()
        setNumbers(generateNumber("old"))
        setColor()
        binding.settings.setOnClickListener {
            startActivity(Intent(this, Settings::class.java))
        }
    }


    fun OnClik(view: View) {
        val clicked: Button = view as Button
        val tag = view.tag.toString()
        val clickedX = tag[0] - '0'
        val clickedY = tag[1] - '0'
        onMove(clicked, clickedX, clickedY)
        setTrueColor()
        setState()
        val str = setState();
        _sharedPreferences = getSharedPreferences("GameState", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = _sharedPreferences.edit()
        editor.clear()
        editor.putString("GameState", str)
        editor.putString("GameStatus", "Number")
        editor.apply()
        if (isWonGame()) {
            setWonDialog()
        }
    }

    private fun touchSound() {
        _sharedPreferences = getSharedPreferences("settings", Context.MODE_PRIVATE)

        if (_sharedPreferences.getString("touch","")=="true"){
            val mediaPlayer: MediaPlayer = MediaPlayer.create(this,R.raw.touch)
            //mediaPlayer.release()
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)
            mediaPlayer.isLooping = false
            mediaPlayer.start()
            if (!mediaPlayer.isPlaying){
                mediaPlayer.stop()
            }
        }

    }

    private fun setWonDialog() {
        binding.timer.stop()
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.winning_dialog)
        val restart = dialog.findViewById<TextView>(R.id.restart)
        restart.setOnClickListener {
            dialog.dismiss()
            setNewGame()
        }
        val anim = dialog.findViewById<KonfettiView>(R.id.konfettiView)
        val party = Party(
            speed = 0f,
            maxSpeed = 30f,
            damping = 0.9f,
            spread = 360,
            colors = listOf(0xfce18a, 0xff726d, 0xf4306d, 0xb48def, 0xb95fef),
            position = Position.Relative(0.5, 0.3),
            emitter = Emitter(duration = 100, TimeUnit.MILLISECONDS).max(100)
        )
        anim.start(party)
        dialog.setCancelable(false)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.create()
        dialog.show()
        val mediaPlayer: MediaPlayer = MediaPlayer.create(applicationContext, R.raw.success)
        _sharedPreferences = getSharedPreferences("settings", Context.MODE_PRIVATE)
        if ("on" == _sharedPreferences.getString("isPlaying", "").toString()) {
            mediaPlayer.start()
        }
    }
    private fun setNewGame() {
        _moveCounter = 0
        binding.count.text = "$_moveCounter/250"
        setNumbers(generateNumber("new"))
        setColor()
        setTrueColor()

    }

    private fun setState(): String {
        var numbersStr = "";
        for (i in 0..15) {
            if ((_gridLayout.getChildAt(i) as Button).text == null || (_gridLayout.getChildAt(i) as Button).text == "") {
                numbersStr = numbersStr + "16" + ":"
            } else {
                numbersStr = numbersStr + (_gridLayout.getChildAt(i) as Button).text + ":"
            }
        }
        return numbersStr
    }

    private fun setTrueColor() {

        for (i in 1..16) {


            if ((_gridLayout.getChildAt(i - 1) as Button).text != "16") {
                if ((_gridLayout.getChildAt(i - 1) as Button).text == i.toString()) {
                    (_gridLayout.getChildAt(i - 1) as Button).setBackgroundColor(Color.GREEN)
                } else {
                    (_gridLayout.getChildAt(i - 1) as Button).setBackgroundColor(Color.BLUE)
                }
            }
        }


    }

    fun setColor() {
        for (i in 1..16) {


            if ((_gridLayout.getChildAt(i - 1) as Button).text != "16") {
                if ((_gridLayout.getChildAt(i - 1) as Button).text == i.toString()) {
                    (_gridLayout.getChildAt(i - 1) as Button).setBackgroundColor(Color.GREEN)
                } else {
                    (_gridLayout.getChildAt(i - 1) as Button).setBackgroundColor(Color.BLUE)
                }
            }
        }
    }

    @SuppressLint("ResourceAsColor")
    private fun setNumbers(_numbers: ArrayList<Int>) {
        for (i in 0 until _gridLayout.childCount) {
            (_gridLayout.getChildAt(i) as Button).text = _numbers[i].toString()
            if (_numbers[i] == 16) {
                (_gridLayout.getChildAt(i) as Button).visibility = View.GONE
                (_gridLayout.getChildAt(i) as Button).text = ""
                _emptyButton = _gridLayout.getChildAt(i) as Button
                val tag = _emptyButton.tag.toString()
                _x = tag[0] - '0'
                _y = tag[1] - '0'
            }
            (_gridLayout.getChildAt(i) as Button).setTextColor(Color.WHITE)
            (_gridLayout.getChildAt(i) as Button).setBackgroundColor(R.color.blue)

        }
    }

    private fun generateNumber(string: String): ArrayList<Int> {
        _sharedPreferences = getSharedPreferences("GameState", Context.MODE_PRIVATE)
        val oldNumberStr = _sharedPreferences.getString("GameState", "")
        if (oldNumberStr == "" || string == "new") {
            newGenerate()
        } else {
            getStateNumbers()
        }
        return _numbers;
    }

    private fun getStateNumbers(): ArrayList<Int> {
        _sharedPreferences = getSharedPreferences("GameState", Context.MODE_PRIVATE)
        _numbers.clear()
        val oldNumberStr = _sharedPreferences.getString("GameState", "")
        println(oldNumberStr)
        val e: List<String> = oldNumberStr!!.trim().split(":")
        for (i in 0..e.size - 2) {
            if ((e[i] != ":" && e[i] != " ")) {
                _numbers.add(Integer.parseInt(e[i]))
            }
        }

        return _numbers
    }

    private fun newGenerate(): ArrayList<Int> {
        _numbers.clear()
        for (i in 1 until 17) {
            _numbers.add(i)
        }
//        do {
//            _numbers.shuffle()
//        } while (isSolvable(_numbers))
        return _numbers
    }

    @SuppressLint("ResourceAsColor")
    private fun onMove(clicked: Button, clickedX: Int, clickedY: Int) {
        if (abs((clickedX + clickedY) - (_x + _y)) == 1 && abs(clickedX - _x) != 2 && abs(clickedY - _y) != 2) {
            val str = clicked.text.toString()
            touchSound()
            clicked.text = "16"
            _emptyButton.text = str
            _emptyButton.visibility = View.VISIBLE
            clicked.visibility = View.GONE
            _emptyButton = clicked
            _x = clickedX
            _y = clickedY
            _moveCounter++;
            setScore(_moveCounter)

        } else {

        }

    }

    private fun setScore(_moveCounter: Int) {
        _sharedPreferences = getSharedPreferences("settings", Context.MODE_PRIVATE)
        val priority = _sharedPreferences.getString("priority", "").toString()


        if (_moveCounter > 250) {
            showLoseDiaolog("easy")
        } else {
            binding.count.text = "$_moveCounter/5"
        }


    }

    private fun showLoseDiaolog(priority: String) {
        _sharedPreferences = getSharedPreferences("settings", Context.MODE_PRIVATE)
        var prScore = _sharedPreferences.getString(priority, "").toString()

        val editor: SharedPreferences.Editor = _sharedPreferences.edit()
        if (prScore == "") {
            editor.putString(priority, "1")
            editor.apply()
        } else {
            prScore = (Integer.parseInt(prScore) + 1).toString()

            editor.putString(priority, prScore)
            editor.apply()
        }
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.lose_dialog)
        val restart = dialog.findViewById<TextView>(R.id.restart)
        restart.setOnClickListener {
            dialog.dismiss()
            setNewGame()
        }
        Toast.makeText(this, "$prScore", Toast.LENGTH_SHORT).show()
        dialog.setCancelable(false)
        dialog.create()
        dialog.show()

    }

    private fun isSolvable(_numbers: ArrayList<Int>): Boolean {
        var counter = 0;
        for (i in 0 until _numbers.size) {
            if (_numbers[i] == 16) {
                counter += i / 4 + 1
                continue
            }
            for (j in i until _numbers.size) {
                if (_numbers[i] > _numbers[j]) counter++
            }
        }
        return counter % 2 == 0
    }

    @SuppressLint("ResourceAsColor")
    private fun isWonGame(): Boolean {
        var counter = 1
        for (i in 0..14) {
            if (((_gridLayout.getChildAt(i) as Button).text).trim() == counter.toString().trim()) {
                counter++
            }
        }
        return counter == 16
    }

}