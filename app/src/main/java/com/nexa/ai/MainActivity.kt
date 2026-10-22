
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val NexaBlack = Color.Black
private val NexaWhite = Color.White
private val NexaOrange = Color(0xFFFF8A00)
private val NexaRed = Color(0xFFFF2020)
private val NexaBlue = Color(0xFF0057FF)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.statusBarColor = android.graphics.Color.BLACK
        window.navigationBarColor = android.graphics.Color.BLACK

        setContent {
            MaterialTheme {
                NexaApp()
            }
        }
    }
}

@Composable
private fun NexaApp() {
    var currentPage by remember { mutableStateOf("Home") }
    var selectedFiles by remember { mutableStateOf(listOf<String>()) }
    var voiceText by remember { mutableStateOf("") }

    val photoPicker = rememberLauncherForActivityResult(
        ActivityResultContracts.PickMultipleVisualMedia()
    ) { uris ->
        if (uris.isNotEmpty()) {
            selectedFiles = uris.map { it.toString() }
            currentPage = "Files"
        }
    }

    val videoPicker = rememberLauncherForActivityResult(
        ActivityResultContracts.PickMultipleVisualMedia()
    ) { uris ->
        if (uris.isNotEmpty()) {
            selectedFiles = uris.map { it.toString() }
            currentPage = "Files"
        }
    }

    val documentPicker = rememberLauncherForActivityResult(
        ActivityResultContracts.OpenMultipleDocuments()
    ) { uris ->
        if (uris.isNotEmpty()) {
            selectedFiles = uris.map { it.toString() }
            currentPage = "Files"
        }
    }

    val speechLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == android.app.Activity.RESULT_OK) {
            val spoken = result.data
                ?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                ?.firstOrNull()
            if (!spoken.isNullOrBlank()) {
                voiceText = spoken
                currentPage = "Chat"
            }
        }
    }

    val blackScreen = Modifier
        .fillMaxSize()
        .background(NexaBlack)

    Box(modifier = blackScreen) {
        when (currentPage) {
            "Chat" -> ChatScreen()

            "Files" -> SelectedFilesScreen(
                files = selectedFiles,
                onBack = { currentPage = "Home" },
                onPickPhotos = {
                    photoPicker.launch(
                        androidx.activity.result.PickVisualMediaRequest(
                            ActivityResultContracts.PickVisualMedia.ImageOnly
                        )
                    )
                },
                onPickVideos = {
                    videoPicker.launch(
                        androidx.activity.result.PickVisualMediaRequest(
                            ActivityResultContracts.PickVisualMedia.VideoOnly
                        )
                    )
                },
                onPickDocuments = {
                    documentPicker.launch(
                        arrayOf("*/*")
                    )
                }
            )

            "History" -> SimplePage(
                title = "CHAT HISTORY",
                description = "Your previous conversations will appear here as chat history storage is connected.",
                accent = NexaOrange,
                onBack = { currentPage = "Home" },
                icon = Icons.Default.History
            )

            "Profile" -> SimplePage(
                title = "YOUR PROFILE",
                description = "Nexa AI account and app preferences.",
                accent = NexaRed,
                onBack = { currentPage = "Home" },
                icon = Icons.Default.Person
            )

            else -> HomeScreen(
                onOpenChat = { currentPage = "Chat" },
                onOpenHistory = { currentPage = "History" },
                onOpenProfile = { currentPage = "Profile" },
                onPickPhotos = {
                    photoPicker.launch(
                        androidx.activity.result.PickVisualMediaRequest(
                            ActivityResultContracts.PickVisualMedia.ImageOnly
                        )
                    )
                },
                onPickVideos = {
                    videoPicker.launch(
                        androidx.activity.result.PickVisualMediaRequest(
                            ActivityResultContracts.PickVisualMedia.VideoOnly
                        )
                    )
                },
                onPickDocuments = {
                    documentPicker.launch(arrayOf("*/*"))
                },
                onVoice = {
                    val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                        putExtra(
                            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
                        )
                        putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to Nexa AI")
                    }
                    try {
                        speechLauncher.launch(intent)
                    } catch (_: Exception) {
                        voiceText = "Speech recognition is not available on this device."
                    }
                }
            )
        }
    }
}

