package com.nexa.ai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// -----------------------------------------------------------------------------
// STRICT COLOR PALETTE (NO PURPLE: ONLY BLUE, ORANGE, RED, GREEN, WHITE)
// -----------------------------------------------------------------------------
private val NexaBlack = Color(0xFF000000)          // Pure black for Home background
private val NexaBrightBlack = Color(0xFF141923)     // Bright black for Cards & Chat background
private val NexaWhite = Color(0xFFFFFFFF)           // White text & icons
private val NexaBlue = Color(0xFF246BFD)            // Primary blue accents & buttons
private val NexaOrange = Color(0xFFFF8A00)          // Secondary orange accents
private val NexaRed = Color(0xFFFF2020)             // Red accents & End buttons
private val NexaGreen = Color(0xFF00C853)           // Success & Active states
private val NexaGray = Color(0xFF8A95A5)            // Muted text color

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.statusBarColor = android.graphics.Color.BLACK
        window.navigationBarColor = android.graphics.Color.BLACK

        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .windowInsetsPadding(WindowInsets.systemBars),
                    color = NexaBlack
                ) {
                    NexaApp()
                }
            }
        }
    }
}

@Composable
private fun NexaApp() {
    var currentScreen by remember { mutableStateOf("Boot") }
    var userName by remember { mutableStateOf("Samuel Ngethe") }
    var userHandle by remember { mutableStateOf("@samuel_ng") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(NexaBlack)
    ) {
        when (currentScreen) {
            "Boot" -> BootScreen(onTimeout = { currentScreen = "Onboarding" })
            "Onboarding" -> OnboardingScreen(
                onGetStarted = { currentScreen = "Login" },
                onSkip = { currentScreen = "Home" }
            )
            "Login" -> LoginScreen(
                onLoginSuccess = { name ->
                    if (name.isNotBlank()) userName = name
                    currentScreen = "Home"
                },
                onSkip = { currentScreen = "Home" }
            )
            "Home" -> HomeScreen(userName = userName, onNavigate = { route -> currentScreen = route })
            "Chat" -> ChatScreen(onBack = { currentScreen = "Home" })
            "Voice" -> VoiceChatScreen(onBack = { currentScreen = "Home" })
            "ImageGen" -> ImageGenScreen(onBack = { currentScreen = "Home" })
            "History" -> HistoryScreen(onBack = { currentScreen = "Home" }, onSelectChat = { currentScreen = "Chat" })
            "Profile" -> ProfileScreen(
                userName = userName,
                userHandle = userHandle,
                onBack = { currentScreen = "Home" }
            )
        }
    }
}

// -----------------------------------------------------------------------------
// 1. BOOT SCREEN
// -----------------------------------------------------------------------------
@Composable
private fun BootScreen(onTimeout: () -> Unit) {
    LaunchedEffect(Unit) {
        delay(2000)
        onTimeout()
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .background(
                    brush = Brush.linearGradient(listOf(NexaBlue, NexaOrange)),
                    shape = RoundedCornerShape(28.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text("N", color = NexaWhite, fontSize = 56.sp, fontWeight = FontWeight.Black)
        }
        Spacer(Modifier.height(20.dp))
        Text("Nexa AI", color = NexaWhite, fontSize = 32.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(4.dp))
        Text("Smarter Chats. Bigger Ideas.", color = NexaGray, fontSize = 14.sp)
        Spacer(Modifier.height(60.dp))
        CircularProgressIndicator(color = NexaBlue, modifier = Modifier.size(36.dp))
        Spacer(Modifier.height(16.dp))
        Text("Starting up...", color = NexaGray, fontSize = 13.sp)
    }
}

// -----------------------------------------------------------------------------
// 2. WELCOME / ONBOARDING SCREEN (Matches screenshot 1: Headset avatar, skip, dots)
// -----------------------------------------------------------------------------
@Composable
private fun OnboardingScreen(onGetStarted: () -> Unit, onSkip: () -> Unit) {
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
                color = NexaWhite,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .clickable { onSkip() }
                    .padding(4.dp)
            )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(200.dp)
                    .background(
                        brush = Brush.radialGradient(listOf(NexaBlue, NexaBrightBlack, Color.Transparent)),
                        shape = CircleShape
                    )
                    .border(2.dp, NexaBlue, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Headset,
                    contentDescription = null,
                    tint = NexaBlue,
                    modifier = Modifier.size(100.dp)
                )
            }
            Spacer(Modifier.height(36.dp))
            Text("Meet Nexa", color = NexaWhite, fontSize = 28.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(12.dp))
            Text(
                "Your AI assistant for everyday\nquestions, ideas and creativity.",
                color = NexaGray,
                fontSize = 15.sp,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(24.dp))
            // Indicator dots
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Box(modifier = Modifier.size(8.dp).background(NexaBlue, CircleShape))
                Box(modifier = Modifier.size(8.dp).background(NexaBrightBlack, CircleShape))
                Box(modifier = Modifier.size(8.dp).background(NexaBrightBlack, CircleShape))
                Box(modifier = Modifier.size(8.dp).background(NexaBrightBlack, CircleShape))
                Box(modifier = Modifier.size(8.dp).background(NexaBrightBlack, CircleShape))
            }
        }

        Button(
            onClick = onGetStarted,
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
                .background(
                    brush = Brush.horizontalGradient(listOf(NexaBlue, NexaOrange)),
                    shape = RoundedCornerShape(27.dp)
                ),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
        ) {
            Text("Get Started", color = NexaWhite, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
    }
}

