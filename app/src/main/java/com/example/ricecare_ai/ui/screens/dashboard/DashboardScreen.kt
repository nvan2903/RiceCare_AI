package com.example.ricecare_ai.ui.screens.dashboard

import androidx.compose.foundation.Canvas
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ricecare_ai.viewmodel.DashboardViewModel
import com.example.ricecare_ai.viewmodel.SharedViewModels

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    onNavigateToUpload: () -> Unit,
    onNavigateToChat: () -> Unit,
    onNavigateToHistory: () -> Unit,
    onNavigateToSettings: () -> Unit = {},
    authToken: String? = null,
    userName: String = "User"
) {
    val viewModel = SharedViewModels.getDashboardViewModel()
    val uiState by viewModel.uiState.collectAsState()
    
    // Set token and load data
    LaunchedEffect(authToken, userName) {
        viewModel.setAuthToken(authToken)
        viewModel.setUserName(userName)
        if (authToken != null) {
            viewModel.loadDashboardData()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // Settings icon
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
                                text = "Xin chào, ${uiState.userName}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                            )
                        }
                    }
                },
                actions = {
                    if (authToken != null) {
                        IconButton(onClick = { viewModel.refresh() }) {
                            Icon(Icons.Default.Refresh, contentDescription = "Refresh")
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { paddingValues ->
        DashboardContent(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            uiState = uiState,
            isLoggedIn = authToken != null,
            onNavigateToUpload = onNavigateToUpload,
            onNavigateToChat = onNavigateToChat,
            onNavigateToHistory = onNavigateToHistory,
            onDismissError = { viewModel.dismissError() }
        )
    }
}

@Composable
private fun DashboardContent(
    modifier: Modifier = Modifier,
    uiState: com.example.ricecare_ai.viewmodel.DashboardUiState,
    isLoggedIn: Boolean,
    onNavigateToUpload: () -> Unit,
    onNavigateToChat: () -> Unit,
    onNavigateToHistory: () -> Unit,
    onDismissError: () -> Unit
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Error display
        uiState.error?.let { error ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        modifier = Modifier.weight(1f)
                    )
                    TextButton(onClick = onDismissError) {
                        Text("Đóng")
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
        
        // Loading indicator
        if (uiState.isLoading) {
            LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Statistics Section (only if logged in and has data)
        if (isLoggedIn && uiState.totalPredictions > 0) {
            Text(
                text = "Thống kê chẩn đoán",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            // First row - Total predictions
            StatCard(
                title = "Tổng lượt chẩn đoán",
                value = "${uiState.totalPredictions}",
                percentage = "",
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Second row - Healthy and diseased counts
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard(
                    title = "Lá khỏe mạnh",
                    value = "${uiState.healthyCount}",
                    percentage = if (uiState.totalPredictions > 0) 
                        "${(uiState.healthyCount * 100.0 / uiState.totalPredictions).toInt()}%" 
                    else "",
                    color = Color(0xFFBDBDBD), // Gray400 for healthy
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    title = "Phát hiện bệnh",
                    value = "${uiState.diseasedCount}",
                    percentage = if (uiState.totalPredictions > 0) 
                        "${(uiState.diseasedCount * 100.0 / uiState.totalPredictions).toInt()}%" 
                    else "",
                    color = Color(0xFF757575), // Gray600 for disease
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
                        MiniPieChart(
                            diseaseCount = uiState.diseasedCount,
                            healthyCount = uiState.healthyCount,
                            modifier = Modifier.size(80.dp)
                        )
                        
                        Spacer(modifier = Modifier.width(16.dp))
                        
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(12.dp)
                                        .background(Color(0xFF757575), CircleShape) // Gray600
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                val diseasePercent = if (uiState.totalPredictions > 0)
                                    (uiState.diseasedCount * 100.0 / uiState.totalPredictions).toInt() else 0
                                Text(
                                    text = "${uiState.diseasedCount} bệnh ($diseasePercent%)",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                            
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(12.dp)
                                        .background(Color(0xFFBDBDBD), CircleShape) // Gray400
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                val healthyPercent = if (uiState.totalPredictions > 0)
                                    (uiState.healthyCount * 100.0 / uiState.totalPredictions).toInt() else 0
                                Text(
                                    text = "${uiState.healthyCount} khỏe ($healthyPercent%)",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
        } else if (isLoggedIn && !uiState.isLoading) {
            // Empty state for logged in users
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
                    Text("🌾", style = MaterialTheme.typography.displayMedium)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Bạn chưa có kết quả nào",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Bắt đầu bằng cách upload ảnh lá lúa",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
            
        // Main Action Buttons
        Text(
            text = "Chức năng chính",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
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
        
        Spacer(modifier = Modifier.height(24.dp))
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
    
    val diseaseColor = Color(0xFF757575) // Gray600 for disease
    val healthyColor = Color(0xFFBDBDBD) // Gray400 for healthy
    
    Canvas(modifier = modifier) {
        val canvasSize = size.minDimension
        val strokeWidth = canvasSize * 0.3f
        
        // Draw healthy arc
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
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = color
            )
            if (percentage.isNotEmpty()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = percentage,
                    style = MaterialTheme.typography.bodySmall,
                    color = color.copy(alpha = 0.8f)
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
