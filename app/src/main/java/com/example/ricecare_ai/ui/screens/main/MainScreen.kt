package com.example.ricecare_ai.ui.screens.main

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.ricecare_ai.ui.screens.chat.ChatScreen
import com.example.ricecare_ai.ui.screens.dashboard.DashboardScreen
import com.example.ricecare_ai.ui.screens.history.HistoryScreen
import com.example.ricecare_ai.ui.screens.upload.UploadScreen
import com.example.ricecare_ai.viewmodel.SharedViewModels
import kotlinx.coroutines.launch

sealed class BottomNavItem(
    val route: String,
    val title: String,
    val icon: ImageVector,
    val index: Int
) {
    object Dashboard : BottomNavItem("dashboard", "Trang chủ", Icons.Default.Home, 0)
    object Upload : BottomNavItem("upload", "Phân tích", Icons.Default.Add, 1)
    object Chat : BottomNavItem("chat", "Tư vấn", Icons.Default.Create, 2)
    object History : BottomNavItem("history", "Lịch sử", Icons.Default.Info, 3)
}

@Composable
fun MainScreen(
    initialPredictionId: String? = null,
    onNavigateToDetail: (String) -> Unit = {},
    onNavigateToSettings: () -> Unit = {},
    onNavigateToChatWithPrediction: (String) -> Unit = {}
) {
    val authViewModel = SharedViewModels.getAuthViewModel()
    val authState by authViewModel.authState.collectAsState()
    
    val items = listOf(
        BottomNavItem.Dashboard,
        BottomNavItem.Upload,
        BottomNavItem.Chat,
        BottomNavItem.History
    )
    
    var selectedTab by remember { mutableIntStateOf(if (initialPredictionId != null) 2 else 0) }
    var pendingPredictionId by remember { mutableStateOf(initialPredictionId) }
    val coroutineScope = rememberCoroutineScope()
    
    // Refresh token periodically
    LaunchedEffect(Unit) {
        authViewModel.refreshToken { }
    }

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp
            ) {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.title
                            )
                        },
                        label = { Text(item.title) },
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (selectedTab) {
                0 -> DashboardScreen(
                    onNavigateToUpload = { selectedTab = 1 },
                    onNavigateToChat = { selectedTab = 2 },
                    onNavigateToHistory = { selectedTab = 3 },
                    onNavigateToSettings = onNavigateToSettings,
                    authToken = authState.token,
                    userName = authState.displayName ?: "User"
                )
                1 -> UploadScreen(
                    onNavigateToChatWithPrediction = { predictionId ->
                        pendingPredictionId = predictionId
                        selectedTab = 2 // Switch to Chat tab
                    },
                    onNavigateBack = { selectedTab = 0 },
                    onNavigateToDashboard = { selectedTab = 0 },
                    onNavigateToHistory = { selectedTab = 3 },
                    authToken = authState.token
                )
                2 -> ChatScreen(
                    predictionId = pendingPredictionId?.also { pendingPredictionId = null },
                    onNavigateBack = { selectedTab = 0 }
                )
                3 -> HistoryScreen(
                    onNavigateToDetail = onNavigateToDetail,
                    onNavigateBack = { selectedTab = 0 },
                    onNavigateToDashboard = { selectedTab = 0 },
                    onNavigateToUpload = { selectedTab = 1 },
                    onNavigateToChat = { selectedTab = 2 },
                    authToken = authState.token
                )
            }
        }
    }
}
