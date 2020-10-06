package com.example.memorytest.games

import android.os.Bundle
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.View.VISIBLE
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import com.example.memorytest.R
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*
import kotlin.random.Random.Default.nextInt


//TODO make it as a parameter
private const val CARDS_IN_ROW = 4
private const val CARDS_IN_ALL = 8

class NumberGame : AppCompatActivity() {

    private lateinit var state: GameState

    private lateinit var progressBar: ProgressBar
    private lateinit var layout: TableLayout
    private lateinit var progressBarLabel: TextView
    private lateinit var endLabel: TextView
    private lateinit var generatedNumbers: List<Int>
    private lateinit var nextOrRepeatButton: Button

    private var hiddenNumber: Int = -1
    private val timer = Timer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.number_game)

        layout = findViewById(R.id.tableLayout)
        progressBar = findViewById(R.id.timer)
        progressBarLabel = findViewById(R.id.timerProgressLabel)
        endLabel = findViewById(R.id.endLabel)
        nextOrRepeatButton = findViewById(R.id.nextOrRepeat)

        setState(GameState.INIT)
        setUpTimer()
    }

    private fun hideNumber(): Int {
        val newGeneratedNumbers = generatedNumbers.toMutableList()
        val hiddenNumberIndex = nextInt(0, newGeneratedNumbers.size)
        val hiddenNumber = newGeneratedNumbers[hiddenNumberIndex] * nextInt(1) / 10
        newGeneratedNumbers[hiddenNumberIndex] = hiddenNumber
        newGeneratedNumbers.shuffle()

        setUpLayout(newGeneratedNumbers)

        return hiddenNumber
    }

    private fun generateNumbers(numberCount: Int): List<Int> {
        val generatedNumbers = List(numberCount) { nextInt(0, 9) }
        setUpLayout(generatedNumbers)
        return generatedNumbers
    }


    private fun setUpLayout(numbers: List<Int>) {
        layout.removeAllViews()
        numbers.chunked(CARDS_IN_ROW).forEach {
            val tableRow = TableRow(this)
            it.forEach {
                val number = CircleImageView(this).apply {
                    setImageResource(getNumberImageResource(it))
                    contentDescription = it.toString()
                    if (state == GameState.PROGRESS) {
                        setOnTouchListener { v, event ->
                            when (event.action) {
                                MotionEvent.ACTION_DOWN -> {
                                    disableCards()
                                    highlightChosenCard(v)
                                    setState(GameState.END)
                                }
                            }
                            v.performClick()
                            true
                        }
                    }
                }
                tableRow.addView(number)
                number.layoutParams = TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.MATCH_PARENT
                ).apply {
                    setMargins(20, 20, 20, 20)
                }
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
    }

    private fun disableCards() {
        runOnUiThread {
            this@NumberGame.layout.children.forEach { row ->
                (row as TableRow).children.forEach { image ->
                    image.setOnTouchListener(
                        null
                    )
                }
            }
        }
    }

    private fun CircleImageView.highlightChosenCard(v: View) {
        borderWidth = 10
        val imageNumber = v.contentDescription.toString().toInt()
        borderColor = if (hiddenNumber == imageNumber) {
            getColor(R.color.rightAnswer)
        } else {
            getColor(R.color.wrongAnswer)
        }
    }

    private fun setState(state: GameState) {
        this.state = state
        when (state) {
            GameState.INIT -> {
                progressBar.progress = 0
                generatedNumbers = generateNumbers(CARDS_IN_ALL)
            }
            GameState.PROGRESS -> {
                progressBar.progress = 0
                hiddenNumber = hideNumber()
            }
            GameState.END -> {
                timer.cancel()
                runOnUiThread {
                    endLabel.text = "к следующей игре"
                    progressBarLabel.visibility = View.GONE
                    nextOrRepeatButton.visibility = VISIBLE
                    nextOrRepeatButton.setOnClickListener {
                        this.finish()
                    }
                }
            }
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
        timer.schedule(GameTimerTask(this), 1000, 500)
    }

    fun updateTimer() {
        runOnUiThread {
            when (state) {
                GameState.INIT -> {
                    progressBar.incrementProgressBy(10)
                    progressBarLabel.text = progressBar.progress.toString()

                    if (progressBar.progress == 100) {
                        setState(GameState.PROGRESS)
                    }
                }
                GameState.PROGRESS -> {
                    progressBar.incrementProgressBy(10)
                    progressBarLabel.text = progressBar.progress.toString()

                    if (progressBar.progress == 100) {
                        setState(GameState.END)
                    }
                }
                GameState.END -> {
                }
            }

        }
    }

    class GameTimerTask(private val activity: NumberGame) : TimerTask() {
        override fun run() {
            activity.updateTimer()
        }
    }
}