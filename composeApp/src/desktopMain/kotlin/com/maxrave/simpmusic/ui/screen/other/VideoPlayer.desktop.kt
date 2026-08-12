package com.maxrave.simpmusic.ui.screen.other

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

@Composable
actual fun DynamicBackground() {
    // Yahan PC ke liye Premium Dark Purple Gradient set kiya hai taaki error na aaye
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.radialGradient(
                    colors = listOf(Color(0xFF2D1B4E), Color(0xFF07030C))
                )
            )
    )
}
