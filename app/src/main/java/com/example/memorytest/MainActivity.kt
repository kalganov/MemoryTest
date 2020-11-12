package com.example.memorytest

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.memorytest.games.CardGame
import com.example.memorytest.games.NumberGame
import com.example.memorytest.games.TextGame


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun startNumberGame(view: View) {
        val game = Intent(this, NumberGame::class.java)
        startActivity(game)
    }

    fun startTextGame(view: View) {
        val game = Intent(this, TextGame::class.java)
        startActivity(game)
    }

    fun startCardsGame(view: View) {
        val game = Intent(this, CardGame::class.java)
        startActivity(game)
    }
}