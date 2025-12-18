package com.example.ricecare_ai.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.ricecare_ai.data.model.PredictionResponse
import com.example.ricecare_ai.data.repository.PredictionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel for disease prediction screen
 */
class PredictionViewModel(application: Application) : AndroidViewModel(application) {
    
    private val repository = PredictionRepository(application.applicationContext)
    
    private val _uiState = MutableStateFlow(PredictionUiState())
    val uiState: StateFlow<PredictionUiState> = _uiState.asStateFlow()
    
    private var authToken: String? = null
    
    /**
     * Set authentication token for API calls
     */
    fun setAuthToken(token: String?) {
        authToken = token
    }
    
    /**
     * Select image for analysis
     */
    fun selectImage(uri: Uri) {
        _uiState.update { 
            it.copy(
                selectedImageUri = uri,
                predictionResult = null,
                error = null,
                showResult = false
            ) 
        }
    }
    
    /**
     * Clear selected image
     */
    fun clearImage() {
        _uiState.update { 
            PredictionUiState() 
        }
    }
    
    /**
     * Analyze selected image for disease
     */
    fun analyzeImage(saveHistory: Boolean = false) {
        val imageUri = _uiState.value.selectedImageUri ?: return
        
        viewModelScope.launch {
            _uiState.update { it.copy(isAnalyzing = true, error = null) }
            
            repository.predictDisease(
                imageUri = imageUri,
                token = authToken,
                saveHistory = saveHistory
            ).onSuccess { result ->
                _uiState.update { 
                    it.copy(
                        isAnalyzing = false,
                        predictionResult = result,
                        showResult = true
                    ) 
                }
            }.onFailure { exception ->
                _uiState.update { 
                    it.copy(
                        isAnalyzing = false,
                        error = exception.message ?: "Prediction failed"
                    ) 
                }
            }
        }
    }
    
    /**
     * Reset for new prediction
     */
    fun resetPrediction() {
        _uiState.update { PredictionUiState() }
    }
    
    /**
     * Dismiss error
     */
    fun dismissError() {
        _uiState.update { it.copy(error = null) }
    }
}
