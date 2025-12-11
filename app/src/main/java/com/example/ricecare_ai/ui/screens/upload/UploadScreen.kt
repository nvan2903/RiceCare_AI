package com.example.ricecare_ai.ui.screens.upload

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// ⭐ Đưa lên đầu hoặc tách file riêng
data class PredictionResult(
    val label: String,
    val confidence: Float,
    val description: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UploadScreen(
    onNavigateToChat: () -> Unit,
    onNavigateBack: () -> Unit,
    onNavigateToDashboard: () -> Unit = {},
    onNavigateToHistory: () -> Unit = {}
) {
    var imageUri by remember { mutableStateOf<String?>(null) }
    var isAnalyzing by remember { mutableStateOf(false) }
    var result by remember { mutableStateOf<PredictionResult?>(null) }

    val scope = rememberCoroutineScope()

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
        UploadScreenContent(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            onNavigateToChat = onNavigateToChat
        )
    }
}

@Composable
internal fun UploadScreenContent(
    modifier: Modifier = Modifier,
    onNavigateToChat: () -> Unit = {}
) {
    var imageUri by remember { mutableStateOf<String?>(null) }
    var isAnalyzing by remember { mutableStateOf(false) }
    var result by remember { mutableStateOf<PredictionResult?>(null) }

    val scope = rememberCoroutineScope()

    Column(
        modifier = modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // --------------------------
        // 1) Chưa chọn ảnh
        // --------------------------
        if (imageUri == null) {
                UploadPlaceHolder { selected ->
                    imageUri = selected
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Ảnh rõ nét, lá chiếm phần lớn khung hình",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                    textAlign = TextAlign.Center
                )
                return@Column
            }

            // --------------------------
            // 2) Đang phân tích
            // --------------------------
            if (isAnalyzing) {
                AnalyzingView(imageUri!!)
                return@Column
            }

            // --------------------------
            // 3) Đã có kết quả
            // --------------------------
            result?.let {
                PredictionResultView(
                    imageUri = imageUri!!,
                    result = it,
                    onNavigateToChat = onNavigateToChat
                )
                return@Column
            }

            // --------------------------
            // 4) Chọn ảnh rồi nhưng chưa phân tích
            // --------------------------
            Image(
                painter = rememberAsyncImagePainter(imageUri),
                contentDescription = "Selected Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    isAnalyzing = true
                    scope.launch {
                        delay(2000)
                        isAnalyzing = false
                        result = PredictionResult(
                            label = "Bệnh đạo ôn",
                            confidence = 0.93f,
                            description = "Triệu chứng: vết cháy hình thoi trên lá, màu nâu xám"
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Phân tích ảnh")
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedButton(
                onClick = {
                    imageUri = null
                    result = null
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Chọn ảnh khác")
            }
        }
    }


// ======================================================
// COMPONENTS
// ======================================================

@Composable
fun UploadPlaceHolder(onImageSelected: (String) -> Unit) {
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
            .clickable {
                onImageSelected("https://via.placeholder.com/400")
            },
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
                text = "Camera hoặc Thư viện",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
            )
        }
    }
}

@Composable
fun AnalyzingView(imageUri: String) {
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
            text = "Đang phân tích ảnh bằng mô hình RiceVision…",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun PredictionResultView(
    imageUri: String,
    result: PredictionResult,
    onNavigateToChat: () -> Unit
) {
    Image(
        painter = rememberAsyncImagePainter(imageUri),
        contentDescription = "Selected Image",
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
            .clip(RoundedCornerShape(16.dp)),
        contentScale = ContentScale.Crop
    )

    Spacer(modifier = Modifier.height(16.dp))

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = result.label,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                Badge(containerColor = MaterialTheme.colorScheme.error) {
                    Text("Bệnh", modifier = Modifier.padding(horizontal = 8.dp))
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Confidence:",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.width(8.dp))

                LinearProgressIndicator(
                    progress = result.confidence,
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "${(result.confidence * 100).toInt()}%",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = result.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
            )
        }
    }

    Spacer(modifier = Modifier.height(16.dp))

    Button(
        onClick = onNavigateToChat,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text("Nhận tư vấn chi tiết")
    }

    Spacer(modifier = Modifier.height(8.dp))

    OutlinedButton(
        onClick = { /* Save history */ },
        modifier = Modifier.fillMaxWidth()
    ) {
        Text("Lưu lịch sử")
    }
}
