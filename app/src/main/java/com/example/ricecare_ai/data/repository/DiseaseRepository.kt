package com.example.ricecare_ai.data.repository

import com.example.ricecare_ai.data.api.RetrofitInstance
import com.example.ricecare_ai.data.model.DiseaseInfo
import com.example.ricecare_ai.data.model.DiseasesListResponse

/**
 * Repository for disease information operations
 */
class DiseaseRepository {
    
    private val api = RetrofitInstance.apiService
    
    /**
     * Get all diseases information
     */
    suspend fun getAllDiseases(): Result<DiseasesListResponse> {
        return try {
            val response = api.getAllDiseases()
            
            if (response.isSuccessful && response.body() != null) {
                val apiResponse = response.body()!!
                if (apiResponse.success && apiResponse.data != null) {
                    Result.success(apiResponse.data)
                } else {
                    Result.failure(Exception(apiResponse.error ?: "Failed to get diseases"))
                }
            } else {
                Result.failure(Exception("API Error: ${response.code()}"))
            }
            
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Get specific disease detail
     */
    suspend fun getDiseaseDetail(diseaseClass: String): Result<DiseaseInfo> {
        return try {
            val response = api.getDiseaseDetail(diseaseClass)
            
            if (response.isSuccessful && response.body() != null) {
                val apiResponse = response.body()!!
                if (apiResponse.success && apiResponse.data != null) {
                    Result.success(apiResponse.data)
                } else {
                    Result.failure(Exception(apiResponse.error ?: "Failed to get disease detail"))
                }
            } else {
                Result.failure(Exception("API Error: ${response.code()}"))
            }
            
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
