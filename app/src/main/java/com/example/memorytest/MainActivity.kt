package com.example.memorytest

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.memorytest.games.NumberGame


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



//        findViewById<ConstraintLayout>(R.id.content)
//            .viewTreeObserver.addOnGlobalFocusChangeListener { _, newFocus ->
//                if (newFocus is EditText) {
//                    newFocus.addTextChangedListener(object : TextWatcher {
//                        override fun beforeTextChanged(
//                            charSequence: CharSequence,
//                            i: Int,
//                            i1: Int,
//                            i2: Int
//                        ) {
//                        }
//
//                        override fun onTextChanged(
//                            charSequence: CharSequence,
//                            i: Int,
//                            i1: Int,
//                            i2: Int
//                        ) {
//                        }
//
//                        override fun afterTextChanged(editable: Editable) {
//                            Toast.makeText(mainActivity, editable.toString(), Toast.LENGTH_SHORT)
//                                .show();
//                        }
//                    })
//                }
//            }
    }

    fun startNumberGame(view: View) {
        val game = Intent(this, NumberGame::class.java)
        startActivity(game)
    }
}