@Composable
private fun HomeScreen(
    onOpenChat: () -> Unit,
    onOpenHistory: () -> Unit,
    onOpenProfile: () -> Unit,
    onPickPhotos: () -> Unit,
    onPickVideos: () -> Unit,
    onPickDocuments: () -> Unit,
    onVoice: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NexaBlack)
            .padding(horizontal = 20.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp, bottom = 26.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(NexaWhite, RoundedCornerShape(15.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.GraphicEq,
                    contentDescription = "Nexa AI",
                    tint = NexaBlack,
                    modifier = Modifier.size(30.dp)
                )
            }

            Spacer(Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    "NEXA AI",
                    color = NexaWhite,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Black
                )
                Text(
                    "SMARTER CHATS. BIGGER IDEAS.",
                    color = NexaOrange,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            IconButton(onClick = onOpenProfile) {
                Icon(
                    Icons.Default.Person,
                    contentDescription = "Profile",
                    tint = NexaWhite
                )
            }
        }

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = 18.dp)
        ) {
            item {
                Text(
                    "YOUR IDEAS.\nWITHOUT LIMITS.",
                    color = NexaWhite,
                    fontSize = 32.sp,
                    lineHeight = 38.sp,
                    fontWeight = FontWeight.Black
                )
                Spacer(Modifier.height(10.dp))
                Text(
                    "Chat, speak, and work with your files.",
                    color = NexaWhite,
                    fontSize = 14.sp
                )
            }

            item {
                ActionCard(
                    title = "LIVE CHAT",
                    subtitle = "Start a conversation",
                    icon = Icons.Default.Chat,
                    color = NexaBlue,
                    onClick = onOpenChat
                )
            }

            item {
                ActionCard(
                    title = "VOICE MODE",
                    subtitle = "Speak using Android speech recognition",
                    icon = Icons.Default.Mic,
                    color = NexaRed,
                    onClick = onVoice
                )
            }

            item {
                Text(
                    "TOOLS",
                    color = NexaOrange,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Black
                )
            }

            item {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    SmallToolCard(
                        title = "PHOTOS",
                        icon = Icons.Default.Image,
                        color = NexaBlue,
                        modifier = Modifier.weight(1f),
                        onClick = onPickPhotos
                    )
                    SmallToolCard(
                        title = "VIDEOS",
                        icon = Icons.Default.Movie,
                        color = NexaOrange,
                        modifier = Modifier.weight(1f),
                        onClick = onPickVideos
                    )
                }
            }

            item {
                ActionCard(
                    title = "OPEN DOCUMENTS",
                    subtitle = "Choose files using the Android document picker",
                    icon = Icons.Default.FolderOpen,
                    color = NexaRed,
                    onClick = onPickDocuments
                )
            }

            item {
                ActionCard(
                    title = "CHAT HISTORY",
                    subtitle = "Open your conversation history section",
                    icon = Icons.Default.History,
                    color = NexaOrange,
                    onClick = onOpenHistory
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, NexaWhite, RoundedCornerShape(18.dp))
                .padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BottomItem("HOME", Icons.Default.Home, NexaOrange) {}
            BottomItem("CHATS", Icons.Default.Chat, NexaBlue, onOpenChat)
            BottomItem("FILES", Icons.Default.Folder, NexaRed, onPickDocuments)
            BottomItem("PROFILE", Icons.Default.Person, NexaWhite, onOpenProfile)
        }

        Spacer(Modifier.height(10.dp))
    }
}

@Composable
private fun ActionCard(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(NexaWhite, RoundedCornerShape(18.dp))
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(color, RoundedCornerShape(14.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = title, tint = NexaWhite)
        }
        Spacer(Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                title,
                color = NexaBlack,
                fontWeight = FontWeight.Black,
                fontSize = 15.sp
            )
            Spacer(Modifier.height(4.dp))
            Text(subtitle, color = NexaBlack, fontSize = 12.sp)
        }
        Icon(
            Icons.Default.ArrowForward,
            contentDescription = "Open $title",
            tint = color
        )
    }
}

@Composable
private fun SmallToolCard(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier
            .background(NexaWhite, RoundedCornerShape(18.dp))
            .clickable(onClick = onClick)
            .padding(16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Icon(icon, contentDescription = title, tint = color, modifier = Modifier.size(30.dp))
        Spacer(Modifier.height(12.dp))
        Text(title, color = NexaBlack, fontWeight = FontWeight.Black, fontSize = 13.sp)
        Text("Open picker", color = NexaBlack, fontSize = 11.sp)
    }
}

@Composable
private fun BottomItem(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(horizontal = 5.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(icon, contentDescription = label, tint = color, modifier = Modifier.size(22.dp))
        Spacer(Modifier.height(4.dp))
        Text(label, color = NexaWhite, fontSize = 9.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun SelectedFilesScreen(
    files: List<String>,
    onBack: () -> Unit,
    onPickPhotos: () -> Unit,
    onPickVideos: () -> Unit,
    onPickDocuments: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NexaBlack)
            .padding(20.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = NexaWhite)
            }
            Text(
                "SELECTED FILES",
                color = NexaWhite,
                fontSize = 20.sp,
                fontWeight = FontWeight.Black
            )
        }

        Spacer(Modifier.height(12.dp))
        Text(
            "${files.size} file(s) selected",
            color = NexaOrange,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(12.dp))

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(files) { file ->
                Text(
                    file.substringAfterLast('/').take(70),
                    color = NexaWhite,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp)
                )
                HorizontalDivider(color = NexaBlue)
            }
        }

        Button(
            onClick = onPickPhotos,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = NexaBlue,
                contentColor = NexaWhite
            )
        ) { Text("Choose photos") }

        Button(
            onClick = onPickVideos,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = NexaOrange,
                contentColor = NexaBlack
            )
        ) { Text("Choose videos") }

        Button(
            onClick = onPickDocuments,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = NexaRed,
                contentColor = NexaWhite
            )
        ) { Text("Choose documents") }
    }
}

@Composable
private fun SimplePage(
    title: String,
    description: String,
    accent: Color,
    onBack: () -> Unit,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NexaBlack)
            .padding(20.dp)
    ) {
        IconButton(onClick = onBack) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = NexaWhite)
        }
        Spacer(Modifier.height(24.dp))
        Icon(icon, contentDescription = title, tint = accent, modifier = Modifier.size(54.dp))
        Spacer(Modifier.height(20.dp))
        Text(title, color = NexaWhite, fontSize = 28.sp, fontWeight = FontWeight.Black)
        Spacer(Modifier.height(12.dp))
        Text(description, color = NexaWhite, fontSize = 15.sp)
        Spacer(Modifier.height(24.dp))
        Button(
            onClick = onBack,
            colors = ButtonDefaults.buttonColors(
                containerColor = accent,
                contentColor = NexaWhite
            )
        ) {
            Text("BACK TO HOME")
        }
    }
}
