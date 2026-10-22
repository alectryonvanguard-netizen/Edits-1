package com.nexa.ai

import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Color Palette from Design Blueprint
private val NexaBackground = Color(0xFF050816)
private val NexaSurface = Color(0xFF10172A)
private val NexaCyan = Color(0xFF00E5FF)
private val NexaBlue = Color(0xFF246BFD)
private val NexaPurple = Color(0xFF963BFF)
private val NexaMagenta = Color(0xFFFF35C8)
private val NexaText = Color(0xFFF4F7FF)
private val NexaMuted = Color(0xFF9AA9CC)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = android.graphics.Color.parseColor("#050816")
        window.navigationBarColor = android.graphics.Color.parseColor("#050816")

        setContent {
            MaterialTheme {
                NexaApp()
            }
        }
    }
}

@Composable
private fun NexaApp() {
    // Current route matching the 8 screens in your design
    var currentScreen by remember { mutableStateOf("Boot") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(NexaBackground)
    ) {
        when (currentScreen) {
            "Boot" -> BootScreen(onTimeout = { currentScreen = "Onboarding" })
            "Onboarding" -> OnboardingScreen(onGetStarted = { currentScreen = "Login" })
            "Login" -> LoginScreen(onLoginSuccess = { currentScreen = "Home" })
            "Home" -> HomeScreen(
                onNavigate = { route -> currentScreen = route }
            )
            "Chat" -> ChatScreen(onBack = { currentScreen = "Home" })
            "Voice" -> VoiceChatScreen(onBack = { currentScreen = "Home" })
            "ImageGen" -> ImageGenScreen(onBack = { currentScreen = "Home" })
            "History" -> HistoryScreen(onBack = { currentScreen = "Home" }, onSelectChat = { currentScreen = "Chat" })
            "Profile" -> ProfileScreen(onBack = { currentScreen = "Home" })
        }
    }
}

// -----------------------------------------------------------------------------
// SCREEN 1: BOOT SCREEN
// -----------------------------------------------------------------------------
@Composable
private fun BootScreen(onTimeout: () -> Unit) {
    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(2000) // Auto navigate after 2s
        onTimeout()
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(90.dp)
                .background(
                    brush = Brush.radialGradient(listOf(NexaCyan, NexaPurple)),
                    shape = RoundedCornerShape(24.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text("N", color = NexaText, fontSize = 48.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.height(16.dp))
        Text("Nexa AI", color = NexaText, fontSize = 28.sp, fontWeight = FontWeight.Bold)
        Text("Smarter Chats. Bigger Ideas.", color = NexaMuted, fontSize = 14.sp)
        Spacer(Modifier.height(40.dp))
        CircularProgressIndicator(color = NexaCyan)
        Spacer(Modifier.height(12.dp))
        Text("Starting up...", color = NexaMuted, fontSize = 12.sp)
    }
}

// -----------------------------------------------------------------------------
// SCREEN 2: WELCOME / ONBOARDING
// -----------------------------------------------------------------------------
@Composable
private fun OnboardingScreen(onGetStarted: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            Text(
                "Skip",
                color = NexaMuted,
                modifier = Modifier.clickable { onGetStarted() }
            )
        }
        
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            // Robot Avatar Placeholder
            Box(
                modifier = Modifier
                    .size(180.dp)
                    .background(
                        brush = Brush.linearGradient(listOf(NexaBlue, NexaPurple)),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.SmartToy, contentDescription = null, tint = NexaText, modifier = Modifier.size(90.dp))
            }
            Spacer(Modifier.height(32.dp))
            Text("Meet Nexa", color = NexaText, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            Text(
                "Your AI assistant for everyday\nquestions, ideas and creativity.",
                color = NexaMuted,
                fontSize = 14.sp,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }

        Button(
            onClick = onGetStarted,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(25.dp),
            colors = ButtonDefaults.buttonColors(containerColor = NexaBlue)
        ) {
            Text("Get Started", color = NexaText, fontWeight = FontWeight.Bold)
        }
    }
}

