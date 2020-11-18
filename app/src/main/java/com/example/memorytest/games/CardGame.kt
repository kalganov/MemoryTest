package com.example.memorytest.games

import android.os.Bundle
import android.os.CountDownTimer
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.TableLayout
import android.widget.TableRow
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import com.example.memorytest.MainActivity
import com.example.memorytest.R
import de.hdodenhof.circleimageview.CircleImageView
import kotlin.random.Random.Default.nextInt


//TODO make it as a parameter
private const val CARDS_IN_ROW = 4
private const val CARDS_IN_ALL = 8 // онли четные числа
private const val MAX_NUMBER = 9

class CardGame : AppCompatActivity() {

    private lateinit var layout: TableLayout

    private lateinit var generatedNumbers: List<Int>

    private var timer: CountDownTimer? = null
    private var clickedNumber: CircleImageView? = null
    private var score: Int = 0;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        generate()
    }

    private fun generate(){
        setContentView(R.layout.cards_game)
        layout = findViewById(R.id.tableLayout)

        generateNumbers()
        addListeners()
    }

    private fun generateNumbers(): List<Int> {
        var generatedNumbers =  (0..MAX_NUMBER).shuffled().take(CARDS_IN_ALL / 2)
        generatedNumbers = (generatedNumbers + generatedNumbers).shuffled()
        setUpLayout(generatedNumbers)
        return generatedNumbers
    }

    private fun setUpLayout(numbers: List<Int>) {
        layout.removeAllViews()
        numbers.chunked(CARDS_IN_ROW).forEach {
            val tableRow = TableRow(this)
            it.forEach {
                val number = CircleImageView(this).apply {
                    setImageResource(R.drawable.ic_fill)
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


    private fun addListeners() {
        runOnUiThread {
            this@CardGame.layout.children.forEach { row ->
                (row as TableRow).children.forEach { image -> addListeners(image) }
            }
        }
    }

    private fun addListeners(img: View){
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

    private fun chooseCard(card1: CircleImageView) {
        var card2: CircleImageView? = clickedNumber

        timer?.cancel()
        timer?.onFinish()
        timer = null;

        card1.setOnTouchListener(null)
        card1.borderWidth = 10

        val imageNumber = card1.contentDescription.toString().toInt()
        card1.setImageResource(getNumberImageResource(imageNumber))
        val prevClick = card2?.contentDescription?.toString()?.toInt() ?: -1

        if (prevClick == -1) clickedNumber = card1
        else{
            if (prevClick == imageNumber) chooseRight(card1, card2!!)
            else chooseFalse(card1, card2!!)

            card1.borderWidth = 0
            card2.borderWidth = 0

            clickedNumber = null
        }
    }

    private fun chooseFalse(card1: CircleImageView, card2: CircleImageView){
        timer = object: CountDownTimer(1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {}

            override fun onFinish() {
                card1.setImageResource(R.drawable.ic_fill)
                card2.setImageResource(R.drawable.ic_fill)

                addListeners(card1)
                addListeners(card2)
            }
        }
        timer?.start()
    }

    private fun chooseRight(card1: CircleImageView, card2: CircleImageView){
        card1.setOnTouchListener(null)
        card2?.setOnTouchListener(null)
        score+=2;

        if (score >= CARDS_IN_ALL){
            setContentView(R.layout.success)

            var repeat = findViewById<Button>(R.id.repeat)
            repeat.setOnClickListener {generate()}

            var next = findViewById<Button>(R.id.next)
            next.setOnClickListener {finish()}
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