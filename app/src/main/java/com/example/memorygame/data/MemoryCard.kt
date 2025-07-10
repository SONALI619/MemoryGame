package com.example.memorygame.data



data class MemoryCard(

        val id: Int,
        val emoji: String,
        val isFaceUp: Boolean = false,
        val isMatched: Boolean = false

)


