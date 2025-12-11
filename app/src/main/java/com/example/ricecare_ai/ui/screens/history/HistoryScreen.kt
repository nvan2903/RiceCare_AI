package com.example.ricecare_ai.ui.screens.history

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    onNavigateToDetail: (String) -> Unit,
    onNavigateBack: () -> Unit,
    onNavigateToDashboard: () -> Unit = {},
    onNavigateToUpload: () -> Unit = {},
    onNavigateToChat: () -> Unit = {}
) {
    var selectedFilter by remember { mutableStateOf("Tất cả") }
    val filterOptions = listOf("Tất cả", "Đạo ôn", "Bạc lá", "Khỏe")
    
    val historyItems = remember {
        listOf(
            HistoryItem(
                id = "1",
                imageUrl = "https://via.placeholder.com/100",
                label = "Bệnh đạo ôn",
                confidence = 93,
                date = "08/12/2024",
                time = "14:30"
            ),
            HistoryItem(
                id = "2",
                imageUrl = "https://via.placeholder.com/100",
                label = "Lá khỏe",
                confidence = 89,
                date = "07/12/2024",
                time = "10:15"
            ),
            HistoryItem(
                id = "3",
                imageUrl = "https://via.placeholder.com/100",
                label = "Bệnh bạc lá",
                confidence = 85,
                date = "06/12/2024",
                time = "16:45"
            )
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Lịch sử chẩn đoán",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
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
        HistoryScreenContent(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            onNavigateToDetail = onNavigateToDetail
        )
    }
}

@Composable
internal fun HistoryScreenContent(
    modifier: Modifier = Modifier,
    onNavigateToDetail: (String) -> Unit = {}
) {
    var selectedFilter by remember { mutableStateOf("Tất cả") }
    val filterOptions = listOf("Tất cả", "Đạo ôn", "Bạc lá", "Khỏe")
    
    val historyItems = remember {
        listOf(
            HistoryItem(
                id = "1",
                imageUrl = "https://via.placeholder.com/100",
                label = "Bệnh đạo ôn",
                confidence = 93,
                date = "08/12/2024",
                time = "14:30"
            ),
            HistoryItem(
                id = "2",
                imageUrl = "https://via.placeholder.com/100",
                label = "Lá khỏe",
                confidence = 89,
                date = "07/12/2024",
                time = "10:15"
            ),
            HistoryItem(
                id = "3",
                imageUrl = "https://via.placeholder.com/100",
                label = "Bệnh bạc lá",
                confidence = 85,
                date = "06/12/2024",
                time = "16:45"
            )
        )
    }

    Column(
        modifier = modifier
    ) {
        // Filter Chips
        LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
            ) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        filterOptions.forEach { filter ->
                            FilterChip(
                                selected = selectedFilter == filter,
                                onClick = { selectedFilter = filter },
                                label = { Text(filter) }
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                }

                // History Items
                items(historyItems) { item ->
                    HistoryItemCard(
                        item = item,
                        onClick = { onNavigateToDetail(item.id) }
                    )
                }
            }
        }
    }


@Composable
fun HistoryItemCard(
    item: HistoryItem,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Thumbnail
            Image(
                painter = rememberAsyncImagePainter(item.imageUrl),
                contentDescription = "Thumbnail",
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            
            Spacer(modifier = Modifier.width(12.dp))
            
            // Info
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.label,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Badge(
                        containerColor = if (item.label.contains("khỏe", ignoreCase = true))
                            MaterialTheme.colorScheme.primaryContainer
                        else
                            MaterialTheme.colorScheme.errorContainer
                    ) {
                        Text(
                            "${item.confidence}%",
                            color = if (item.label.contains("khỏe", ignoreCase = true))
                                MaterialTheme.colorScheme.onPrimaryContainer
                            else
                                MaterialTheme.colorScheme.onErrorContainer
                        )
                    }
                    Text(
                        text = "${item.date} ${item.time}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }
            
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )
        }
    }
}

data class HistoryItem(
    val id: String,
    val imageUrl: String,
    val label: String,
    val confidence: Int,
    val date: String,
    val time: String
)
