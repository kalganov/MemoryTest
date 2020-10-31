package com.example.memorytest.games

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.memorytest.R
import java.util.*

abstract class AbstractGame : AppCompatActivity() {

    private lateinit var state: GameState

    private lateinit var progressBar: ProgressBar
    private lateinit var progressBarLabel: TextView
    private lateinit var endLabel: TextView
    private lateinit var nextOrRepeatButton: Button

    private val timer = Timer()

    protected var speedOnInit: Int = 10
    protected var speedOnProgress: Int = 10

    protected abstract fun setUpInitState()
    protected abstract fun setUpProgressState()
    protected abstract fun setUpVariables()
    protected abstract fun setUpEndState()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setUpVariables()
        progressBar = findViewById(R.id.timer)
        progressBarLabel = findViewById(R.id.timerProgressLabel)
        endLabel = findViewById(R.id.endLabel)
        nextOrRepeatButton = findViewById(R.id.nextOrRepeat)

        changeState(GameState.INIT)
        timer.schedule(GameTimerTask(this), 1000, 500)
    }

    fun changeState(state: GameState) {
        this.state = state
        when (state) {
            GameState.INIT -> {
                progressBar.progress = 0
                setUpInitState()
            }
            GameState.PROGRESS -> {
                progressBar.progress = 0
                setUpProgressState()
            }
            GameState.END -> {
                setUpEndState()
                timer.cancel()
                runOnUiThread {
                    endLabel.text = "к следующей игре"
                    progressBarLabel.visibility = View.GONE
                    nextOrRepeatButton.visibility = View.VISIBLE
                    nextOrRepeatButton.setOnClickListener {
                        this.finish()
                    }
                }
            }
        }
    }

    fun updateTimer() {
        runOnUiThread {
            when (state) {
                GameState.INIT -> {
                    progressBar.incrementProgressBy(speedOnInit)
                    progressBarLabel.text = progressBar.progress.toString()

                    if (progressBar.progress >= 100) {
                        changeState(GameState.PROGRESS)
                    }
                }
                GameState.PROGRESS -> {
                    progressBar.incrementProgressBy(speedOnProgress)
                    progressBarLabel.text = progressBar.progress.toString()

                    if (progressBar.progress >= 100) {
                        changeState(GameState.END)
                    }
                }
                GameState.END -> {
                }
            }

        }
    }

    class GameTimerTask(private val activity: AbstractGame) : TimerTask() {
        override fun run() {
            activity.updateTimer()
        }
    }
}