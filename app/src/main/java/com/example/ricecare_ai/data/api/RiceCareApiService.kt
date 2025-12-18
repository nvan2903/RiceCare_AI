package com.example.ricecare_ai.data.api

import com.example.ricecare_ai.data.model.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

/**
 * Retrofit API Service Interface
 * Complete API implementation matching backend documentation
 */
interface RiceCareApiService {
    
    // =============== Authentication APIs ===============
    
    @POST("api/v1/auth/google")
    suspend fun googleSignIn(
        @Body request: GoogleSignInRequest
    ): Response<ApiResponse<AuthResponse>>
    
    @POST("api/v1/auth/refresh")
    suspend fun refreshToken(
        @Header("Authorization") token: String
    ): Response<ApiResponse<AuthResponse>>
    
    @POST("api/v1/auth/logout")
    suspend fun logout(
        @Header("Authorization") token: String
    ): Response<ApiResponse<LogoutResponse>>
    
    @GET("api/v1/auth/verify")
    suspend fun verifyToken(
        @Header("Authorization") token: String
    ): Response<ApiResponse<TokenVerifyResponse>>
    
    // =============== Prediction APIs ===============
    
    @Multipart
    @POST("api/v1/predictions/predict")
    suspend fun predictDisease(
        @Part file: MultipartBody.Part,
        @Part("save_history") saveHistory: RequestBody? = null,
        @Header("Authorization") token: String? = null
    ): Response<ApiResponse<PredictionResponse>>
    
    @GET("api/v1/predictions/history")
    suspend fun getPredictionHistory(
        @Query("limit") limit: Int = 50,
        @Header("Authorization") token: String
    ): Response<ApiResponse<HistoryListResponse>>
    
    @GET("api/v1/predictions/history/{id}")
    suspend fun getPredictionDetail(
        @Path("id") predictionId: String,
        @Header("Authorization") token: String? = null
    ): Response<ApiResponse<PredictionHistory>>
    
    @DELETE("api/v1/predictions/history/{id}")
    suspend fun deletePrediction(
        @Path("id") predictionId: String,
        @Header("Authorization") token: String
    ): Response<ApiResponse<DeleteResponse>>
    
    @GET("api/v1/predictions/history/{id}/export-pdf")
    @Streaming
    suspend fun exportPredictionPdf(
        @Path("id") predictionId: String,
        @Header("Authorization") token: String
    ): Response<ResponseBody>
    
    @POST("api/v1/predictions/{id}/create-chat")
    suspend fun createChatFromPrediction(
        @Path("id") predictionId: String,
        @Header("Authorization") token: String
    ): Response<ApiResponse<CreateChatResponse>>
    
    @GET("api/v1/predictions/model-info")
    suspend fun getModelInfo(): Response<ApiResponse<ModelInfo>>
    
    // =============== Chat APIs ===============
    
    @GET("api/v1/chat/conversations")
    suspend fun getConversations(
        @Query("limit") limit: Int = 20,
        @Query("offset") offset: Int = 0,
        @Header("Authorization") token: String
    ): Response<ApiResponse<ConversationsResponse>>
    
    @POST("api/v1/chat/conversations")
    suspend fun createConversation(
        @Body request: CreateConversationRequest,
        @Header("Authorization") token: String
    ): Response<ApiResponse<Conversation>>
    
    @GET("api/v1/chat/conversations/{id}")
    suspend fun getConversation(
        @Path("id") conversationId: String,
        @Header("Authorization") token: String
    ): Response<ApiResponse<ConversationDetail>>
    
    @DELETE("api/v1/chat/conversations/{id}")
    suspend fun deleteConversation(
        @Path("id") conversationId: String,
        @Header("Authorization") token: String
    ): Response<ApiResponse<DeleteResponse>>
    
    @GET("api/v1/chat/conversations/{id}/messages")
    suspend fun getMessages(
        @Path("id") conversationId: String,
        @Query("limit") limit: Int = 50,
        @Header("Authorization") token: String
    ): Response<ApiResponse<MessagesResponse>>
    
    @POST("api/v1/chat/conversations/{id}/messages")
    @Streaming
    suspend fun sendMessage(
        @Path("id") conversationId: String,
        @Body request: SendMessageRequest,
        @Header("Authorization") token: String
    ): Response<ResponseBody>
    
    @POST("api/v1/chat/from-prediction/{id}")
    suspend fun createChatFromPredictionAlt(
        @Path("id") predictionId: String,
        @Header("Authorization") token: String
    ): Response<ApiResponse<Conversation>>
    
    // =============== User APIs ===============
    
    @GET("api/v1/user/profile")
    suspend fun getUserProfile(
        @Header("Authorization") token: String
    ): Response<ApiResponse<UserProfile>>
    
    @PUT("api/v1/user/profile")
    suspend fun updateUserProfile(
        @Body profile: UpdateProfileRequest,
        @Header("Authorization") token: String
    ): Response<ApiResponse<UserProfile>>
    
    @GET("api/v1/user/dashboard")
    suspend fun getDashboardStats(
        @Header("Authorization") token: String
    ): Response<ApiResponse<DashboardStats>>
    
    // =============== Disease Info APIs ===============
    
    @GET("api/v1/diseases")
    suspend fun getAllDiseases(): Response<ApiResponse<DiseasesListResponse>>
    
    @GET("api/v1/diseases/{class}")
    suspend fun getDiseaseDetail(
        @Path("class") diseaseClass: String
    ): Response<ApiResponse<DiseaseInfo>>
    
    @GET("api/v1/diseases/severity/{level}")
    suspend fun getDiseasesBySeverity(
        @Path("level") severityLevel: String
    ): Response<ApiResponse<DiseasesListResponse>>
    
    @GET("api/v1/diseases/search/{query}")
    suspend fun searchDiseases(
        @Path("query") query: String
    ): Response<ApiResponse<DiseasesListResponse>>
    
    // =============== Health Check ===============
    
    @GET("health")
    suspend fun healthCheck(): Response<HealthResponse>
}
