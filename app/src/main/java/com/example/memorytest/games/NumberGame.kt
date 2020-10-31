package com.example.memorytest.games

import android.view.Gravity
import android.view.MotionEvent
import android.widget.TableLayout
import android.widget.TableRow
import androidx.core.view.children
import com.example.memorytest.R
import de.hdodenhof.circleimageview.CircleImageView
import kotlin.random.Random.Default.nextInt


//TODO make it as a parameter
private const val CARDS_IN_ROW = 4
private const val CARDS_IN_ALL = 8

class NumberGame : AbstractGame() {

    private lateinit var layout: TableLayout

    private lateinit var generatedNumbers: List<Int>

    private var hiddenNumber: Int = -1

    override fun setUpVariables() {
        setContentView(R.layout.number_game)
        layout = findViewById(R.id.tableLayout)
    }

    override fun setUpInitState() {
        generatedNumbers = generateNumbers(CARDS_IN_ALL)
    }

    override fun setUpProgressState() {
        hiddenNumber = hideNumber()
        addListeners()
    }

    override fun setUpEndState() {
        disableCards()
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

    private fun addListeners() {
        runOnUiThread {
            this@NumberGame.layout.children.forEach { row ->
                (row as TableRow).children.forEach { image ->
                    image.setOnTouchListener { v, event ->
                        if (v is CircleImageView) {
                            when (event.action) {
                                MotionEvent.ACTION_DOWN -> {
                                    highlightChosenCard(v)
                                    changeState(GameState.END)
                                }
                            }
                            v.performClick()
                            true
                        } else {
                            false
                        }
                    }
                }
            }
        }
    }

    private fun highlightChosenCard(card: CircleImageView) {
        card.borderWidth = 10
        val imageNumber = card.contentDescription.toString().toInt()
        card.borderColor = if (hiddenNumber == imageNumber) {
            getColor(R.color.rightAnswer)
        } else {
            getColor(R.color.wrongAnswer)
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
}