// -----------------------------------------------------------------------------
// 3. LOGIN / SIGNUP SCREEN (Fully functional tabs, inputs, and action buttons)
// -----------------------------------------------------------------------------
@Composable
private fun LoginScreen(onLoginSuccess: (String) -> Unit, onSkip: () -> Unit) {
    var isLoginTab by remember { mutableStateOf(true) }
    var emailOrUser by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            TextButton(onClick = onSkip) {
                Text("Skip to Home", color = NexaOrange, fontSize = 13.sp)
            }
        }

        Box(
            modifier = Modifier
                .size(60.dp)
                .background(Brush.linearGradient(listOf(NexaBlue, NexaOrange)), RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text("N", color = NexaWhite, fontSize = 32.sp, fontWeight = FontWeight.Black)
        }
        Spacer(Modifier.height(10.dp))
        Text("Nexa AI", color = NexaWhite, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(24.dp))

        // Working Log In / Sign Up Segmented Tabs
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(NexaBrightBlack, RoundedCornerShape(25.dp))
                .padding(4.dp)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .background(if (isLoginTab) NexaBlue else Color.Transparent, RoundedCornerShape(20.dp))
                    .clickable { isLoginTab = true }
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) { Text("Log In", color = NexaWhite, fontWeight = FontWeight.Bold) }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .background(if (!isLoginTab) NexaBlue else Color.Transparent, RoundedCornerShape(20.dp))
                    .clickable { isLoginTab = false }
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) { Text("Sign Up", color = NexaWhite, fontWeight = FontWeight.Bold) }
        }

        Spacer(Modifier.height(20.dp))

        OutlinedTextField(
            value = emailOrUser,
            onValueChange = { emailOrUser = it },
            placeholder = { Text("Enter your email or username", color = NexaGray) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = NexaBrightBlack,
                unfocusedContainerColor = NexaBrightBlack,
                focusedBorderColor = NexaBlue,
                unfocusedBorderColor = Color.Transparent,
                focusedTextColor = NexaWhite,
                unfocusedTextColor = NexaWhite
            )
        )
        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            placeholder = { Text("Enter your password", color = NexaGray) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = null,
                        tint = NexaGray
                    )
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = NexaBrightBlack,
                unfocusedContainerColor = NexaBrightBlack,
                focusedBorderColor = NexaBlue,
                unfocusedBorderColor = Color.Transparent,
                focusedTextColor = NexaWhite,
                unfocusedTextColor = NexaWhite
            )
        )

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            TextButton(onClick = {}) {
                Text("Forgot password?", color = NexaBlue, fontSize = 12.sp)
            }
        }

        Spacer(Modifier.height(10.dp))

        Button(
            onClick = { onLoginSuccess(emailOrUser) },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(25.dp),
            colors = ButtonDefaults.buttonColors(containerColor = NexaBlue)
        ) {
            Text(if (isLoginTab) "Log In" else "Create Account", color = NexaWhite, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(Modifier.height(16.dp))
        Text("or", color = NexaGray, fontSize = 12.sp)
        Spacer(Modifier.height(16.dp))

        OutlinedButton(
            onClick = { onLoginSuccess("Google User") },
            modifier = Modifier.fillMaxWidth().height(48.dp),
            shape = RoundedCornerShape(24.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, NexaGray)
        ) {
            Text("Continue with Google", color = NexaWhite)
        }
        Spacer(Modifier.height(10.dp))
        OutlinedButton(
            onClick = { onLoginSuccess("GitHub User") },
            modifier = Modifier.fillMaxWidth().height(48.dp),
            shape = RoundedCornerShape(24.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, NexaGray)
        ) {
            Text("Continue with GitHub", color = NexaWhite)
        }
    }
}

