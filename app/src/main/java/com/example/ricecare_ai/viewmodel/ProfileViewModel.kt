package com.example.ricecare_ai.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ricecare_ai.data.model.UserProfile
import com.example.ricecare_ai.data.model.UpdateProfileRequest
import com.example.ricecare_ai.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel for user profile screen
 */
class ProfileViewModel : ViewModel() {
    
    private val repository = UserRepository()
    
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()
    
    private var authToken: String? = null
    
    /**
     * Set authentication token
     */
    fun setAuthToken(token: String?) {
        authToken = token
    }
    
    /**
     * Load user profile
     */
    fun loadProfile() {
        val token = authToken ?: return
        
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            
            repository.getUserProfile(token)
                .onSuccess { profile ->
                    _uiState.update { 
                        it.copy(
                            isLoading = false,
                            profile = profile
                        ) 
                    }
                }
                .onFailure { exception ->
                    _uiState.update { 
                        it.copy(
                            isLoading = false,
                            error = exception.message ?: "Failed to load profile"
                        ) 
                    }
                }
        }
    }
    
    /**
     * Update user profile
     */
    fun updateProfile(profile: UserProfile) {
        val token = authToken ?: return
        
        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, error = null, saveSuccess = false) }
            
            // Convert UserProfile to UpdateProfileRequest
            val request = UpdateProfileRequest(
                displayName = profile.displayName,
                phoneNumber = profile.phoneNumber,
                farmLocation = profile.farmLocation,
                farmSize = profile.farmSize,
                notificationEnabled = profile.notificationEnabled,
                settings = profile.settings
            )
            
            repository.updateUserProfile(token, request)
                .onSuccess { updatedProfile ->
                    _uiState.update { 
                        it.copy(
                            isSaving = false,
                            profile = updatedProfile,
                            saveSuccess = true
                        ) 
                    }
                }
                .onFailure { exception ->
                    _uiState.update { 
                        it.copy(
                            isSaving = false,
                            error = exception.message ?: "Failed to update profile"
                        ) 
                    }
                }
        }
    }
    
    /**
     * Update display name
     */
    fun updateDisplayName(name: String) {
        _uiState.value.profile?.let { profile ->
            _uiState.update { 
                it.copy(profile = profile.copy(displayName = name)) 
            }
        }
    }
    
    /**
     * Update phone number
     */
    fun updatePhoneNumber(phone: String) {
        _uiState.value.profile?.let { profile ->
            _uiState.update { 
                it.copy(profile = profile.copy(phoneNumber = phone)) 
            }
        }
    }
    
    /**
     * Update farm location
     */
    fun updateFarmLocation(location: String) {
        _uiState.value.profile?.let { profile ->
            _uiState.update { 
                it.copy(profile = profile.copy(farmLocation = location)) 
            }
        }
    }
    
    /**
     * Update farm size
     */
    fun updateFarmSize(size: Float?) {
        _uiState.value.profile?.let { profile ->
            _uiState.update { 
                it.copy(profile = profile.copy(farmSize = size)) 
            }
        }
    }
    
    /**
     * Toggle notification setting
     */
    fun toggleNotification(enabled: Boolean) {
        _uiState.value.profile?.let { profile ->
            _uiState.update { 
                it.copy(profile = profile.copy(notificationEnabled = enabled)) 
            }
        }
    }
    
    /**
     * Save profile changes
     */
    fun saveChanges() {
        _uiState.value.profile?.let { profile ->
            updateProfile(profile)
        }
    }
    
    /**
     * Dismiss error
     */
    fun dismissError() {
        _uiState.update { it.copy(error = null) }
    }
    
    /**
     * Reset save success flag
     */
    fun resetSaveSuccess() {
        _uiState.update { it.copy(saveSuccess = false) }
    }
}
