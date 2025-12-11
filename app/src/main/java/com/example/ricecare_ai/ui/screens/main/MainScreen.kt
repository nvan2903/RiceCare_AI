package com.example.ricecare_ai.ui.screens.main

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.ricecare_ai.ui.screens.chat.ChatScreenContent
import com.example.ricecare_ai.ui.screens.dashboard.DashboardScreenContent
import com.example.ricecare_ai.ui.screens.history.HistoryScreenContent
import com.example.ricecare_ai.ui.screens.upload.UploadScreenContent
import kotlinx.coroutines.launch

sealed class BottomNavItem(
    val route: String,
    val title: String,
    val icon: ImageVector,
    val index: Int
) {
    object Dashboard : BottomNavItem("dashboard", "Dashboard", Icons.Default.Home, 0)
    object Upload : BottomNavItem("upload", "Upload", Icons.Default.Add, 1)
    object Chat : BottomNavItem("chat", "Chat", Icons.Default.Create, 2)
    object History : BottomNavItem("history", "Lịch sử", Icons.Default.Info, 3)
}

@Composable
fun MainScreen(
    onNavigateToDetail: (String) -> Unit = {},
    onNavigateToSettings: () -> Unit = {}
) {
    val items = listOf(
        BottomNavItem.Dashboard,
        BottomNavItem.Upload,
        BottomNavItem.Chat,
        BottomNavItem.History
    )

    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { items.size }
    )

    val coroutineScope = rememberCoroutineScope()

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
                        selected = pagerState.currentPage == index,
                        onClick = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        },
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

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            userScrollEnabled = true
        ) { page ->
            when (page) {
                0 -> DashboardScreenContent(onNavigateToSettings = onNavigateToSettings)
                1 -> UploadScreenContent()
                2 -> ChatScreenContent()
                3 -> HistoryScreenContent(onNavigateToDetail = onNavigateToDetail)
            }
        }
    }
}
