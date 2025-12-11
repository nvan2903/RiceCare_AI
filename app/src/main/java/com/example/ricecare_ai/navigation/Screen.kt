package com.example.ricecare_ai.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Login : Screen("login")
    object Dashboard : Screen("dashboard")
    object Upload : Screen("upload")
    object Chat : Screen("chat")
    object History : Screen("history")
    object HistoryDetail : Screen("history_detail/{id}") {
        fun createRoute(id: String) = "history_detail/$id"
    }
    object Settings : Screen("settings")
    object Account : Screen("account")
    object About : Screen("about")
    object Privacy : Screen("privacy")
}