// -----------------------------------------------------------------------------
// SCREEN 3: LOGIN / SIGNUP
// -----------------------------------------------------------------------------
@Composable
private fun LoginScreen(onLoginSuccess: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Nexa AI", color = NexaText, fontSize = 28.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(32.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email or Username", color = NexaMuted) },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = NexaCyan,
                unfocusedBorderColor = NexaSurface,
                focusedTextColor = NexaText,
                unfocusedTextColor = NexaText
            )
        )
        Spacer(Modifier.height(12.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password", color = NexaMuted) },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = NexaCyan,
                unfocusedBorderColor = NexaSurface,
                focusedTextColor = NexaText,
                unfocusedTextColor = NexaText
            )
        )
        Spacer(Modifier.height(24.dp))

        Button(
            onClick = onLoginSuccess,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(25.dp),
            colors = ButtonDefaults.buttonColors(containerColor = NexaCyan)
        ) {
            Text("Log In", color = NexaBackground, fontWeight = FontWeight.Bold)
        }
    }
}

// -----------------------------------------------------------------------------
// SCREEN 4: HOME / CHAT OVERVIEW
// -----------------------------------------------------------------------------
@Composable
private fun HomeScreen(onNavigate: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Nexa AI", color = NexaText, fontSize = 22.sp, fontWeight = FontWeight.Bold)
            IconButton(onClick = { onNavigate("Profile") }) {
                Icon(Icons.Default.AccountCircle, contentDescription = "Profile", tint = NexaCyan)
            }
        }

        Spacer(Modifier.height(20.dp))
        Text("Good morning, Samuel", color = NexaText, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Text("What do you want to explore today?", color = NexaMuted, fontSize = 14.sp)

        Spacer(Modifier.height(24.dp))

        // Feature Grid Shortcuts
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            ShortcutCard("Chat", Icons.Default.Chat, NexaBlue, Modifier.weight(1f)) { onNavigate("Chat") }
            ShortcutCard("Voice", Icons.Default.Mic, NexaPurple, Modifier.weight(1f)) { onNavigate("Voice") }
            ShortcutCard("Image", Icons.Default.Image, NexaMagenta, Modifier.weight(1f)) { onNavigate("ImageGen") }
        }

        Spacer(Modifier.weight(1f))

        // Bottom Navigation Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(NexaSurface, RoundedCornerShape(25.dp))
                .padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            IconButton(onClick = { onNavigate("Home") }) { Icon(Icons.Default.Home, null, tint = NexaCyan) }
            IconButton(onClick = { onNavigate("Chat") }) { Icon(Icons.Default.Chat, null, tint = NexaMuted) }
            IconButton(onClick = { onNavigate("Voice") }) { Icon(Icons.Default.Mic, null, tint = NexaMuted) }
            IconButton(onClick = { onNavigate("History") }) { Icon(Icons.Default.History, null, tint = NexaMuted) }
            IconButton(onClick = { onNavigate("Profile") }) { Icon(Icons.Default.Person, null, tint = NexaMuted) }
        }
    }
}

@Composable
private fun ShortcutCard(title: String, icon: androidx.compose.ui.graphics.vector.ImageVector, color: Color, modifier: Modifier, onClick: () -> Unit) {
    Column(
        modifier = modifier
            .background(NexaSurface, RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(icon, contentDescription = title, tint = color, modifier = Modifier.size(28.dp))
        Spacer(Modifier.height(8.dp))
        Text(title, color = NexaText, fontSize = 12.sp, fontWeight = FontWeight.Bold)
    }
}

// Placeholder Composables for the rest of the screens
@Composable private fun ChatScreen(onBack: () -> Unit) { SimpleHeaderScreen("Chat Screen", onBack) }
@Composable private fun VoiceChatScreen(onBack: () -> Unit) { SimpleHeaderScreen("Voice Mode", onBack) }
@Composable private fun ImageGenScreen(onBack: () -> Unit) { SimpleHeaderScreen("Image Generation", onBack) }
@Composable private fun HistoryScreen(onBack: () -> Unit, onSelectChat: () -> Unit) { SimpleHeaderScreen("Chat History", onBack) }
@Composable private fun ProfileScreen(onBack: () -> Unit) { SimpleHeaderScreen("Profile & Settings", onBack) }

@Composable
private fun SimpleHeaderScreen(title: String, onBack: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().padding(20.dp)) {
        IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, null, tint = NexaText) }
        Text(title, color = NexaText, fontSize = 24.sp, fontWeight = FontWeight.Bold)
    }
}
