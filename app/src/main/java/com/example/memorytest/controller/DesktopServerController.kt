package com.example.memorytest.controller

import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.example.memorytest.dto.UserInput
import com.google.gson.Gson
import org.json.JSONObject

class DesktopServerController(private val requestQueue: RequestQueue) {

    private val serverUrl = "http://26f4791df118.ngrok.io/text"

    fun postText(userInput: UserInput) {
        Log.i(this.javaClass.name, "Send user input $userInput")
        requestQueue.add(
            JsonObjectRequest(
                Request.Method.POST, serverUrl, JSONObject(Gson().toJson(userInput)),
                Response.Listener<JSONObject> { response ->
                    Log.i("http response", response.toString())
                },
                Response.ErrorListener { e -> Log.e("http error", e.toString()) })
        )
    }
}