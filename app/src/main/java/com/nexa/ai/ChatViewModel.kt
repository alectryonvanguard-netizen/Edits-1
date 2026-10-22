
package com.nexa.ai

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ChatViewModel : ViewModel() {

    private val _messages = MutableStateFlow(
        listOf(
            ChatMessage(
                text = "Hi! I'm Nexa AI. What would you like to explore today?",
                isFromUser = false
            )
        )
    )

    val messages: StateFlow<List<ChatMessage>> = _messages.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun sendMessage(text: String) {
        val question = text.trim()
        if (question.isEmpty() || _isLoading.value) return

        _messages.value = _messages.value + ChatMessage(
            text = question,
            isFromUser = true
        )

        _isLoading.value = true

        viewModelScope.launch {
            try {
                val reply = "The secure AI connection still needs to be configured."
                _messages.value = _messages.value + ChatMessage(
                    text = reply,
                    isFromUser = false
                )
            } finally {
                _isLoading.value = false
            }
        }
    }
}
