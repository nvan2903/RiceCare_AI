package com.example.ricecare_ai.ui.screens.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    onNavigateBack: () -> Unit,
    onNavigateToDashboard: () -> Unit = {},
    onNavigateToUpload: () -> Unit = {},
    onNavigateToHistory: () -> Unit = {}
) {
    var messageText by remember { mutableStateOf("") }
    var messages by remember {
        mutableStateOf(
            listOf(
                ChatMessage(
                    text = "Hệ thống xác định: Đạo ôn. Sau đây là hướng dẫn xử lý...",
                    isUser = false,
                    timestamp = "10:30"
                )
            )
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            "Tư vấn nông nghiệp (AI)",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Badge(
                                containerColor = MaterialTheme.colorScheme.error,
                                modifier = Modifier.padding(0.dp)
                            ) {
                                Text("Đạo ôn", modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                            }
                            Text(
                                "93%",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { paddingValues ->
        ChatScreenContent(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        )
    }
}

@Composable
internal fun ChatScreenContent(
    modifier: Modifier = Modifier
) {
    var messageText by remember { mutableStateOf("") }
    var messages by remember {
        mutableStateOf(
            listOf(
                ChatMessage(
                    text = "Hệ thống xác định: Đạo ôn. Sau đây là hướng dẫn xử lý...",
                    isUser = false,
                    timestamp = "10:30"
                )
            )
        )
    }

    Column(
        modifier = modifier
    ) {
        // Messages List
        LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(messages) { message ->
                    ChatBubble(message = message)
                }
            }

            // Input Area
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shadowElevation = 8.dp,
                color = MaterialTheme.colorScheme.surface
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.Bottom
                ) {
                    IconButton(
                        onClick = { /* Attach image */ }
                    ) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = "Attach",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }

                    TextField(
                        value = messageText,
                        onValueChange = { messageText = it },
                        modifier = Modifier.weight(1f),
                        placeholder = { Text("Nhập câu hỏi...") },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                            focusedIndicatorColor = androidx.compose.ui.graphics.Color.Transparent,
                            unfocusedIndicatorColor = androidx.compose.ui.graphics.Color.Transparent
                        ),
                        shape = RoundedCornerShape(24.dp),
                        maxLines = 4
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    IconButton(
                        onClick = {
                            if (messageText.isNotBlank()) {
                                messages = messages + ChatMessage(
                                    text = messageText,
                                    isUser = true,
                                    timestamp = "10:35"
                                )
                                messageText = ""
                                // Simulate AI response
                                messages = messages + ChatMessage(
                                    text = "Cảm ơn câu hỏi của bạn. Tôi sẽ tư vấn chi tiết...",
                                    isUser = false,
                                    timestamp = "10:36"
                                )
                            }
                        },
                        enabled = messageText.isNotBlank()
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.Send,
                            contentDescription = "Send",
                            tint = if (messageText.isNotBlank())
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                        )
                    }
                }
            }
        }
    }


@Composable
fun ChatBubble(message: ChatMessage) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (message.isUser) Arrangement.End else Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .widthIn(max = 280.dp)
                .background(
                    color = if (message.isUser)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp,
                        bottomStart = if (message.isUser) 16.dp else 4.dp,
                        bottomEnd = if (message.isUser) 4.dp else 16.dp
                    )
                )
                .padding(12.dp)
        ) {
            Column {
                Text(
                    text = message.text,
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (message.isUser)
                        MaterialTheme.colorScheme.onPrimary
                    else
                        MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = message.timestamp,
                    style = MaterialTheme.typography.bodySmall,
                    color = if (message.isUser)
                        MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f)
                    else
                        MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                )
            }
        }
    }
}

data class ChatMessage(
    val text: String,
    val isUser: Boolean,
    val timestamp: String
)
