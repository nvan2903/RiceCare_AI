package com.example.ricecare_ai.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Login : Screen("login")
    object Dashboard : Screen("dashboard") {
        fun createRouteWithPrediction(predictionId: String) = "dashboard?predictionId=$predictionId"
    }
    object Upload : Screen("upload")
    object Chat : Screen("chat")
    object ChatDetail : Screen("chat/{conversationId}") {
        fun createRoute(conversationId: String) = "chat/$conversationId"
    }
    object ChatFromPrediction : Screen("chat/prediction/{predictionId}") {
        fun createRoute(predictionId: String) = "chat/prediction/$predictionId"
    }
    object History : Screen("history")
    object HistoryDetail : Screen("history_detail/{id}") {
        fun createRoute(id: String) = "history_detail/$id"
    }
    object Diseases : Screen("diseases")
    object DiseaseDetail : Screen("diseases/{diseaseClass}") {
        fun createRoute(diseaseClass: String) = "diseases/$diseaseClass"
    }
    object Settings : Screen("settings")
    object Account : Screen("account")
    object About : Screen("about")
    object Privacy : Screen("privacy")
}
