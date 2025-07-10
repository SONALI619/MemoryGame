package com.example.memorygame.views

import android.annotation.SuppressLint
import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.memorygame.data.MemoryCard
import kotlinx.coroutines.delay


@SuppressLint("ContextCast", "ContextCastToActivity")
@Composable
fun MemoryGameScreen() {
    val emojiList = listOf("😂", "😺", "☠️", "🙉", "♣️", "🎲")
    val context = LocalContext.current as? Activity

    var cards by remember { mutableStateOf(listOf<MemoryCard>()) }
    var selectIndices by remember { mutableStateOf(listOf<Int>()) }
    var score by remember { mutableStateOf(0) }
    var highScore by remember { mutableStateOf(0) }
    var timeLeft by remember { mutableStateOf(60) }
    var isRunning by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    var gameStarted by remember { mutableStateOf(false) }

    LaunchedEffect(isRunning) {
        if (isRunning) {
            while (timeLeft > 0) {
                delay(1000)
                timeLeft -= 1
            }
            isRunning = false
            cards = cards.map { it.copy(isFaceUp = false, isMatched = false) }
            if (score > highScore) highScore = score
            showDialog = true
        }
    }

    LaunchedEffect(selectIndices) {
        if (selectIndices.size == 2) {
            delay(600)
            val first = cards[selectIndices[0]]
            val second = cards[selectIndices[1]]

            cards = cards.map {
                when (it.id) {
                    first.id, second.id -> {
                        if (first.emoji == second.emoji && first.id != second.id) {
                            score += 10
                            it.copy(isMatched = true)
                        } else {
                            it.copy(isFaceUp = false)
                        }
                    }

                    else -> it
                }
            }
            selectIndices = listOf()
        }
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(12.dp)
    ) {
        val cardWidth = maxWidth / 3
        val cardHeight = maxHeight / 3

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            if (gameStarted) {
                // Timer and progress
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp)
                ) {
                    Card(
                        shape = RoundedCornerShape(8.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFF9E9E)),
                        modifier = Modifier
                            .padding(end = 12.dp)
                            .wrapContentWidth()
                    ) {
                        Text(
                            text = "${timeLeft}s",
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    LinearProgressIndicator(
                        progress = timeLeft / 60f,
                        modifier = Modifier
                            .weight(1f)
                            .height(10.dp)
                            .clip(RoundedCornerShape(5.dp)),
                        color = Color.Green,
                        trackColor = Color.DarkGray
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    userScrollEnabled = false,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    items(cards.size) { index ->
                        MemoryCardView(
                            card = cards[index],
                            width = cardWidth,
                            height = cardHeight
                        ) {
                            if (!cards[index].isFaceUp && !cards[index].isMatched && selectIndices.size < 2) {
                                cards = cards.map {
                                    if (it.id == cards[index].id) it.copy(isFaceUp = true)
                                    else it
                                }
                                selectIndices = selectIndices + index
                            }
                        }
                    }
                }
            } else {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(0.7f)
                            .height(60.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0xFF61C1D5))
                            .clickable {
                                cards = (emojiList + emojiList)
                                    .shuffled()
                                    .mapIndexed { index, emoji -> MemoryCard(index, emoji) }
                                selectIndices = listOf()
                                score = 0
                                timeLeft = 30
                                isRunning = true
                                showDialog = false
                                gameStarted = true
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "START GAME..!",
                            fontSize = 22.sp,
                            color = Color.White
                        )
                    }
                }
            }
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = {},
                confirmButton = {
                    TextButton(onClick = {
                        cards = (emojiList + emojiList)
                            .shuffled()
                            .mapIndexed { index, emoji -> MemoryCard(index, emoji) }
                        selectIndices = listOf()
                        score = 0
                        timeLeft = 30
                        isRunning = true
                        showDialog = false
                    }) {
                        Text("Play Again")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { context?.finish() }) {
                        Text("Exit")
                    }
                },
                title = { Text("TIME'S UP..!", color = Color.Black) },
                text = { Text("Your Score is: $score\nHigh Score: $highScore", color = Color.Black) },
                containerColor = Color.White
            )
        }
    }
}
