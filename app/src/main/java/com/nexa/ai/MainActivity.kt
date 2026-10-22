
package com.nexa.ai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

private val NexaBackground = Color(0xFF050816)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.statusBarColor = android.graphics.Color.rgb(5, 8, 22)
        window.navigationBarColor = android.graphics.Color.rgb(5, 8, 22)

        setContent {
            NexaApp()
        }
    }
}

@Composable
private fun NexaApp() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(NexaBackground)
    ) {
        ChatScreen()
    }
}
