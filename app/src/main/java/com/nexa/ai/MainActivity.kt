package com.nexa.ai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

// -----------------------------------------------------------------------------
// EXACT COLOR SYSTEM FROM BLUEPRINT
// -----------------------------------------------------------------------------
private val NexaBackground = Color(0xFF050816)
private val NexaSurface = Color(0xFF10172A)
private val NexaCyan = Color(0xFF00E5FF)
private val NexaBlue = Color(0xFF246BFD)
private val NexaPurple = Color(0xFF963BFF)
private val NexaMagenta = Color(0xFFFF35C8)
private val NexaText = Color(0xFFF4F7FF)
private val NexaMuted = Color(0xFF9AA9CC)
private val NexaBubbleUser = Color(0xFF246BFD)
private val NexaBubbleAI = Color(0xFF162038)

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
            "Home" -> HomeScreen(onNavigate = { route -> currentScreen = route })
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
        delay(2000)
        onTimeout()
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .background(
                    brush = Brush.linearGradient(listOf(NexaCyan, NexaPurple, NexaMagenta)),
                    shape = RoundedCornerShape(28.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text("N", color = NexaText, fontSize = 56.sp, fontWeight = FontWeight.Black)
        }
        Spacer(Modifier.height(20.dp))
        Text("Nexa AI", color = NexaText, fontSize = 32.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(4.dp))
        Text("Smarter Chats. Bigger Ideas.", color = NexaMuted, fontSize = 14.sp)
        Spacer(Modifier.height(60.dp))
        CircularProgressIndicator(color = NexaCyan, modifier = Modifier.size(36.dp))
        Spacer(Modifier.height(16.dp))
        Text("Starting up...", color = NexaMuted, fontSize = 13.sp)
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
                fontSize = 14.sp,
                modifier = Modifier.clickable { onGetStarted() }
            )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(200.dp)
                    .background(
                        brush = Brush.radialGradient(listOf(NexaBlue, NexaPurple, Color.Transparent)),
                        shape = CircleShape
                    )
                    .border(2.dp, NexaCyan, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Headset,
                    contentDescription = null,
                    tint = NexaCyan,
                    modifier = Modifier.size(100.dp)
                )
            }
            Spacer(Modifier.height(40.dp))
            Text("Meet Nexa", color = NexaText, fontSize = 28.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(12.dp))
            Text(
                "Your AI assistant for everyday\nquestions, ideas and creativity.",
                color = NexaMuted,
                fontSize = 15.sp,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(24.dp))
            // Page Indicator Dots
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Box(modifier = Modifier.size(8.dp).background(NexaCyan, CircleShape))
                Box(modifier = Modifier.size(8.dp).background(NexaSurface, CircleShape))
                Box(modifier = Modifier.size(8.dp).background(NexaSurface, CircleShape))
                Box(modifier = Modifier.size(8.dp).background(NexaSurface, CircleShape))
                Box(modifier = Modifier.size(8.dp).background(NexaSurface, CircleShape))
            }
        }

        Button(
            onClick = onGetStarted,
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
                .background(
                    brush = Brush.horizontalGradient(listOf(NexaCyan, NexaBlue, NexaPurple)),
                    shape = RoundedCornerShape(27.dp)
                ),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
        ) {
            Text("Get Started", color = NexaText, fontSize = 16.sp, fontWeight = FontWeight.Bold)
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
        Box(
            modifier = Modifier
                .size(60.dp)
                .background(Brush.linearGradient(listOf(NexaCyan, NexaBlue)), RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text("N", color = NexaText, fontSize = 32.sp, fontWeight = FontWeight.Black)
        }
        Spacer(Modifier.height(12.dp))
        Text("Nexa AI", color = NexaText, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(32.dp))

        // Tabs
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(NexaSurface, RoundedCornerShape(25.dp))
                .padding(4.dp)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .background(NexaBlue, RoundedCornerShape(20.dp))
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) { Text("Log In", color = NexaText, fontWeight = FontWeight.Bold) }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) { Text("Sign Up", color = NexaMuted) }
        }

        Spacer(Modifier.height(24.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            placeholder = { Text("Enter your email or username", color = NexaMuted) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = NexaSurface,
                unfocusedContainerColor = NexaSurface,
                focusedBorderColor = NexaCyan,
                unfocusedBorderColor = Color.Transparent,
                focusedTextColor = NexaText,
                unfocusedTextColor = NexaText
            )
        )
        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            placeholder = { Text("Enter your password", color = NexaMuted) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            trailingIcon = { Icon(Icons.Default.Visibility, contentDescription = null, tint = NexaMuted) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = NexaSurface,
                unfocusedContainerColor = NexaSurface,
                focusedBorderColor = NexaCyan,
                unfocusedBorderColor = Color.Transparent,
                focusedTextColor = NexaText,
                unfocusedTextColor = NexaText
            )
        )

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            TextButton(onClick = {}) {
                Text("Forgot password?", color = NexaCyan, fontSize = 12.sp)
            }
        }

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = onLoginSuccess,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(25.dp),
            colors = ButtonDefaults.buttonColors(containerColor = NexaCyan)
        ) {
            Text("Log In", color = NexaBackground, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(Modifier.height(20.dp))
        Text("or", color = NexaMuted, fontSize = 12.sp)
        Spacer(Modifier.height(20.dp))

        // Social Buttons
        OutlinedButton(
            onClick = onLoginSuccess,
            modifier = Modifier.fillMaxWidth().height(48.dp),
            shape = RoundedCornerShape(24.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, NexaMuted)
        ) {
            Text("Continue with Google", color = NexaText)
        }
        Spacer(Modifier.height(10.dp))
        OutlinedButton(
            onClick = onLoginSuccess,
            modifier = Modifier.fillMaxWidth().height(48.dp),
            shape = RoundedCornerShape(24.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, NexaMuted)
        ) {
            Text("Continue with GitHub", color = NexaText)
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
        // Top Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(Brush.linearGradient(listOf(NexaCyan, NexaPurple)), RoundedCornerShape(10.dp)),
                    contentAlignment = Alignment.Center
                ) { Text("N", color = NexaText, fontWeight = FontWeight.Bold) }
                Spacer(Modifier.width(10.dp))
                Text("Nexa AI", color = NexaText, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }
            Row {
                Icon(Icons.Default.WorkspacePremium, contentDescription = null, tint = Color(0xFFFFD700))
                Spacer(Modifier.width(12.dp))
                IconButton(onClick = { onNavigate("Profile") }) {
                    Icon(Icons.Default.AccountCircle, contentDescription = null, tint = NexaCyan)
                }
            }
        }

        Spacer(Modifier.height(20.dp))
        Text("Good morning, Samuel", color = NexaText, fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Text("What do you want to explore today?", color = NexaMuted, fontSize = 13.sp)

        Spacer(Modifier.height(24.dp))

        // Feature Grid
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            ShortcutCard("Chat", "Ask anything", Icons.Default.Chat, NexaBlue, Modifier.weight(1f)) { onNavigate("Chat") }
            ShortcutCard("Voice", "Speak", Icons.Default.Mic, NexaPurple, Modifier.weight(1f)) { onNavigate("Voice") }
            ShortcutCard("Image", "Upload / Create", Icons.Default.Image, NexaMagenta, Modifier.weight(1f)) { onNavigate("ImageGen") }
        }

        Spacer(Modifier.height(28.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Quick Prompts", color = NexaText, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text("See all >", color = NexaCyan, fontSize = 12.sp)
        }
        Spacer(Modifier.height(12.dp))

        // Prompts List
        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.weight(1f)) {
            item { PromptRow("Explain something", Icons.Default.Help) { onNavigate("Chat") } }
            item { PromptRow("Write a message", Icons.Default.Edit) { onNavigate("Chat") } }
            item { PromptRow("Create an image", Icons.Default.Palette) { onNavigate("ImageGen") } }
            item { PromptRow("Help me code", Icons.Default.Code) { onNavigate("Chat") } }
        }

        // Bottom Bar
        NexaBottomBar(selected = "Home", onNavigate = onNavigate)
    }
}

