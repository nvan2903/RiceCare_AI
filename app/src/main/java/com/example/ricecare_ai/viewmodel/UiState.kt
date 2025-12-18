package com.example.ricecare_ai.viewmodel

import android.net.Uri
import com.example.ricecare_ai.data.model.*

/**
 * Base sealed class for UI states
 */
sealed class UiState<out T> {
    object Idle : UiState<Nothing>()
    object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()
}

/**
 * Prediction Screen UI State
 */
data class PredictionUiState(
    val selectedImageUri: Uri? = null,
    val isAnalyzing: Boolean = false,
    val predictionResult: PredictionResponse? = null,
    val error: String? = null,
    val showResult: Boolean = false
)

/**
 * Dashboard UI State
 */
data class DashboardUiState(
    val isLoading: Boolean = false,
    val userName: String = "User",
    val totalPredictions: Int = 0,
    val healthyCount: Int = 0,
    val diseasedCount: Int = 0,
    val recentPredictions: List<PredictionHistory> = emptyList(),
    val error: String? = null
)

/**
 * History UI State
 */
data class HistoryUiState(
    val isLoading: Boolean = false,
    val predictions: List<PredictionHistory> = emptyList(),
    val selectedFilter: String = "Tất cả",
    val error: String? = null
)

/**
 * History Detail UI State
 */
data class HistoryDetailUiState(
    val isLoading: Boolean = false,
    val prediction: PredictionHistory? = null,
    val error: String? = null
)

/**
 * User Profile UI State
 */
data class ProfileUiState(
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val profile: UserProfile? = null,
    val error: String? = null,
    val saveSuccess: Boolean = false
)

/**
 * Auth State
 */
data class AuthState(
    val isLoggedIn: Boolean = false,
    val isLoading: Boolean = false,
    val userId: String? = null,
    val userEmail: String? = null,
    val displayName: String? = null,
    val photoUrl: String? = null,  // Google profile picture URL
    val token: String? = null,
    val error: String? = null
)
