package com.example.memorytest.games

import android.annotation.SuppressLint
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import android.widget.TextView
import com.android.volley.toolbox.Volley
import com.example.memorytest.R
import com.example.memorytest.controller.DesktopServerController
import com.example.memorytest.dto.UserInput
import com.example.memorytest.text.MotoricTextWatcher
import kotlin.random.Random.Default.nextInt

class TextGame : AbstractGame() {

    private lateinit var text: TextView
    private lateinit var inputText: EditText
    private lateinit var controller: DesktopServerController
    private lateinit var motoricTextWatcher: MotoricTextWatcher

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        speedOnProgress = 1
        controller = DesktopServerController(Volley.newRequestQueue(this))
        motoricTextWatcher = MotoricTextWatcher(this, text.text.toString())
        inputText.addTextChangedListener(motoricTextWatcher)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
    }

    override fun setUpVariables() {
        setContentView(R.layout.text_game)
        text = findViewById(R.id.text)
        inputText = findViewById(R.id.inputText)
    }

    override fun setUpInitState() {
        runOnUiThread { text.text = generateText() }
    }

    //TODO generate normal text
    private fun generateText(): CharSequence? {
        return when (nextInt(0, 8)) {
            0 -> "the type integer is the common choice"
            1 -> "because it relies on compiler support for eight-byte integers"
            2 -> "on such machines"
            3 -> "my sister is 20 years old and she works at the city Zoo"
            4 -> "I like reading. I read every day and everywhere"
            5 -> "Every time she buys a new skirt, or a blouse, or a dress"
            6 -> "I am in Paris now. I like it very much. It is big and very interesting."
            7 -> "They are very beautiful. Yesterday we visited a lot of galleries and museums"
            8 -> "He usually reads a paper in the morning."
            else -> "else"
        }
    }

    override fun setUpProgressState() {
        runOnUiThread {
            //TODO UNCOMMENT IT FURTHER
//            text.visibility = View.GONE
            inputText.visibility = View.VISIBLE
        }
    }

    @SuppressLint("HardwareIds")
    override fun setUpEndState() {
        runOnUiThread {
            if (motoricTextWatcher.isSomethingInputted()) {
                val userId =
                    Settings.Secure.getString(this.contentResolver, Settings.Secure.ANDROID_ID)
                controller.postText(UserInput(
                    motoricTextWatcher.inputtedText,
                    userId,
                    motoricTextWatcher.symbolsToSpeed.toList()
                ))
            }
            text.visibility = View.GONE
            inputText.visibility = View.VISIBLE
            inputText.isEnabled = false
        }
    }
}