package com.example.kamennozhbum

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var resultText: TextView
    private lateinit var userScoreText: TextView
    private lateinit var appScoreText: TextView

    private var userScore = 0
    private var appScore = 0
    private var maxScore = 5 // default value

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sharedPref = getSharedPreferences("game_settings", MODE_PRIVATE)
        maxScore = sharedPref.getInt("max_score", 5)

        resultText = findViewById(R.id.result_text)
        userScoreText = findViewById(R.id.user_score)
        appScoreText = findViewById(R.id.app_score)

        val buttonRock: ImageButton = findViewById(R.id.button_rock)
        val buttonPaper: ImageButton = findViewById(R.id.button_paper)
        val buttonScissors: ImageButton = findViewById(R.id.button_scissors)
        val buttonSettings: ImageButton = findViewById(R.id.button_settings)

        buttonRock.setOnClickListener { playGame("rock") }
        buttonPaper.setOnClickListener { playGame("paper") }
        buttonScissors.setOnClickListener { playGame("scissors") }

        buttonSettings.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        val sharedPref = getSharedPreferences("game_settings", MODE_PRIVATE)
        maxScore = sharedPref.getInt("max_score", 5)
    }

    private fun playGame(userChoice: String) {
        val appChoice = getAppChoice()
        val result = getResult(userChoice, appChoice)

        when (result) {
            "win" -> {
                userScore++
                resultText.text = "You Win! App chose $appChoice"
            }
            "lose" -> {
                appScore++
                resultText.text = "You Lose! App chose $appChoice"
            }
            else -> {
                resultText.text = "It's a Draw! App chose $appChoice"
            }
        }

        userScoreText.text = "Your Score: $userScore"
        appScoreText.text = "App Score: $appScore"

        if (userScore >= maxScore || appScore >= maxScore) {
            endGame()
        }
    }

    private fun getAppChoice(): String {
        val choices = listOf("rock", "paper", "scissors")
        return choices[Random.nextInt(choices.size)]
    }

    private fun getResult(userChoice: String, appChoice: String): String {
        return if (userChoice == appChoice) {
            "draw"
        } else if ((userChoice == "rock" && appChoice == "scissors") ||
            (userChoice == "paper" && appChoice == "rock") ||
            (userChoice == "scissors" && appChoice == "paper")
        ) {
            "win"
        } else {
            "lose"
        }
    }

    private fun endGame() {
        val winner = if (userScore >= appScore) "You" else "App"
        resultText.text = "$winner Win the Game!"
        userScore = 0
        appScore = 0
    }
}
