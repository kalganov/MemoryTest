package com.example.memorytest.dto


data class UserInput(
        val text: String,
        val userId: String,
        val symbolsToSpeed: List<Pair<Pair<Int, Int>, Long>>
)