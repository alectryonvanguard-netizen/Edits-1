
package com.nexa.ai

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope

private val ChatBackground = Color(0xFF050816)
private val ChatSurface = Color(0xFF10172A)
private val ChatCyan = Color(0xFF00E5FF)
private val ChatPurple = Color(0xFF963BFF)
private val ChatWhite = Color(0xFFF4F7FF)
private val ChatMuted = Color(0xFF9AA9CC)

@Composable
fun ChatScreen(
    chatViewModel: ChatViewModel = viewModel()
) {
    val messages by chatViewModel.messages.collectAsState()
    val isLoading by chatViewModel.isLoading.collectAsState()
    var input by remember { mutableStateOf("") }
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ChatBackground)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp, bottom = 18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .background(ChatSurface, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "N",
                    color = ChatCyan,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 12.dp)
            ) {
                Text(
                    "Nexa AI",
                    color = ChatWhite,
                    fontSize = 21.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    if (isLoading) "Thinking..." else "Your AI assistant",
                    color = ChatMuted,
                    fontSize = 13.sp
                )
            }

            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = ChatCyan,
                    strokeWidth = 2.dp
                )
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            state = listState,
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            items(messages, key = { it.id }) { message ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = if (message.isFromUser) {
                        Arrangement.End
                    } else {
                        Arrangement.Start
                    }
                ) {
                    Surface(
                        color = if (message.isFromUser) {
                            Color(0xFF19345A)
                        } else {
                            ChatSurface
                        },
                        shape = RoundedCornerShape(
                            topStart = 18.dp,
                            topEnd = 18.dp,
                            bottomStart = if (message.isFromUser) 18.dp else 4.dp,
                            bottomEnd = if (message.isFromUser) 4.dp else 18.dp
                        ),
                        modifier = Modifier.fillMaxWidth(0.88f)
                    ) {
                        Text(
                            text = message.text,
                            color = ChatWhite,
                            fontSize = 15.sp,
                            lineHeight = 22.sp,
                            modifier = Modifier.padding(14.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(ChatSurface, RoundedCornerShape(28.dp))
                .padding(horizontal = 8.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {
                    input = if (input.isEmpty()) {
                        " "
                    } else {
                        input
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add attachment",
                    tint = ChatPurple
                )
            }

            BasicTextField(
                value = input,
                onValueChange = { input = it },
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp),
                textStyle = androidx.compose.ui.text.TextStyle(
                    color = ChatWhite,
                    fontSize = 15.sp
                ),
                cursorBrush = SolidColor(ChatCyan),
                decorationBox = { innerTextField ->
                    Box {
                        if (input.isEmpty()) {
                            Text(
                                "Message Nexa AI...",
                                color = ChatMuted,
                                fontSize = 15.sp
                            )
                        }
                        innerTextField()
                    }
                }
            )

            IconButton(
                enabled = input.isNotBlank() && !isLoading,
                onClick = {
                    val question = input.trim()
                    if (question.isNotEmpty()) {
                        chatViewModel.sendMessage(question)
                        input = ""
                        scope.launch {
                            listState.animateScrollToItem(messages.size)
                        }
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "Send message",
                    tint = if (input.isNotBlank() && !isLoading) {
                        ChatCyan
                    } else {
                        ChatMuted
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
    }
}