// -----------------------------------------------------------------------------
// 4. HOME SCREEN (Pure black background, bright black cards)
// -----------------------------------------------------------------------------
@Composable
private fun HomeScreen(userName: String, onNavigate: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NexaBlack)
            .padding(20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(Brush.linearGradient(listOf(NexaBlue, NexaOrange)), RoundedCornerShape(10.dp)),
                    contentAlignment = Alignment.Center
                ) { Text("N", color = NexaWhite, fontWeight = FontWeight.Bold) }
                Spacer(Modifier.width(10.dp))
                Text("Nexa AI", color = NexaWhite, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }
            IconButton(onClick = { onNavigate("Profile") }) {
                Icon(Icons.Default.AccountCircle, contentDescription = null, tint = NexaBlue)
            }
        }

        Spacer(Modifier.height(16.dp))
        Text("Good morning, $userName", color = NexaWhite, fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Text("What do you want to explore today?", color = NexaGray, fontSize = 13.sp)

        Spacer(Modifier.height(20.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            ShortcutCard("Chat", "Ask anything", Icons.Default.Chat, NexaBlue, Modifier.weight(1f)) { onNavigate("Chat") }
            ShortcutCard("Voice", "Speak", Icons.Default.Mic, NexaOrange, Modifier.weight(1f)) { onNavigate("Voice") }
            ShortcutCard("Image", "Upload / Create", Icons.Default.Image, NexaGreen, Modifier.weight(1f)) { onNavigate("ImageGen") }
        }

        Spacer(Modifier.height(20.dp))
        Text("Quick Prompts", color = NexaWhite, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        Spacer(Modifier.height(10.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.weight(1f)
        ) {
            item { PromptRow("Explain something", Icons.Default.Help) { onNavigate("Chat") } }
            item { PromptRow("Write a message", Icons.Default.Edit) { onNavigate("Chat") } }
            item { PromptRow("Create an image", Icons.Default.Palette) { onNavigate("ImageGen") } }
            item { PromptRow("Help me code", Icons.Default.Code) { onNavigate("Chat") } }
        }

        Spacer(Modifier.height(10.dp))
        NexaBottomBar(selected = "Home", onNavigate = onNavigate)
    }
}

@Composable
private fun ShortcutCard(title: String, subtitle: String, icon: androidx.compose.ui.graphics.vector.ImageVector, color: Color, modifier: Modifier, onClick: () -> Unit) {
    Column(
        modifier = modifier
            .background(NexaBrightBlack, RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .padding(14.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Box(
            modifier = Modifier.size(36.dp).background(color.copy(alpha = 0.2f), RoundedCornerShape(10.dp)),
            contentAlignment = Alignment.Center
        ) { Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(20.dp)) }
        Spacer(Modifier.height(10.dp))
        Text(title, color = NexaWhite, fontSize = 14.sp, fontWeight = FontWeight.Bold)
        Text(subtitle, color = NexaGray, fontSize = 10.sp)
    }
}

@Composable
private fun PromptRow(title: String, icon: androidx.compose.ui.graphics.vector.ImageVector, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(NexaBrightBlack, RoundedCornerShape(14.dp))
            .clickable(onClick = onClick)
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, tint = NexaBlue, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(12.dp))
            Text(title, color = NexaWhite, fontSize = 14.sp)
        }
        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = NexaGray)
    }
}

