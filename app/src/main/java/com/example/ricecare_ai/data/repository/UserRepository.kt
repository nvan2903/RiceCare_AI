package com.example.ricecare_ai.data.repository

import com.example.ricecare_ai.data.api.RetrofitInstance
import com.example.ricecare_ai.data.model.*

/**
 * Repository for user profile and dashboard operations
 */
class UserRepository {
    
    private val api = RetrofitInstance.apiService
    
    /**
     * Get user profile
     */
    suspend fun getUserProfile(token: String): Result<UserProfile> {
        return try {
            val response = api.getUserProfile("Bearer $token")
            
            if (response.isSuccessful && response.body() != null) {
                val apiResponse = response.body()!!
                if (apiResponse.success && apiResponse.data != null) {
                    Result.success(apiResponse.data)
                } else {
                    Result.failure(Exception(apiResponse.error ?: "Failed to get profile"))
                }
            } else {
                Result.failure(Exception("API Error: ${response.code()}"))
            }
            
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Update user profile
     */
    suspend fun updateUserProfile(token: String, request: UpdateProfileRequest): Result<UserProfile> {
        return try {
            val response = api.updateUserProfile(request, "Bearer $token")
            
            if (response.isSuccessful && response.body() != null) {
                val apiResponse = response.body()!!
                if (apiResponse.success && apiResponse.data != null) {
                    Result.success(apiResponse.data)
                } else {
                    Result.failure(Exception(apiResponse.error ?: "Failed to update profile"))
                }
            } else {
                Result.failure(Exception("API Error: ${response.code()}"))
            }
            
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Helper to convert UserProfile to UpdateProfileRequest
     */
    suspend fun updateUserProfileFromProfile(token: String, profile: UserProfile): Result<UserProfile> {
        val request = UpdateProfileRequest(
            displayName = profile.displayName,
            phoneNumber = profile.phoneNumber,
            farmLocation = profile.farmLocation,
            farmSize = profile.farmSize,
            notificationEnabled = profile.notificationEnabled,
            settings = profile.settings
        )
        return updateUserProfile(token, request)
    }
    
    /**
     * Get dashboard statistics
     */
    suspend fun getDashboardStats(token: String): Result<DashboardStats> {
        return try {
            val response = api.getDashboardStats("Bearer $token")
            
            if (response.isSuccessful && response.body() != null) {
                val apiResponse = response.body()!!
                if (apiResponse.success && apiResponse.data != null) {
                    Result.success(apiResponse.data)
                } else {
                    Result.failure(Exception(apiResponse.error ?: "Failed to get dashboard stats"))
                }
            } else {
                Result.failure(Exception("API Error: ${response.code()}"))
            }
            
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
