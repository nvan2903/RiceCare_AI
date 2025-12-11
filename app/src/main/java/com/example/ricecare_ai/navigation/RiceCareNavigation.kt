package com.example.ricecare_ai.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.ricecare_ai.ui.screens.history.HistoryDetailScreen
import com.example.ricecare_ai.ui.screens.login.LoginScreen
import com.example.ricecare_ai.ui.screens.main.MainScreen
import com.example.ricecare_ai.ui.screens.settings.SettingsScreen
import com.example.ricecare_ai.ui.screens.settings.AccountScreen
import com.example.ricecare_ai.ui.screens.settings.AboutScreen
import com.example.ricecare_ai.ui.screens.settings.PrivacyPolicyScreen
import com.example.ricecare_ai.ui.splash.SplashScreen

@Composable
fun RiceCareNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(
                onTimeout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
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

        composable(Screen.Dashboard.route) {
            MainScreen(
                onNavigateToDetail = { id ->
                    navController.navigate(Screen.HistoryDetail.createRoute(id))
                },
                onNavigateToSettings = {
                    navController.navigate(Screen.Settings.route)
                }
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
                onNavigateToChat = { /* Có thể thêm navigate nếu cần */ }
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
            AccountScreen(
                onNavigateBack = { navController.popBackStack() },
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
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
