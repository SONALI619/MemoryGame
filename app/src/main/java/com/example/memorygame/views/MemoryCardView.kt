package com.example.memorygame.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.memorygame.data.MemoryCard



@Composable
fun MemoryCardView(
    card: MemoryCard, width: Dp, height:Dp, onClick:() -> Unit
) {
    val isFront = card.isFaceUp || card.isMatched
    val gradient= Brush.verticalGradient(
        colors= if(isFront){
            listOf(Color(0xFF44444444),
                Color(0xF0000000))
        } else{
            listOf(Color(0xF007DAF6),
                Color(0xF007EECB)
            )
        }
    )
    Box(modifier = Modifier
            .size(width * 0.5f, height * 0.5f)
            .clip(RoundedCornerShape(12.dp))
            .background(brush = gradient)
            .clickable(enabled = !card.isFaceUp && !card.isMatched) { onClick() },
        contentAlignment = Alignment.Center){
        if(isFront) {
            Text(text = card.emoji, color = Color.White, fontSize = 26.sp)
        }else{
        Text(text="❓", color=Color.White.copy(alpha=0.5f), fontSize=20.sp)
        }

    }

}


