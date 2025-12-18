package com.example.ricecare_ai.data.repository

import android.content.Context
import android.net.Uri
import com.example.ricecare_ai.data.api.RetrofitInstance
import com.example.ricecare_ai.data.model.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream

/**
 * Repository for disease prediction operations
 */
class PredictionRepository(private val context: Context) {
    
    private val api = RetrofitInstance.apiService
    
    /**
     * Predict disease from image URI
     */
    suspend fun predictDisease(
        imageUri: Uri,
        token: String? = null,
        saveHistory: Boolean = false
    ): Result<PredictionResponse> {
        return try {
            // Convert URI to file
            val file = uriToFile(imageUri)
            
            // Create multipart body
            val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
            val multipartBody = MultipartBody.Part.createFormData(
                "file",
                file.name,
                requestBody
            )
            
            // Create save_history param
            val saveHistoryBody = if (saveHistory) {
                "true".toRequestBody("text/plain".toMediaTypeOrNull())
            } else null
            
            // Make API call
            val authHeader = token?.let { "Bearer $it" }
            val response = api.predictDisease(multipartBody, saveHistoryBody, authHeader)
            
            // Clean up temp file
            file.delete()
            
            if (response.isSuccessful && response.body() != null) {
                val apiResponse = response.body()!!
                if (apiResponse.success && apiResponse.data != null) {
                    Result.success(apiResponse.data)
                } else {
                    Result.failure(Exception(apiResponse.error ?: "Prediction failed"))
                }
            } else {
                Result.failure(Exception("API Error: ${response.code()} - ${response.message()}"))
            }
            
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Get prediction history
     */
    suspend fun getPredictionHistory(
        token: String,
        limit: Int = 50
    ): Result<List<PredictionHistory>> {
        return try {
            val response = api.getPredictionHistory(limit, "Bearer $token")
            
            if (response.isSuccessful && response.body() != null) {
                val apiResponse = response.body()!!
                if (apiResponse.success && apiResponse.data != null) {
                    Result.success(apiResponse.data.predictions)
                } else {
                    Result.failure(Exception(apiResponse.error ?: "Failed to get history"))
                }
            } else {
                Result.failure(Exception("API Error: ${response.code()}"))
            }
            
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Get specific prediction detail
     */
    suspend fun getPredictionDetail(
        predictionId: String,
        token: String? = null
    ): Result<PredictionHistory> {
        return try {
            val authHeader = token?.let { "Bearer $it" }
            val response = api.getPredictionDetail(predictionId, authHeader)
            
            if (response.isSuccessful && response.body() != null) {
                val apiResponse = response.body()!!
                if (apiResponse.success && apiResponse.data != null) {
                    Result.success(apiResponse.data)
                } else {
                    Result.failure(Exception(apiResponse.error ?: "Failed to get prediction"))
                }
            } else {
                Result.failure(Exception("API Error: ${response.code()}"))
            }
            
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Get model information
     */
    suspend fun getModelInfo(): Result<ModelInfo> {
        return try {
            val response = api.getModelInfo()
            
            if (response.isSuccessful && response.body() != null) {
                val apiResponse = response.body()!!
                if (apiResponse.success && apiResponse.data != null) {
                    Result.success(apiResponse.data)
                } else {
                    Result.failure(Exception(apiResponse.error ?: "Failed to get model info"))
                }
            } else {
                Result.failure(Exception("API Error: ${response.code()}"))
            }
            
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Delete prediction from history
     */
    suspend fun deletePrediction(
        predictionId: String,
        token: String
    ): Result<Unit> {
        return try {
            val response = api.deletePrediction(predictionId, "Bearer $token")
            
            if (response.isSuccessful && response.body() != null) {
                val apiResponse = response.body()!!
                if (apiResponse.success) {
                    Result.success(Unit)
                } else {
                    Result.failure(Exception(apiResponse.error ?: "Failed to delete prediction"))
                }
            } else {
                Result.failure(Exception("API Error: ${response.code()}"))
            }
            
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Convert URI to temporary file
     */
    private fun uriToFile(uri: Uri): File {
        val contentResolver = context.contentResolver
        val tempFile = File.createTempFile("upload_", ".jpg", context.cacheDir)
        
        contentResolver.openInputStream(uri)?.use { input ->
            FileOutputStream(tempFile).use { output ->
                input.copyTo(output)
            }
        }
        
        return tempFile
    }
}