@Composable
private fun ShortcutCard(title: String, subtitle: String, icon: androidx.compose.ui.graphics.vector.ImageVector, color: Color, modifier: Modifier, onClick: () -> Unit) {
    Column(
        modifier = modifier
            .background(NexaSurface, RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .padding(14.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Box(
            modifier = Modifier.size(36.dp).background(color.copy(alpha = 0.2f), RoundedCornerShape(10.dp)),
            contentAlignment = Alignment.Center
        ) { Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(20.dp)) }
        Spacer(Modifier.height(12.dp))
        Text(title, color = NexaText, fontSize = 14.sp, fontWeight = FontWeight.Bold)
        Text(subtitle, color = NexaMuted, fontSize = 10.sp)
    }
}

@Composable
private fun PromptRow(title: String, icon: androidx.compose.ui.graphics.vector.ImageVector, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(NexaSurface, RoundedCornerShape(14.dp))
            .clickable(onClick = onClick)
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, tint = NexaCyan, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(12.dp))
            Text(title, color = NexaText, fontSize = 14.sp)
        }
        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = NexaMuted)
    }
}

// -----------------------------------------------------------------------------
// SCREEN 5: CHAT SCREEN
// -----------------------------------------------------------------------------
@Composable
private fun ChatScreen(onBack: () -> Unit) {
    var messageText by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, contentDescription = null, tint = NexaText) }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Nexa AI", color = NexaText, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text("Online", color = NexaCyan, fontSize = 11.sp)
            }
            Icon(Icons.Default.MoreVert, contentDescription = null, tint = NexaText)
        }

        Spacer(Modifier.height(16.dp))

        // Chat Conversation
        LazyColumn(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            item {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    Box(
                        modifier = Modifier
                            .background(NexaBubbleUser, RoundedCornerShape(18.dp))
                            .padding(14.dp)
                    ) { Text("Explain how photosynthesis works in simple terms.", color = NexaText, fontSize = 13.sp) }
                }
            }
            item {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start) {
                    Box(
                        modifier = Modifier
                            .background(NexaBubbleAI, RoundedCornerShape(18.dp))
                            .padding(14.dp)
                    ) {
                        Column {
                            Text(
                                "Photosynthesis is the process by which plants use sunlight to turn carbon dioxide and water into glucose (food) and oxygen.",
                                color = NexaText,
                                fontSize = 13.sp
                            )
                            Spacer(Modifier.height(8.dp))
                            Text(
                                "It happens in the chloroplasts of plant cells, using light energy from the sun.",
                                color = NexaMuted,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }
        }

        // Input Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(NexaSurface, RoundedCornerShape(25.dp))
                .padding(horizontal = 14.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Search, contentDescription = null, tint = NexaMuted)
            Spacer(Modifier.width(8.dp))
            OutlinedTextField(
                value = messageText,
                onValueChange = { messageText = it },
                placeholder = { Text("Type a message...", color = NexaMuted, fontSize = 13.sp) },
                modifier = Modifier.weight(1f),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    focusedTextColor = NexaText,
                    unfocusedTextColor = NexaText
                )
            )
            IconButton(
                onClick = {},
                modifier = Modifier.size(36.dp).background(NexaBlue, CircleShape)
            ) { Icon(Icons.Default.Send, contentDescription = null, tint = NexaText, modifier = Modifier.size(18.dp)) }
        }
    }
}

