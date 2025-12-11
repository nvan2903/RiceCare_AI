package com.example.ricecare_ai.ui.screens.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.foundation.Canvas

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    onNavigateToUpload: () -> Unit,
    onNavigateToChat: () -> Unit,
    onNavigateToHistory: () -> Unit,
    onNavigateToSettings: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // Logo (clickable)
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primaryContainer)
                                .clickable { onNavigateToSettings() },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = "Settings",
                                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        
                        Spacer(modifier = Modifier.width(12.dp))
                        
                        Column {
                            Text(
                                text = "RiceCare AI",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Xin chào, User",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { paddingValues ->
        DashboardScreenContent(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            onNavigateToUpload = onNavigateToUpload,
            onNavigateToChat = onNavigateToChat,
            onNavigateToHistory = onNavigateToHistory
        )
    }
}

@Composable
internal fun DashboardScreenContent(
    modifier: Modifier = Modifier,
    onNavigateToUpload: () -> Unit = {},
    onNavigateToChat: () -> Unit = {},
    onNavigateToHistory: () -> Unit = {},
    onNavigateToSettings: () -> Unit = {},
    hasData: Boolean = true // Giả định có dữ liệu để demo
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // Top Bar with Logo
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Logo (clickable)
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .clickable { onNavigateToSettings() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Settings",
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.size(24.dp)
                )
            }
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column {
                Text(
                    text = "RiceCare AI",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Xin chào, User",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
        }
        
        Column(
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            if (!hasData) {
            // Empty state
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Bạn chưa có kết quả nào.",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Bắt đầu bằng Upload ảnh.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            // Statistics Cards
            Text(
                text = "Thống kê",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard(
                    title = "Lần chẩn đoán gần nhất",
                    value = "Đạo ôn",
                    percentage = "93%",
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    title = "Tổng lượt",
                    value = "12",
                    percentage = "",
                    modifier = Modifier.weight(1f)
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Pie Chart Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Tỷ lệ bệnh vs khỏe",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Mini Pie Chart
                        MiniPieChart(
                            diseaseCount = 7,
                            healthyCount = 5,
                            modifier = Modifier.size(80.dp)
                        )
                        
                        Spacer(modifier = Modifier.width(16.dp))
                        
                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(12.dp)
                                        .background(
                                            Color(0xFFEF5350),
                                            CircleShape
                                        )
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "7 bệnh (58%)",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                            
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(12.dp)
                                        .background(
                                            Color(0xFF66BB6A),
                                            CircleShape
                                        )
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "5 khỏe (42%)",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
        }
            
        // Main Action Buttons
        Text(
            text = "Chức năng",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        
        ActionCard(
            title = "Upload & Dự đoán",
            description = "Chụp hoặc tải ảnh lá lúa để phân tích",
            icon = Icons.Default.Add,
            onClick = onNavigateToUpload
        )
        
        Spacer(modifier = Modifier.height(12.dp))
        
        ActionCard(
            title = "Chat với chuyên gia AI",
            description = "Tư vấn và giải đáp thắc mắc",
            icon = Icons.Default.Create,
            onClick = onNavigateToChat
        )
        
        Spacer(modifier = Modifier.height(12.dp))
        
        ActionCard(
            title = "Xem lịch sử chẩn đoán",
            description = "Tra cứu các kết quả đã lưu",
            icon = Icons.Default.Info,
            onClick = onNavigateToHistory
        )
    }
}
}

@Composable
fun MiniPieChart(
    diseaseCount: Int,
    healthyCount: Int,
    modifier: Modifier = Modifier
) {
    val total = diseaseCount + healthyCount
    val diseaseAngle = if (total > 0) 360f * diseaseCount / total else 0f
    
    val diseaseColor = Color(0xFFEF5350)
    val healthyColor = Color(0xFF66BB6A)
    
    Canvas(modifier = modifier) {
        val canvasSize = size.minDimension
        val radius = canvasSize / 2
        val strokeWidth = radius * 0.3f
        
        // Draw healthy arc (starts from 0)
        drawArc(
            color = healthyColor,
            startAngle = diseaseAngle,
            sweepAngle = 360f - diseaseAngle,
            useCenter = false,
            size = Size(canvasSize, canvasSize),
            style = Stroke(width = strokeWidth)
        )
        
        // Draw disease arc
        drawArc(
            color = diseaseColor,
            startAngle = 0f,
            sweepAngle = diseaseAngle,
            useCenter = false,
            size = Size(canvasSize, canvasSize),
            style = Stroke(width = strokeWidth)
        )
    }
}


@Composable
fun StatCard(
    title: String,
    value: String,
    percentage: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            if (percentage.isNotEmpty()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = percentage,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
fun ActionCard(
    title: String,
    description: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
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
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.size(28.dp)
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
            
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )
        }
    }
}