// -----------------------------------------------------------------------------
// 5. CHAT SCREEN (Bright black background, mic icon, sticky input)
// -----------------------------------------------------------------------------
@Composable
private fun ChatScreen(onBack: () -> Unit) {
    var messageText by remember { mutableStateOf("") }
    val chatMessages = remember {
        mutableStateListOf(
            "User: Explain how photosynthesis works in simple terms." to true,
            "Nexa AI: Photosynthesis is the process by which plants use sunlight to turn carbon dioxide and water into glucose and oxygen." to false
        )
    }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NexaBrightBlack)
            .padding(16.dp)
            .imePadding()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, contentDescription = null, tint = NexaWhite) }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Nexa AI", color = NexaWhite, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text("Online", color = NexaGreen, fontSize = 11.sp)
            }
            Spacer(Modifier.width(24.dp))
        }

        Spacer(Modifier.height(12.dp))

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(chatMessages) { (msg, isUser) ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
                ) {
                    Box(
                        modifier = Modifier
                            .background(if (isUser) NexaBlue else Color(0xFF1F293D), RoundedCornerShape(16.dp))
                            .padding(12.dp)
                    ) {
                        Text(msg, color = NexaWhite, fontSize = 13.sp)
                    }
                }
            }
        }

        Spacer(Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(NexaBlack, RoundedCornerShape(25.dp))
                .padding(horizontal = 14.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Mic, contentDescription = "Voice input", tint = NexaBlue)
            Spacer(Modifier.width(8.dp))
            OutlinedTextField(
                value = messageText,
                onValueChange = { messageText = it },
                placeholder = { Text("Message Nexa AI...", color = NexaGray, fontSize = 13.sp) },
                modifier = Modifier.weight(1f),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    focusedTextColor = NexaWhite,
                    unfocusedTextColor = NexaWhite
                )
            )
            IconButton(
                onClick = {
                    if (messageText.isNotBlank()) {
                        val currentQuery = messageText
                        chatMessages.add("User: $currentQuery" to true)
                        messageText = ""
                        scope.launch {
                            delay(600)
                            chatMessages.add("Nexa AI: Response received for '$currentQuery'. Ready for Gemini API." to false)
                        }
                    }
                },
                modifier = Modifier.size(36.dp).background(NexaBlue, CircleShape)
            ) {
                Icon(Icons.Default.Send, contentDescription = "Send", tint = NexaWhite, modifier = Modifier.size(16.dp))
            }
        }
    }
}

// -----------------------------------------------------------------------------
// 6. VOICE MODE SCREEN
// -----------------------------------------------------------------------------
@Composable
private fun VoiceChatScreen(onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NexaBlack)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, contentDescription = null, tint = NexaWhite) }
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(160.dp)
                    .background(
                        brush = Brush.radialGradient(listOf(NexaBlue, NexaBrightBlack, Color.Transparent)),
                        shape = CircleShape
                    )
                    .border(3.dp, NexaBlue, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Mic, contentDescription = null, tint = NexaWhite, modifier = Modifier.size(60.dp))
            }
            Spacer(Modifier.height(32.dp))
            Text("Tap to speak", color = NexaWhite, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            Text("Voice chat active and listening.", color = NexaGray, fontSize = 13.sp)
        }

        Button(
            onClick = onBack,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(25.dp),
            colors = ButtonDefaults.buttonColors(containerColor = NexaRed)
        ) {
            Text("End", color = NexaWhite, fontWeight = FontWeight.Bold)
        }
    }
}

// -----------------------------------------------------------------------------
// 7. IMAGE GENERATION SCREEN
// -----------------------------------------------------------------------------
@Composable
private fun ImageGenScreen(onBack: () -> Unit) {
    var prompt by remember { mutableStateOf("A futuristic city at night with neon lights") }
    var selectedStyle by remember { mutableStateOf("Realistic") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NexaBlack)
            .padding(20.dp)
            .imePadding()
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, contentDescription = null, tint = NexaWhite) }
            Text("Create Image", color = NexaWhite, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = prompt,
            onValueChange = { prompt = it },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = NexaBrightBlack,
                unfocusedContainerColor = NexaBrightBlack,
                focusedBorderColor = NexaBlue,
                unfocusedBorderColor = Color.Transparent,
                focusedTextColor = NexaWhite,
                unfocusedTextColor = NexaWhite
            )
        )

        Spacer(Modifier.height(12.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("Realistic", "Anime", "Artistic").forEach { style ->
                FilterChip(
                    selected = selectedStyle == style,
                    onClick = { selectedStyle = style },
                    label = { Text(style, color = NexaWhite) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = NexaBlue,
                        containerColor = NexaBrightBlack
                    )
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = {},
            modifier = Modifier.fillMaxWidth().height(48.dp),
            shape = RoundedCornerShape(24.dp),
            colors = ButtonDefaults.buttonColors(containerColor = NexaOrange)
        ) { Text("Generate", color = NexaWhite, fontWeight = FontWeight.Bold) }

        Spacer(Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(NexaBrightBlack, RoundedCornerShape(20.dp))
                .border(1.dp, NexaGray.copy(alpha = 0.3f), RoundedCornerShape(20.dp)),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(Icons.Default.Image, contentDescription = null, tint = NexaBlue, modifier = Modifier.size(48.dp))
                Spacer(Modifier.height(8.dp))
                Text("Generated preview canvas", color = NexaGray, fontSize = 12.sp)
            }
        }
    }
}

