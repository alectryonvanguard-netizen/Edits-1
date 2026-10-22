
package com.nexa.ai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

private val DeepNavy = Color(0xFF050816)
private val NeonCyan = Color(0xFF00E5FF)
private val NeonPurple = Color(0xFF963BFF)
private val SoftWhite = Color(0xFFF4F7FF)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.statusBarColor = android.graphics.Color.rgb(5, 8, 22)
        window.navigationBarColor = android.graphics.Color.rgb(5, 8, 22)

        setContent {
            MaterialTheme {
                NexaApp()
            }
        }
    }
}

@Composable
fun NexaApp() {
    var booting by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        delay(2500)
        booting = false
    }

    if (booting) {
        NexaBootScreen()
    } else {
        NexaHomeScreen()
    }
}

@Composable
fun NexaBootScreen() {
    val animation = rememberInfiniteTransition(label = "nexaBoot")

    val glow by animation.animateFloat(
        initialValue = 0.55f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1100),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow"
    )

    val pulse by animation.animateFloat(
        initialValue = 0.96f,
        targetValue = 1.04f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepNavy),
        contentAlignment = Alignment.Center
    ) {
        Canvas(Modifier.fillMaxSize()) {
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        NeonPurple.copy(alpha = 0.22f * glow),
                        Color.Transparent
                    ),
                    center = Offset(size.width * 0.5f, size.height * 0.40f),
                    radius = size.width * 0.65f
                ),
                radius = size.width * 0.65f,
                center = Offset(size.width * 0.5f, size.height * 0.40f)
            )

            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        NeonCyan.copy(alpha = 0.12f * glow),
                        Color.Transparent
                    ),
                    center = Offset(size.width * 0.5f, size.height * 0.65f),
                    radius = size.width * 0.5f
                ),
                radius = size.width * 0.5f,
                center = Offset(size.width * 0.5f, size.height * 0.65f)
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(118.dp)
                    .scale(pulse)
                    .border(
                        width = 1.5.dp,
                        brush = Brush.linearGradient(
                            listOf(NeonCyan, NeonPurple, NeonCyan)
                        ),
                        shape = RoundedCornerShape(30.dp)
                    )
                    .background(
                        Color(0xFF0B1027),
                        RoundedCornerShape(30.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "N",
                    fontSize = 78.sp,
                    fontWeight = FontWeight.Black,
                    style = androidx.compose.ui.text.TextStyle(
                        brush = Brush.linearGradient(
                            listOf(NeonCyan, Color(0xFF478BFF), NeonPurple)
                        )
                    )
                )
            }

            Spacer(Modifier.height(26.dp))

            Text(
                text = "Nexa AI",
                fontSize = 34.sp,
                fontWeight = FontWeight.Bold,
                color = SoftWhite,
                letterSpacing = 0.5.sp
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = "Smarter Chats. Bigger Ideas.",
                fontSize = 14.sp,
                color = Color(0xFFB9C8E8)
            )

            Spacer(Modifier.height(38.dp))

            CircularProgressIndicator(
                modifier = Modifier
                    .size(32.dp)
                    .alpha(glow),
                color = NeonCyan,
                trackColor = NeonPurple.copy(alpha = 0.25f),
                strokeWidth = 3.dp
            )

            Spacer(Modifier.height(12.dp))

            Text(
                text = "Starting Nexa AI...",
                fontSize = 12.sp,
                color = Color(0xFF9AA9CC)
            )
        }
    }
}

@Composable
fun NexaHomeScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepNavy),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Nexa AI",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                style = androidx.compose.ui.text.TextStyle(
                    brush = Brush.horizontalGradient(
                        listOf(NeonCyan, NeonPurple)
                    )
                )
            )

            Spacer(Modifier.height(12.dp))

            Text(
                text = "Your AI journey starts here.",
                color = SoftWhite,
                fontSize = 15.sp
            )

            Spacer(Modifier.height(24.dp))

            Text(
                text = "Chat  •  Voice  •  Images",
                color = Color(0xFF9AA9CC),
                fontSize = 14.sp
            )
        }
    }
}
