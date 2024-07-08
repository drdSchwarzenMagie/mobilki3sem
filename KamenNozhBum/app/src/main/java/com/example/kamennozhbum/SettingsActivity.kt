package com.example.kamennozhbum

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val editTextMaxScore: EditText = findViewById(R.id.editTextMaxScore)
        val buttonSave: Button = findViewById(R.id.buttonSave)

        val sharedPref = getSharedPreferences("game_settings", MODE_PRIVATE)
        val savedMaxScore = sharedPref.getInt("max_score", 5)
        editTextMaxScore.setText(savedMaxScore.toString())

        buttonSave.setOnClickListener {
            val maxScore = editTextMaxScore.text.toString().toIntOrNull() ?: 5
            with(sharedPref.edit()) {
                putInt("max_score", maxScore)
                apply()
            }
            finish()
        }
    }
}
