package com.example.memorytest.text

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import com.example.memorytest.games.AbstractGame
import com.example.memorytest.games.GameState
import java.time.Duration
import java.time.Instant

class MotoricTextWatcher(private val game: AbstractGame, private val text: String) : TextWatcher {

    private val wholeWordCode = 2_000_000

    lateinit var prevText: String
    lateinit var startTime: Instant
    lateinit var inputtedText: String
    val symbolsToSpeed: MutableList<Pair<Pair<Int, Int>, Long>> = mutableListOf()

    fun isSomethingInputted(): Boolean {
        return this::startTime.isInitialized
    }

    override fun afterTextChanged(s: Editable?) {
        inputtedText = s.toString()
        Log.i("Inputted text: ", inputtedText)

        if (this::prevText.isInitialized) {
            val duration = Duration.between(startTime, Instant.now()).toMillis()
            val difference = inputtedText.length - prevText.length

            //user add a symbol
            if (difference == 1) {
                symbolsToSpeed.add(
                    (prevText.last().toInt() to inputtedText.last().toInt()) to duration
                )
            } else if (difference > 1) {
                symbolsToSpeed.add(
                    (prevText.last().toInt() to wholeWordCode) to duration
                )
            }
        }

        prevText = inputtedText
        startTime = Instant.now()

        if (text == inputtedText) {
            game.changeState(GameState.END)
        }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

}