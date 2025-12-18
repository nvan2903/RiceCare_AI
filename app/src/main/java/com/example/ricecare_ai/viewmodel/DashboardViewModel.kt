package com.example.ricecare_ai.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ricecare_ai.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel for dashboard screen
 */
class DashboardViewModel : ViewModel() {
    
    private val userRepository = UserRepository()
    
    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()
    
    private var authToken: String? = null
    
    /**
     * Set authentication token
     */
    fun setAuthToken(token: String?) {
        authToken = token
    }
    
    /**
     * Set user name for display
     */
    fun setUserName(name: String) {
        _uiState.update { it.copy(userName = name) }
    }
    
    /**
     * Load dashboard data from API
     */
    fun loadDashboardData() {
        val token = authToken ?: return
        
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            
            userRepository.getDashboardStats(token)
                .onSuccess { stats ->
                    _uiState.update { 
                        it.copy(
                            isLoading = false,
                            totalPredictions = stats.totalPredictions,
                            healthyCount = stats.healthyCount,
                            diseasedCount = stats.diseasedCount,
                            recentPredictions = stats.recentPredictions
                        ) 
                    }
                }
                .onFailure { exception ->
                    _uiState.update { 
                        it.copy(
                            isLoading = false,
                            error = exception.message ?: "Failed to load dashboard"
                        ) 
                    }
                }
        }
    }
    
    /**
     * Refresh dashboard data
     */
    fun refresh() {
        loadDashboardData()
    }
    
    /**
     * Dismiss error
     */
    fun dismissError() {
        _uiState.update { it.copy(error = null) }
    }
    
    /**
     * Calculate health percentage for chart
     */
    fun getHealthPercentage(): Float {
        val total = _uiState.value.totalPredictions
        if (total == 0) return 0f
        return (_uiState.value.healthyCount.toFloat() / total) * 100
    }
    
    /**
     * Calculate disease percentage for chart
     */
    fun getDiseasePercentage(): Float {
        val total = _uiState.value.totalPredictions
        if (total == 0) return 0f
        return (_uiState.value.diseasedCount.toFloat() / total) * 100
    }
}
