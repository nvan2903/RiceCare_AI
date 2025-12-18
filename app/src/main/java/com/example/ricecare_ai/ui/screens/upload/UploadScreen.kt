package com.example.ricecare_ai.ui.screens.upload

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.ricecare_ai.data.model.PredictionResponse
import com.example.ricecare_ai.viewmodel.PredictionViewModel
import com.example.ricecare_ai.viewmodel.ViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UploadScreen(
    onNavigateToChatWithPrediction: (String) -> Unit,
    onNavigateBack: () -> Unit,
    onNavigateToDashboard: () -> Unit = {},
    onNavigateToHistory: () -> Unit = {},
    authToken: String? = null
) {
    val context = LocalContext.current
    val viewModel: PredictionViewModel = viewModel(
        factory = ViewModelFactory(context.applicationContext as android.app.Application)
    )
    
    // Set auth token
    LaunchedEffect(authToken) {
        viewModel.setAuthToken(authToken)
    }
    
    val uiState by viewModel.uiState.collectAsState()
    
    // Photo picker launcher
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        uri?.let { viewModel.selectImage(it) }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Dự đoán bệnh lá lúa",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Show error if any
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
                        TextButton(onClick = { viewModel.dismissError() }) {
                            Text("Đóng")
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            when {
                // No image selected
                uiState.selectedImageUri == null -> {
                    UploadPlaceholder(
                        onPickFromGallery = {
                            photoPickerLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        }
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text(
                        text = "Ảnh rõ nét, lá chiếm phần lớn khung hình",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                        textAlign = TextAlign.Center
                    )
                }
                
                // Analyzing
                uiState.isAnalyzing -> {
                    AnalyzingView(imageUri = uiState.selectedImageUri!!)
                }
                
                // Has result
                uiState.showResult && uiState.predictionResult != null -> {
                    PredictionResultView(
                        imageUri = uiState.selectedImageUri!!,
                        result = uiState.predictionResult!!,
                        onNavigateToChatWithPrediction = onNavigateToChatWithPrediction,
                        onNewPrediction = { viewModel.resetPrediction() }
                    )
                }
                
                // Image selected, ready to analyze
                else -> {
                    Image(
                        painter = rememberAsyncImagePainter(uiState.selectedImageUri),
                        contentDescription = "Selected Image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                            .clip(RoundedCornerShape(16.dp)),
                        contentScale = ContentScale.Crop
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Button(
                        onClick = { viewModel.analyzeImage(saveHistory = authToken != null) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Phân tích ảnh")
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    OutlinedButton(
                        onClick = { viewModel.clearImage() },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Chọn ảnh khác")
                    }
                }
            }
        }
    }
}

@Composable
fun UploadPlaceholder(
    onPickFromGallery: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .border(
                width = 2.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable { onPickFromGallery() },
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Chọn ảnh lá lúa",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Nhấn để chọn từ thư viện",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
            )
        }
    }
    
    Spacer(modifier = Modifier.height(16.dp))
    
    // Quick action buttons
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedButton(
            onClick = onPickFromGallery,
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Thư viện")
        }
        
        OutlinedButton(
            onClick = { /* Camera - implement later */ },
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Camera")
        }
    }
}

@Composable
fun AnalyzingView(imageUri: Uri) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = rememberAsyncImagePainter(imageUri),
            contentDescription = "Selected Image",
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .clip(RoundedCornerShape(16.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(24.dp))

        CircularProgressIndicator()

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Đang phân tích ảnh bằng AI RiceVision…",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "Vui lòng đợi trong giây lát",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
        )
    }
}

@Composable
fun PredictionResultView(
    imageUri: Uri,
    result: PredictionResponse,
    onNavigateToChatWithPrediction: (String) -> Unit,
    onNewPrediction: () -> Unit
) {
    val diseaseInfo = result.diseaseInfo
    val isHealthy = result.predictedClass.contains("healthy", ignoreCase = true)
    
    // Get display values with fallbacks
    val displayNameVi = diseaseInfo.nameVi ?: diseaseInfo.name ?: result.predictedClass
    val displayNameEn = diseaseInfo.nameEn ?: result.predictedClass
    val displayDescription = diseaseInfo.description ?: "Không có mô tả"
    val displaySeverity = diseaseInfo.severity ?: "unknown"
    val displaySeverityVi = diseaseInfo.severityVi ?: if (isHealthy) "Khỏe mạnh" else "Chưa xác định"
    val displayTreatment = diseaseInfo.treatment ?: "Không có thông tin"
    
    Image(
        painter = rememberAsyncImagePainter(imageUri),
        contentDescription = "Selected Image",
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
            .clip(RoundedCornerShape(16.dp)),
        contentScale = ContentScale.Crop
    )

    Spacer(modifier = Modifier.height(16.dp))

    // Main result card
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
                        text = displayNameVi,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = displayNameEn,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Badge(
                    containerColor = when (displaySeverity.lowercase()) {
                        "healthy" -> Color(0xFFE0E0E0) // Gray300 - Khỏe mạnh
                        "low" -> Color(0xFFBDBDBD) // Gray400
                        "medium" -> Color(0xFF757575) // Gray600
                        "high" -> Color(0xFF212121) // Gray900 - Nghiêm trọng
                        else -> MaterialTheme.colorScheme.error
                    },
                    contentColor = when (displaySeverity.lowercase()) {
                        "healthy", "low" -> Color(0xFF000000) // Black text for light bg
                        else -> Color(0xFFFFFFFF) // White text for dark bg
                    }
                ) {
                    Text(
                        displaySeverityVi,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Confidence bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Độ tin cậy:",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.width(8.dp))

                LinearProgressIndicator(
                    progress = { result.confidence / 100f },
                    modifier = Modifier
                        .weight(1f)
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = if (result.confidence > 80f) 
                        Color(0xFF212121) // Gray900 - High confidence
                    else if (result.confidence > 60f)
                        Color(0xFF757575) // Gray600 - Medium
                    else 
                        Color(0xFFBDBDBD), // Gray400 - Low
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "${result.confidence.toInt()}%",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = displayDescription,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }

    Spacer(modifier = Modifier.height(12.dp))

    // Disease details card
    if (!isHealthy) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Triệu chứng",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                diseaseInfo.symptoms?.forEach { symptom ->
                    Text(
                        text = "• $symptom",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                
                Spacer(modifier = Modifier.height(12.dp))
                
                Text(
                    text = "Cách điều trị",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = displayTreatment,
                    style = MaterialTheme.typography.bodySmall
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                Text(
                    text = "Phòng ngừa",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                diseaseInfo.prevention?.take(3)?.forEach { prevention ->
                    Text(
                        text = "• $prevention",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(12.dp))
    }

    // Top 3 predictions
    if (result.top3.isNotEmpty()) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Top 3 dự đoán",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                
                result.top3.forEachIndexed { index, prediction ->
                    val predName = prediction.diseaseInfo?.nameVi ?: prediction.diseaseInfo?.name ?: prediction.className
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "${index + 1}. $predName",
                            style = MaterialTheme.typography.bodySmall
                        )
                        Text(
                            text = "${prediction.confidence.toInt()}%",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
    }

    // Action buttons
    Button(
        onClick = {
            result.predictionId?.let { onNavigateToChatWithPrediction(it) }
        },
        modifier = Modifier.fillMaxWidth(),
        enabled = result.predictionId != null
    ) {
        Text("Nhận tư vấn chi tiết")
    }

    Spacer(modifier = Modifier.height(8.dp))

    OutlinedButton(
        onClick = onNewPrediction,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text("Phân tích ảnh khác")
    }
}