// -----------------------------------------------------------------------------
// SCREEN 6: VOICE MODE
// -----------------------------------------------------------------------------
@Composable
private fun VoiceChatScreen(onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, contentDescription = null, tint = NexaText) }
            Spacer(Modifier.weight(1f))
            Text("Voice Chat", color = NexaText, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.weight(1f))
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(160.dp)
                    .background(
                        brush = Brush.radialGradient(listOf(NexaCyan, NexaPurple, Color.Transparent)),
                        shape = CircleShape
                    )
                    .border(3.dp, NexaCyan, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Mic, contentDescription = null, tint = NexaText, modifier = Modifier.size(60.dp))
            }
            Spacer(Modifier.height(32.dp))
            Text("Tap to speak", color = NexaText, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            Text("Ask anything, get instant answers.", color = NexaMuted, fontSize = 13.sp)
        }

        Button(
            onClick = onBack,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(25.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF2020))
        ) {
            Text("End", color = NexaText, fontWeight = FontWeight.Bold)
        }
    }
}

// -----------------------------------------------------------------------------
// SCREEN 7: IMAGE GENERATION
// -----------------------------------------------------------------------------
@Composable
private fun ImageGenScreen(onBack: () -> Unit) {
    var prompt by remember { mutableStateOf("A futuristic city at night with neon lights, ultra realistic") }
    var selectedStyle by remember { mutableStateOf("Realistic") }

    Column(modifier = Modifier.fillMaxSize().padding(20.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, contentDescription = null, tint = NexaText) }
            Text("Create Image", color = NexaText, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = prompt,
            onValueChange = { prompt = it },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = NexaSurface,
                unfocusedContainerColor = NexaSurface,
                focusedBorderColor = NexaCyan,
                unfocusedBorderColor = Color.Transparent,
                focusedTextColor = NexaText,
                unfocusedTextColor = NexaText
            )
        )

        Spacer(Modifier.height(12.dp))

        // Style Chips
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("Realistic", "Anime", "Artistic").forEach { style ->
                FilterChip(
                    selected = selectedStyle == style,
                    onClick = { selectedStyle = style },
                    label = { Text(style, color = NexaText) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = NexaBlue,
                        containerColor = NexaSurface
                    )
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = {},
            modifier = Modifier.fillMaxWidth().height(48.dp),
            shape = RoundedCornerShape(24.dp),
            colors = ButtonDefaults.buttonColors(containerColor = NexaPurple)
        ) { Text("Generate", color = NexaText, fontWeight = FontWeight.Bold) }

        Spacer(Modifier.height(20.dp))

        // Generated Image Canvas Placeholder
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(NexaSurface, RoundedCornerShape(20.dp))
                .border(1.dp, NexaMuted.copy(alpha = 0.3f), RoundedCornerShape(20.dp)),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(Icons.Default.Image, contentDescription = null, tint = NexaCyan, modifier = Modifier.size(48.dp))
                Spacer(Modifier.height(8.dp))
                Text("Generated preview canvas", color = NexaMuted, fontSize = 12.sp)
            }
        }
    }
}