// -----------------------------------------------------------------------------
// 8. HISTORY & PROFILE SCREENS
// -----------------------------------------------------------------------------
@Composable
private fun HistoryScreen(onBack: () -> Unit, onSelectChat: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NexaBlack)
            .padding(20.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, contentDescription = null, tint = NexaWhite) }
            Text("Chat History", color = NexaWhite, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(Modifier.height(16.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(
                listOf(
                    "Photosynthesis explanation" to "9:41 AM",
                    "Image generation" to "Yesterday",
                    "Study help" to "Yesterday",
                    "Coding assistance" to "2 days ago"
                )
            ) { item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(NexaBrightBlack, RoundedCornerShape(14.dp))
                        .clickable { onSelectChat() }
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(item.first, color = NexaWhite, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text("Tap to open conversation...", color = NexaGray, fontSize = 11.sp)
                    }
                    Text(item.second, color = NexaGray, fontSize = 11.sp)
                }
            }
        }
    }
}

@Composable
private fun ProfileScreen(
    userName: String,
    userHandle: String,
    onBack: () -> Unit
) {
    var darkMode by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NexaBlack)
            .padding(20.dp)
            .imePadding()
    ) {
        IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, contentDescription = null, tint = NexaWhite) }

        Spacer(Modifier.height(8.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(Brush.linearGradient(listOf(NexaBlue, NexaOrange)), CircleShape),
                contentAlignment = Alignment.Center
            ) { Icon(Icons.Default.Person, contentDescription = null, tint = NexaWhite, modifier = Modifier.size(48.dp)) }
            Spacer(Modifier.height(12.dp))
            Text(userName, color = NexaWhite, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text(userHandle, color = NexaGray, fontSize = 12.sp)
            Spacer(Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .background(NexaBlue, RoundedCornerShape(12.dp))
                    .padding(horizontal = 12.dp, vertical = 4.dp)
            ) { Text("Free Plan", color = NexaWhite, fontSize = 11.sp, fontWeight = FontWeight.Bold) }
        }

        Spacer(Modifier.height(24.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            item { SettingsRow(Icons.Default.Lock, "Account & Security") }
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(NexaBrightBlack, RoundedCornerShape(14.dp))
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Palette, contentDescription = null, tint = NexaBlue)
                        Spacer(Modifier.width(12.dp))
                        Text("Appearance", color = NexaWhite)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Dark Mode", color = NexaGray, fontSize = 12.sp)
                        Spacer(Modifier.width(8.dp))
                        Switch(
                            checked = darkMode,
                            onCheckedChange = { darkMode = it },
                            colors = SwitchDefaults.colors(checkedThumbColor = NexaBlue)
                        )
                    }
                }
            }
            item { SettingsRow(Icons.Default.Notifications, "Notifications") }
            item { SettingsRow(Icons.Default.Language, "Language") }
            item { SettingsRow(Icons.Default.Chat, "Chat Settings") }
            item { SettingsRow(Icons.Default.Shield, "Privacy & Data") }
            item { SettingsRow(Icons.Default.Info, "About Nexa AI") }
        }
    }
}

@Composable
private fun SettingsRow(icon: androidx.compose.ui.graphics.vector.ImageVector, title: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(NexaBrightBlack, RoundedCornerShape(14.dp))
            .clickable {}
            .padding(14.dp),
        horizontalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, tint = NexaBlue)
            Spacer(Modifier.width(12.dp))
            Text(title, color = NexaWhite)
        }
        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = NexaGray)
    }
}

@Composable
private fun NexaBottomBar(selected: String, onNavigate: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(NexaBrightBlack, RoundedCornerShape(25.dp))
            .padding(vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        IconButton(onClick = { onNavigate("Home") }) {
            Icon(Icons.Default.Home, contentDescription = null, tint = if (selected == "Home") NexaBlue else NexaGray)
        }
        IconButton(onClick = { onNavigate("Chat") }) {
            Icon(Icons.Default.Chat, contentDescription = null, tint = if (selected == "Chat") NexaBlue else NexaGray)
        }
        IconButton(onClick = { onNavigate("Voice") }) {
            Icon(Icons.Default.Mic, contentDescription = null, tint = if (selected == "Voice") NexaBlue else NexaGray)
        }
        IconButton(onClick = { onNavigate("History") }) {
            Icon(Icons.Default.History, contentDescription = null, tint = if (selected == "History") NexaBlue else NexaGray)
        }
        IconButton(onClick = { onNavigate("Profile") }) {
            Icon(Icons.Default.Person, contentDescription = null, tint = if (selected == "Profile") NexaBlue else NexaGray)
        }
    }
}
