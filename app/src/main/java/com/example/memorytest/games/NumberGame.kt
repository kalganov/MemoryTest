package com.example.memorytest.games

import android.os.Bundle
import android.view.Gravity
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.memorytest.R
import java.lang.IllegalArgumentException
import java.util.*
import kotlin.random.Random.Default.nextInt


class NumberGame(private val generatedNumbers: List<Int> = mutableListOf()) : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.number_game)

        val layout = findViewById<TableLayout>(R.id.tableLayout)

        setUpLayout(layout, 2)
        setUpTimer()
    }

    private fun setUpLayout(layout: TableLayout, numberCount: Int) {
        val tableRow = TableRow(this)
        for (i in 1..numberCount) {
            val nextInt = nextInt(0, 9)

            val number = ImageView(this).apply {
                setImageResource(getNumberImageResource(nextInt))
            }
            tableRow.addView(number)
            number.layoutParams = TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.MATCH_PARENT
            ).apply {
                setMargins(20, 20, 20, 20)
            }

            generatedNumbers
        }
        layout.addView(tableRow)
        tableRow.gravity = Gravity.CENTER
        tableRow.layoutParams = TableLayout.LayoutParams(
            TableLayout.LayoutParams.MATCH_PARENT,
            TableLayout.LayoutParams.MATCH_PARENT
        ).apply {
            weight = 1.0F
            gravity = Gravity.CENTER
        }
    }

    private fun getNumberImageResource(number: Int): Int {
        return when (number) {
            0 -> R.drawable.ic_zero
            1 -> R.drawable.ic_one
            2 -> R.drawable.ic_two
            3 -> R.drawable.ic_three
            4 -> R.drawable.ic_four
            5 -> R.drawable.ic_five
            6 -> R.drawable.ic_six
            7 -> R.drawable.ic_seven
            8 -> R.drawable.ic_eight
            9 -> R.drawable.ic_nine
            else -> throw IllegalArgumentException()
        }
    }

    private fun setUpTimer() {
        val timer = Timer()
        timer.schedule(GameTimer(findViewById(R.id.timer)), 1000, 500)
    }

    class GameTimer(private val progressBar: ProgressBar) : TimerTask() {
        override fun run() {
            progressBar.incrementProgressBy(2)
        }
    }
}