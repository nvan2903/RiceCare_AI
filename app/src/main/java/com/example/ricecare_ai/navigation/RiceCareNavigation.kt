package com.example.ricecare_ai.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.ricecare_ai.ui.screens.chat.ChatScreen
import com.example.ricecare_ai.ui.screens.history.HistoryDetailScreen
import com.example.ricecare_ai.ui.screens.login.LoginScreen
import com.example.ricecare_ai.ui.screens.main.MainScreen
import com.example.ricecare_ai.ui.screens.settings.SettingsScreen
import com.example.ricecare_ai.ui.screens.settings.AccountScreen
import com.example.ricecare_ai.ui.screens.settings.AboutScreen
import com.example.ricecare_ai.ui.screens.settings.PrivacyPolicyScreen
import com.example.ricecare_ai.ui.splash.SplashScreen
import com.example.ricecare_ai.viewmodel.SharedViewModels

@Composable
fun RiceCareNavigation(navController: NavHostController) {
    val authViewModel = SharedViewModels.getAuthViewModel()
    val authState by authViewModel.authState.collectAsState()
    
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(
                onTimeout = {
                    // Check if already logged in
                    if (authState.isLoggedIn) {
                        navController.navigate(Screen.Dashboard.route) {
                            popUpTo(Screen.Splash.route) { inclusive = true }
                        }
                    } else {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(Screen.Splash.route) { inclusive = true }
                        }
                    }
                }
            )
        }

        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = "dashboard?predictionId={predictionId}",
            arguments = listOf(navArgument("predictionId") { 
                type = NavType.StringType
                nullable = true
                defaultValue = null
            })
        ) { backStackEntry ->
            val predictionId = backStackEntry.arguments?.getString("predictionId")
            MainScreen(
                initialPredictionId = predictionId,
                onNavigateToDetail = { id ->
                    navController.navigate(Screen.HistoryDetail.createRoute(id))
                },
                onNavigateToSettings = {
                    navController.navigate(Screen.Settings.route)
                },
                onNavigateToChatWithPrediction = { /* Not used anymore */ }
            )
        }

        composable(
            route = Screen.HistoryDetail.route,
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id") ?: ""
            HistoryDetailScreen(
                id = id,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToChat = {
                    // Pop to dashboard with predictionId
                    navController.navigate("dashboard?predictionId=$id") {
                        popUpTo(Screen.Dashboard.route) { inclusive = true }
                    }
                },
                authToken = authState.token
            )
        }
        
        // Chat screens
        composable(Screen.Chat.route) {
            ChatScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        composable(
            route = Screen.ChatDetail.route,
            arguments = listOf(navArgument("conversationId") { type = NavType.StringType })
        ) { backStackEntry ->
            val conversationId = backStackEntry.arguments?.getString("conversationId") ?: ""
            ChatScreen(
                conversationId = conversationId,
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        composable(
            route = Screen.ChatFromPrediction.route,
            arguments = listOf(navArgument("predictionId") { type = NavType.StringType })
        ) { backStackEntry ->
            val predictionId = backStackEntry.arguments?.getString("predictionId") ?: ""
            ChatScreen(
                predictionId = predictionId,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Settings.route) {
            SettingsScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToAccount = { navController.navigate(Screen.Account.route) },
                onNavigateToAbout = { navController.navigate(Screen.About.route) },
                onNavigateToPrivacy = { navController.navigate(Screen.Privacy.route) }
            )
        }

        composable(Screen.Account.route) {
            val authState by authViewModel.authState.collectAsState()
            AccountScreen(
                onNavigateBack = { navController.popBackStack() },
                onLogout = {
                    authViewModel.signOut()
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                authState = authState
            )
        }

        composable(Screen.About.route) {
            AboutScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Privacy.route) {
            PrivacyPolicyScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
