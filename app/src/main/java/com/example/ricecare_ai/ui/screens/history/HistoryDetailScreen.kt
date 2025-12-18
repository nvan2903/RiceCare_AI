package com.example.ricecare_ai.ui.screens.history

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.ricecare_ai.data.model.PredictionHistory
import com.example.ricecare_ai.viewmodel.HistoryViewModel
import com.example.ricecare_ai.viewmodel.ViewModelFactory
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryDetailScreen(
    id: String,
    onNavigateBack: () -> Unit,
    onNavigateToChat: () -> Unit,
    authToken: String? = null
) {
    val context = LocalContext.current
    val viewModel: HistoryViewModel = viewModel(
        factory = ViewModelFactory(context.applicationContext as android.app.Application)
    )
    
    val detailState by viewModel.detailState.collectAsState()
    
    // Load prediction detail
    LaunchedEffect(id, authToken) {
        viewModel.setAuthToken(authToken)
        viewModel.loadPredictionDetail(id)
    }
    
    // Cleanup when leaving
    DisposableEffect(Unit) {
        onDispose {
            viewModel.clearDetailState()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Chi tiết chẩn đoán",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { /* Export PDF */ }) {
                        Icon(Icons.Default.Share, contentDescription = "Export PDF")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { paddingValues ->
        when {
            detailState.isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            
            detailState.error != null -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "⚠️",
                        style = MaterialTheme.typography.displayLarge
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = detailState.error!!,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.error
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { viewModel.loadPredictionDetail(id) }) {
                        Text("Thử lại")
                    }
                }
            }
            
            detailState.prediction != null -> {
                PredictionDetailContent(
                    prediction = detailState.prediction!!,
                    onNavigateToChat = onNavigateToChat,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                )
            }
        }
    }
}

@Composable
fun PredictionDetailContent(
    prediction: PredictionHistory,
    onNavigateToChat: () -> Unit,
    modifier: Modifier = Modifier
) {
    val diseaseInfo = prediction.diseaseInfo
    val isHealthy = prediction.predictedClass.contains("healthy", ignoreCase = true)
    val formattedDate = formatDetailTimestamp(prediction.timestamp)
    
    // Get display values with fallbacks
    val displayName = diseaseInfo?.nameVi ?: diseaseInfo?.name ?: prediction.predictedClass
    val displayNameEn = diseaseInfo?.nameEn ?: prediction.predictedClass
    val displayDescription = diseaseInfo?.description ?: "Không có mô tả"
    val displaySeverity = diseaseInfo?.severity ?: "unknown"
    val displaySeverityVi = diseaseInfo?.severityVi ?: if (isHealthy) "Khỏe mạnh" else "Chưa xác định"
    
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Large Image
        if (prediction.imageUrl != null) {
            Image(
                painter = rememberAsyncImagePainter(prediction.imageUrl),
                contentDescription = "Disease Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text("🌾", style = MaterialTheme.typography.displayLarge)
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Result Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = if (isHealthy) 
                    Color(0xFFE8F5E9) 
                else 
                    MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f)
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = displayName,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = displayNameEn,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Badge(
                        containerColor = when (displaySeverity.lowercase()) {
                            "healthy" -> Color(0xFF4CAF50)
                            "low" -> Color(0xFFFFC107)
                            "medium" -> Color(0xFFFF9800)
                            "high" -> Color(0xFFF44336)
                            else -> MaterialTheme.colorScheme.error
                        },
                        contentColor = Color.White
                    ) {
                        Text(
                            displaySeverityVi,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Confidence
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Độ tin cậy:",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    LinearProgressIndicator(
                        progress = { prediction.confidence / 100f },
                        modifier = Modifier
                            .weight(1f)
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = if (prediction.confidence > 80f)
                            Color(0xFF212121) // Gray900
                        else if (prediction.confidence > 60f)
                            Color(0xFF757575) // Gray600
                        else
                            Color(0xFFBDBDBD), // Gray400
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "${prediction.confidence.toInt()}%",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(16.dp))
                
                // Description
                Text(
                    text = "Mô tả",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = displayDescription,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Date info
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Column {
                        Text(
                            text = "Ngày chẩn đoán",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                        )
                        Text(
                            text = formattedDate.first,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    
                    Column {
                        Text(
                            text = "Giờ",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                        )
                        Text(
                            text = formattedDate.second,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
        
        // Disease Details Card (if not healthy)
        if (!isHealthy) {
            Spacer(modifier = Modifier.height(16.dp))
            
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // Symptoms
                    Text(
                        text = "🔍 Triệu chứng",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    diseaseInfo?.symptoms?.forEach { symptom ->
                        Text(
                            text = "• $symptom",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(vertical = 2.dp)
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider()
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Treatment
                    Text(
                        text = "💊 Cách điều trị",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = diseaseInfo?.treatment ?: "Không có thông tin",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider()
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Prevention
                    Text(
                        text = "🛡️ Phòng ngừa",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    diseaseInfo?.prevention?.forEach { prevention ->
                        Text(
                            text = "• $prevention",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(vertical = 2.dp)
                        )
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Action Buttons
        Button(
            onClick = onNavigateToChat,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Icon(Icons.Default.Create, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Nhận tư vấn chi tiết từ AI")
        }
        
        Spacer(modifier = Modifier.height(24.dp))
    }
}

/**
 * Format timestamp to date and time pair
 */
private fun formatDetailTimestamp(timestamp: Any?): Pair<String, String> {
    return try {
        when (timestamp) {
            is Long -> {
                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
                val date = Date(timestamp)
                Pair(dateFormat.format(date), timeFormat.format(date))
            }
            is Double -> {
                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
                val date = Date(timestamp.toLong())
                Pair(dateFormat.format(date), timeFormat.format(date))
            }
            is String -> Pair(timestamp, "N/A")
            else -> Pair("N/A", "N/A")
        }
    } catch (e: Exception) {
        Pair("N/A", "N/A")
    }
}
