package com.example.ricecare_ai.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * Factory for creating ViewModels with Application context
 */
class ViewModelFactory(
    private val application: Application
) : ViewModelProvider.Factory {
    
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(PredictionViewModel::class.java) -> {
                PredictionViewModel(application) as T
            }
            modelClass.isAssignableFrom(HistoryViewModel::class.java) -> {
                HistoryViewModel(application) as T
            }
            modelClass.isAssignableFrom(DashboardViewModel::class.java) -> {
                DashboardViewModel() as T
            }
            modelClass.isAssignableFrom(AuthViewModel::class.java) -> {
                AuthViewModel() as T
            }
            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                ProfileViewModel() as T
            }
            modelClass.isAssignableFrom(ChatViewModel::class.java) -> {
                ChatViewModel() as T
            }
            modelClass.isAssignableFrom(DiseaseViewModel::class.java) -> {
                DiseaseViewModel() as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}

/**
 * Singleton object to provide shared ViewModels across the app
 */
object SharedViewModels {
    private var authViewModel: AuthViewModel? = null
    private var dashboardViewModel: DashboardViewModel? = null
    private var chatViewModel: ChatViewModel? = null
    private var diseaseViewModel: DiseaseViewModel? = null
    
    fun getAuthViewModel(): AuthViewModel {
        if (authViewModel == null) {
            authViewModel = AuthViewModel()
        }
        return authViewModel!!
    }
    
    fun getDashboardViewModel(): DashboardViewModel {
        if (dashboardViewModel == null) {
            dashboardViewModel = DashboardViewModel()
        }
        return dashboardViewModel!!
    }
    
    fun getChatViewModel(): ChatViewModel {
        if (chatViewModel == null) {
            chatViewModel = ChatViewModel()
        }
        return chatViewModel!!
    }
    
    fun getDiseaseViewModel(): DiseaseViewModel {
        if (diseaseViewModel == null) {
            diseaseViewModel = DiseaseViewModel()
        }
        return diseaseViewModel!!
    }
    
    fun clear() {
        authViewModel = null
        dashboardViewModel = null
        chatViewModel = null
        diseaseViewModel = null
    }
}
