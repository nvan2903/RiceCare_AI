package com.example.ricecare_ai.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.ricecare_ai.data.model.PredictionHistory
import com.example.ricecare_ai.data.repository.PredictionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel for prediction history screen
 */
class HistoryViewModel(application: Application) : AndroidViewModel(application) {
    
    private val repository = PredictionRepository(application.applicationContext)
    
    private val _uiState = MutableStateFlow(HistoryUiState())
    val uiState: StateFlow<HistoryUiState> = _uiState.asStateFlow()
    
    private val _detailState = MutableStateFlow(HistoryDetailUiState())
    val detailState: StateFlow<HistoryDetailUiState> = _detailState.asStateFlow()
    
    private var authToken: String? = null
    private var allPredictions: List<PredictionHistory> = emptyList()
    
    /**
     * Set authentication token
     */
    fun setAuthToken(token: String?) {
        authToken = token
    }
    
    /**
     * Load prediction history
     */
    fun loadHistory(limit: Int = 50) {
        val token = authToken ?: return
        
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            
            repository.getPredictionHistory(token, limit)
                .onSuccess { predictions ->
                    allPredictions = predictions
                    _uiState.update { 
                        it.copy(
                            isLoading = false,
                            predictions = filterPredictions(predictions, it.selectedFilter)
                        ) 
                    }
                }
                .onFailure { exception ->
                    _uiState.update { 
                        it.copy(
                            isLoading = false,
                            error = exception.message ?: "Failed to load history"
                        ) 
                    }
                }
        }
    }
    
    /**
     * Load prediction detail
     */
    fun loadPredictionDetail(predictionId: String) {
        viewModelScope.launch {
            _detailState.update { it.copy(isLoading = true, error = null) }
            
            repository.getPredictionDetail(predictionId, authToken)
                .onSuccess { prediction ->
                    _detailState.update { 
                        it.copy(
                            isLoading = false,
                            prediction = prediction
                        ) 
                    }
                }
                .onFailure { exception ->
                    _detailState.update { 
                        it.copy(
                            isLoading = false,
                            error = exception.message ?: "Failed to load prediction"
                        ) 
                    }
                }
        }
    }
    
    /**
     * Set filter option
     */
    fun setFilter(filter: String) {
        _uiState.update { 
            it.copy(
                selectedFilter = filter,
                predictions = filterPredictions(allPredictions, filter)
            ) 
        }
    }
    
    /**
     * Filter predictions based on selected filter
     */
    private fun filterPredictions(
        predictions: List<PredictionHistory>,
        filter: String
    ): List<PredictionHistory> {
        return when (filter) {
            "Tất cả" -> predictions
            "Đạo ôn" -> predictions.filter { 
                it.predictedClass.contains("blast", ignoreCase = true) ||
                (it.diseaseInfo?.nameVi?.contains("đạo ôn", ignoreCase = true) == true)
            }
            "Bạc lá" -> predictions.filter { 
                it.predictedClass.contains("blight", ignoreCase = true) ||
                (it.diseaseInfo?.nameVi?.contains("bạc lá", ignoreCase = true) == true)
            }
            "Khỏe" -> predictions.filter { 
                it.predictedClass.contains("healthy", ignoreCase = true) ||
                (it.diseaseInfo?.nameVi?.contains("khỏe", ignoreCase = true) == true)
            }
            else -> predictions
        }
    }
    
    /**
     * Refresh history
     */
    fun refresh() {
        loadHistory()
    }
    
    /**
     * Dismiss error
     */
    fun dismissError() {
        _uiState.update { it.copy(error = null) }
    }
    
    /**
     * Clear detail state
     */
    fun clearDetailState() {
        _detailState.update { HistoryDetailUiState() }
    }
    
    /**
     * Delete prediction
     */
    fun deletePrediction(predictionId: String, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            val token = authToken ?: return@launch
            
            repository.deletePrediction(predictionId, token)
                .onSuccess {
                    // Remove from local list
                    allPredictions = allPredictions.filter { it.id != predictionId }
                    _uiState.update {
                        it.copy(
                            predictions = filterPredictions(allPredictions, it.selectedFilter)
                        )
                    }
                    onSuccess()
                }
                .onFailure { exception ->
                    _uiState.update { 
                        it.copy(error = exception.message ?: "Failed to delete prediction") 
                    }
                }
        }
    }
}
