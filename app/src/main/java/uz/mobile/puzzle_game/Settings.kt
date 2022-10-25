package uz.mobile.puzzle_game

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import uz.mobile.puzzle_game.databinding.ActivitySettingsBinding

class Settings : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    private lateinit var shared: SharedPreferences
    private var touch: String = ""
    private var vibration: String = ""
    private var music: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setStateSwitch()
        binding.musicSwitch.setOnClickListener {
            getStateSwitch()
            setStateSwitch()
        }
        binding.vibrationSwitch.setOnClickListener {
            getStateSwitch()
            setStateSwitch()
        }
        binding.clickSound.setOnClickListener {
            getStateSwitch()
            setStateSwitch()
        }
        binding.priority.setOnClickListener {
            showPriorityDialog()
        }

    }

    private fun showPriorityDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.priority_dialog)
        val radioGroup = dialog.findViewById<RadioGroup>(R.id.radio_group)
        val checkedId = radioGroup.checkedRadioButtonId


        dialog.setCancelable(false)
        dialog.create()
        dialog.show()
    }

    private fun getStateSwitch() {
        shared = getSharedPreferences("settings", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = shared.edit()

        if (binding.vibrationSwitch.isChecked) {
            vibration = "true"
            editor.putString("vibration", vibration)
            editor.apply()
        } else {
            vibration = "false"
            editor.putString("vibration", vibration)
            editor.apply()

        }
        if (binding.musicSwitch.isChecked) {
            music = "true"
            editor.putString("music", music)
            editor.apply()
        } else {
            music = "false"
            editor.putString("music", music)
            editor.apply()

        }

        if (binding.clickSound.isChecked) {
            touch = "true"
            editor.putString("touch", touch)
            editor.apply()
            binding.clickSound.isChecked = touch == "" || touch == "true"

        } else {
            touch = "false"
            editor.putString("touch", touch)
            editor.apply()
            binding.clickSound.isChecked = touch == "" || touch == "true"

        }


    }

    @SuppressLint("SetTextI18n")
    private fun setStateSwitch() {
        shared = getSharedPreferences("settings", Context.MODE_PRIVATE)
        val priority = shared.getString("priority", "").toString()
        vibration = shared.getString("vibration", "").toString()
        binding.priorityTxt.text = "Priority:$priority"

        if (vibration == "false") {
            binding.vibrationSwitch.isChecked = false

        } else {
            binding.vibrationSwitch.isChecked = true
        }
        music = shared.getString("music", "").toString()
        if (music == "false") {
            shared = getSharedPreferences("settings", Context.MODE_PRIVATE)
            val editor: SharedPreferences.Editor = shared.edit()
            editor.putString("isPlaying", "on")
            editor.apply()
            stopService(Intent(this, MyService::class.java))
            binding.musicSwitch.isChecked = false
        } else {
            shared = getSharedPreferences("settings", Context.MODE_PRIVATE)
            if ("off" != shared.getString("isPlaying", "").toString()) {
                shared = getSharedPreferences("settings", Context.MODE_PRIVATE)
                val editor: SharedPreferences.Editor = shared.edit()
                editor.putString("isPlaying", "on")
                editor.apply()
                startService(Intent(this, MyService::class.java))
            }


            binding.musicSwitch.isChecked = true
        }
        touch = shared.getString("touch", "").toString()

        if (touch == "false") {
            binding.clickSound.isChecked = false

        } else {
            binding.clickSound.isChecked = true
        }


    }

}