// -----------------------------------------------------------------------------
// SCREEN 8: CHAT HISTORY & PROFILE / SETTINGS
// -----------------------------------------------------------------------------
@Composable
private fun HistoryScreen(onBack: () -> Unit, onSelectChat: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().padding(20.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, contentDescription = null, tint = NexaText) }
            Text("Chat History", color = NexaText, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(Modifier.height(16.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(
                listOf(
                    "Photosynthesis explanation" to "9:41 AM",
                    "Image generation" to "Yesterday",
                    "Study help" to "Yesterday",
                    "Coding assistance" to "2 days ago",
                    "Travel ideas" to "3 days ago"
                )
            ) { item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(NexaSurface, RoundedCornerShape(14.dp))
                        .clickable { onSelectChat() }
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(item.first, color = NexaText, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text("Here is a simple explanation...", color = NexaMuted, fontSize = 11.sp)
                    }
                    Text(item.second, color = NexaMuted, fontSize = 11.sp)
                }
            }
        }
    }
}

@Composable
private fun ProfileScreen(onBack: () -> Unit) {
    var darkMode by remember { mutableStateOf(true) }

    Column(modifier = Modifier.fillMaxSize().padding(20.dp)) {
        IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, contentDescription = null, tint = NexaText) }

        Spacer(Modifier.height(12.dp))

        // Profile Avatar Header
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(Brush.linearGradient(listOf(NexaCyan, NexaPurple)), CircleShape),
                contentAlignment = Alignment.Center
            ) { Icon(Icons.Default.Person, contentDescription = null, tint = NexaText, modifier = Modifier.size(48.dp)) }
            Spacer(Modifier.height(12.dp))
            Text("Samuel Ngethe", color = NexaText, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text("@samuel_ng", color = NexaMuted, fontSize = 12.sp)
            Spacer(Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .background(NexaPurple, RoundedCornerShape(12.dp))
                    .padding(horizontal = 12.dp, vertical = 4.dp)
            ) { Text("Free Plan", color = NexaText, fontSize = 11.sp, fontWeight = FontWeight.Bold) }
        }

        Spacer(Modifier.height(32.dp))

        // Settings List
        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            item { SettingsRow(Icons.Default.Lock, "Account & Security") }
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(NexaSurface, RoundedCornerShape(14.dp))
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Palette, contentDescription = null, tint = NexaCyan)
                        Spacer(Modifier.width(12.dp))
                        Text("Appearance", color = NexaText)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Dark Mode", color = NexaMuted, fontSize = 12.sp)
                        Spacer(Modifier.width(8.dp))
                        Switch(
                            checked = darkMode,
                            onCheckedChange = { darkMode = it },
                            colors = SwitchDefaults.colors(checkedThumbColor = NexaCyan)
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
            .background(NexaSurface, RoundedCornerShape(14.dp))
            .clickable {}
            .padding(14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, tint = NexaCyan)
            Spacer(Modifier.width(12.dp))
            Text(title, color = NexaText)
        }
        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = NexaMuted)
    }
}

// Bottom Bar Reusable Composable
@Composable
private fun NexaBottomBar(selected: String, onNavigate: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(NexaSurface, RoundedCornerShape(25.dp))
            .padding(vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        IconButton(onClick = { onNavigate("Home") }) {
            Icon(Icons.Default.Home, contentDescription = null, tint = if (selected == "Home") NexaCyan else NexaMuted)
        }
        IconButton(onClick = { onNavigate("Chat") }) {
            Icon(Icons.Default.Chat, contentDescription = null, tint = if (selected == "Chat") NexaCyan else NexaMuted)
        }
        IconButton(onClick = { onNavigate("Voice") }) {
            Icon(Icons.Default.Mic, contentDescription = null, tint = if (selected == "Voice") NexaCyan else NexaMuted)
        }
        IconButton(onClick = { onNavigate("History") }) {
            Icon(Icons.Default.History, contentDescription = null, tint = if (selected == "History") NexaCyan else NexaMuted)
        }
        IconButton(onClick = { onNavigate("Profile") }) {
            Icon(Icons.Default.Person, contentDescription = null, tint = if (selected == "Profile") NexaCyan else NexaMuted)
        }
    }
}
