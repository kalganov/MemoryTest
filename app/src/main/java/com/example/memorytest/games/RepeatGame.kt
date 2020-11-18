package com.example.memorytest.games

import android.os.Bundle
import android.os.CountDownTimer
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import com.example.memorytest.R
import de.hdodenhof.circleimageview.CircleImageView


//TODO make it as a parameter
private const val CARDS_IN_ROW = 3
private const val CARDS_IN_ALL = 9 // онли четные числа

private const val MIN_CARDS_LEN = 3
private const val MAX_CARDS_LEN = 5

private const val STEP_MILLIS = 1000L

private const val STEP_ERROR_MILLIS = 500L
private const val STEP_ERROR = 6


class RepeatGame : AppCompatActivity() {

    private lateinit var layout: TableLayout
    private lateinit var textView: TextView

    private lateinit var repeatedCards: List<CircleImageView>
    private val cards: MutableList<CircleImageView> = ArrayList()
    private lateinit var text: TextView
    private var timer: CountDownTimer? = null
    private var clickedNumber: CircleImageView? = null
    private var score: Int = 0
    private var step: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        generate()
    }

    private fun generate() {
        setContentView(R.layout.repeat_game)
        layout = findViewById(R.id.tableLayout)
        textView = findViewById(R.id.rememberAndRepeat)
        score = MIN_CARDS_LEN
        step = 0
        cards.clear()

        generateNumbers()
        showCards()
    }

    private fun generateNumbers() {
        setUpLayout()
        repeatedCards = cards.shuffled().take(MAX_CARDS_LEN)
    }

    private fun setUpLayout() {
        layout.removeAllViews()
        (1..CARDS_IN_ALL).chunked(CARDS_IN_ROW).forEach {
            val tableRow = TableRow(this)
            it.forEach {
                val number = CircleImageView(this).apply {
                    setImageResource(R.drawable.ic_fill)
                    contentDescription = it.toString()
                }

                cards.add(number)
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


    private fun addListeners() {
        runOnUiThread {
            this@RepeatGame.layout.children.forEach { row ->
                (row as TableRow).children.forEach { image -> addListeners(image) }
            }
        }
    }

    private fun addListeners(img: View) {
        img.setOnTouchListener { v, event ->
            if (v is CircleImageView) {
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        chooseCard(v)
                    }
                }
                v.performClick()
                true
            } else {
                false
            }
        }
    }

    private fun showCards() {
        textView.setText(R.string.remember)

        object : CountDownTimer(STEP_MILLIS, STEP_MILLIS){
            override fun onTick(millisUntilFinished: Long) {}
            
            override fun onFinish() {
                resetShow()
                startTimer()
            }

        }.start()
    }

    private fun startTimer(){
        timer = object : CountDownTimer(score * STEP_MILLIS, STEP_MILLIS) {
            override fun onTick(millisUntilFinished: Long) {
                showStep(repeatedCards[(((score * STEP_MILLIS) - millisUntilFinished) / STEP_MILLIS).toInt()])
            }

            override fun onFinish() {
                resetShow()
                addListeners()
                textView.setText(R.string.repeat)
            }
        }.start()
    }

    private fun showStep(card: CircleImageView) {
        card.borderWidth = 100
        card.borderColor = getColor(R.color.rightAnswer)
    }

    private fun nextStep(card: CircleImageView) {
        step++

        card.borderWidth = 100
        card.borderColor = getColor(R.color.rightAnswer)

        if (step >= score) {
            score++
            step = 0
            resetListeners()

            if (score > MAX_CARDS_LEN) showWin()
            else showCards()
        }
    }

    private fun showError() {
        setContentView(R.layout.fail)

        var repeat = findViewById<Button>(R.id.repeat)
        repeat.setOnClickListener { generate() }

        var next = findViewById<Button>(R.id.next)
        next.setOnClickListener { finish() }
    }

    private fun showWin() {
        setContentView(R.layout.success)

        var repeat = findViewById<Button>(R.id.repeat)
        repeat.setOnClickListener { generate() }

        var next = findViewById<Button>(R.id.next)
        next.setOnClickListener { finish() }
    }

    private fun resetShow() {
        cards.forEach { row -> row.borderWidth = 0 }
    }

    private fun resetListeners() {
        cards.forEach { row ->
            row.setOnTouchListener(null)
        }
    }

    private fun chooseCard(clicked: CircleImageView) {
        var card: CircleImageView? = repeatedCards.find { c -> clicked == c }

        if (card == null) showError()
        else {
            if (card == repeatedCards[step]) nextStep(card)
            else showError()
        }
    }

    private fun chooseRight(card1: CircleImageView, card2: CircleImageView) {
        card1.setOnTouchListener(null)
        card2.setOnTouchListener(null)
        score += 2

        if (score >= CARDS_IN_ALL) {
            setContentView(R.layout.success)

            var repeat = findViewById<Button>(R.id.repeat)
            repeat.setOnClickListener { generate() }

            var next = findViewById<Button>(R.id.next)
            next.setOnClickListener { finish() }
        }
    }

    //картинки храняться тут
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