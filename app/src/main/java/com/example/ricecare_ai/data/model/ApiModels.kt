package com.example.ricecare_ai.data.model

import com.google.gson.annotations.SerializedName

/**
 * API Response wrapper
 */
data class ApiResponse<T>(
    @SerializedName("success")
    val success: Boolean,
    
    @SerializedName("data")
    val data: T?,
    
    @SerializedName("message")
    val message: String?,
    
    @SerializedName("error")
    val error: String?
)

// =============== Authentication Models ===============

data class GoogleSignInRequest(
    @SerializedName("id_token")
    val idToken: String
)

data class AuthResponse(
    @SerializedName("user")
    val user: AuthUser?,
    
    @SerializedName("is_new_user")
    val isNewUser: Boolean = false,
    
    @SerializedName("token_valid")
    val tokenValid: Boolean = true
)

data class AuthUser(
    @SerializedName("uid")
    val uid: String,
    
    @SerializedName("email")
    val email: String?,
    
    @SerializedName("display_name")
    val displayName: String?,
    
    @SerializedName("photo_url")
    val photoUrl: String?,
    
    @SerializedName("provider")
    val provider: String = "google"
)

data class LogoutResponse(
    @SerializedName("logged_out")
    val loggedOut: Boolean
)

data class TokenVerifyResponse(
    @SerializedName("valid")
    val valid: Boolean,
    
    @SerializedName("uid")
    val uid: String?,
    
    @SerializedName("email")
    val email: String?
)

// =============== Prediction Models ===============

/**
 * Prediction Response from API
 */
data class PredictionResponse(
    @SerializedName("predicted_class")
    val predictedClass: String,
    
    @SerializedName("confidence")
    val confidence: Float,
    
    @SerializedName("disease_info")
    val diseaseInfo: DiseaseInfo,
    
    @SerializedName("top3")
    val top3: List<Top3Prediction>,
    
    @SerializedName("image_url")
    val imageUrl: String? = null,
    
    @SerializedName("prediction_id")
    val predictionId: String? = null
)

data class Top3Prediction(
    @SerializedName("class")
    val className: String,
    
    @SerializedName("confidence")
    val confidence: Float,
    
    @SerializedName("disease_info")
    val diseaseInfo: DiseaseInfo? = null
)

data class DiseaseInfo(
    @SerializedName("name")
    val name: String? = null,
    
    @SerializedName("name_vi")
    val nameVi: String? = null,
    
    @SerializedName("name_en")
    val nameEn: String? = null,
    
    @SerializedName("description")
    val description: String? = null,
    
    @SerializedName("symptoms")
    val symptoms: List<String>? = null,
    
    @SerializedName("causes")
    val causes: List<String>? = null,
    
    @SerializedName("treatment")
    val treatment: String? = null,
    
    @SerializedName("treatments")
    val treatments: List<String>? = null,
    
    @SerializedName("prevention")
    val prevention: List<String>? = null,
    
    @SerializedName("severity")
    val severity: String? = null,
    
    @SerializedName("severity_vi")
    val severityVi: String? = null
)

/**
 * History List Response
 */
data class HistoryListResponse(
    @SerializedName("predictions")
    val predictions: List<PredictionHistory>,
    
    @SerializedName("count")
    val count: Int
)

/**
 * Prediction History Item
 */
data class PredictionHistory(
    @SerializedName("id")
    val id: String,
    
    @SerializedName("user_id")
    val userId: String? = null,
    
    @SerializedName("predicted_class")
    val predictedClass: String,
    
    @SerializedName("confidence")
    val confidence: Float,
    
    @SerializedName("disease_info")
    val diseaseInfo: DiseaseInfo? = null,
    
    @SerializedName("image_url")
    val imageUrl: String? = null,
    
    @SerializedName("timestamp")
    val timestamp: Any? = null,
    
    @SerializedName("top3")
    val top3: List<Top3Prediction>? = null
)

data class DeleteResponse(
    @SerializedName("deleted")
    val deleted: Boolean,
    
    @SerializedName("id")
    val id: String? = null
)

data class CreateChatResponse(
    @SerializedName("conversation_id")
    val conversationId: String,
    
    @SerializedName("conversation")
    val conversation: Conversation? = null
)

// =============== Chat Models ===============

data class Conversation(
    @SerializedName("id")
    val id: String,
    
    @SerializedName("user_id")
    val userId: String? = null,
    
    @SerializedName("title")
    val title: String,
    
    @SerializedName("prediction_id")
    val predictionId: String? = null,
    
    @SerializedName("disease_context")
    val diseaseContext: DiseaseContext? = null,
    
    @SerializedName("created_at")
    val createdAt: String? = null,
    
    @SerializedName("updated_at")
    val updatedAt: String? = null,
    
    @SerializedName("message_count")
    val messageCount: Int = 0,
    
    @SerializedName("status")
    val status: String = "active"
)

data class DiseaseContext(
    @SerializedName("predicted_class")
    val predictedClass: String?,
    
    @SerializedName("confidence")
    val confidence: Float?,
    
    @SerializedName("disease_name_vi")
    val diseaseNameVi: String? = null,
    
    @SerializedName("disease_name_en")
    val diseaseNameEn: String? = null,
    
    @SerializedName("severity")
    val severity: String? = null,
    
    @SerializedName("severity_vi")
    val severityVi: String? = null,
    
    @SerializedName("description")
    val description: String? = null,
    
    @SerializedName("symptoms")
    val symptoms: List<String>? = null,
    
    @SerializedName("treatment")
    val treatment: String? = null,
    
    @SerializedName("prevention")
    val prevention: List<String>? = null,
    
    @SerializedName("image_url")
    val imageUrl: String? = null,  // URL of prediction image
    
    @SerializedName("prediction_date")
    val predictionDate: String? = null,
    
    @SerializedName("disease_info")
    val diseaseInfo: DiseaseInfo? = null
)

data class ConversationDetail(
    @SerializedName("id")
    val id: String,
    
    @SerializedName("user_id")
    val userId: String? = null,
    
    @SerializedName("title")
    val title: String,
    
    @SerializedName("prediction_id")
    val predictionId: String? = null,
    
    @SerializedName("disease_context")
    val diseaseContext: DiseaseContext? = null,
    
    @SerializedName("messages")
    val messages: List<ChatMessage>,
    
    @SerializedName("created_at")
    val createdAt: String? = null,
    
    @SerializedName("updated_at")
    val updatedAt: String? = null,
    
    @SerializedName("message_count")
    val messageCount: Int = 0
)

data class ChatMessage(
    @SerializedName("id")
    val id: String,
    
    @SerializedName("conversation_id")
    val conversationId: String? = null,
    
    @SerializedName("role")
    val role: String, // "user" or "assistant"
    
    @SerializedName("content")
    val content: String,
    
    @SerializedName("timestamp")
    val timestamp: String? = null
)

data class ConversationsResponse(
    @SerializedName("conversations")
    val conversations: List<Conversation>,
    
    @SerializedName("total")
    val total: Int,
    
    @SerializedName("limit")
    val limit: Int = 20,
    
    @SerializedName("offset")
    val offset: Int = 0
)

data class MessagesResponse(
    @SerializedName("messages")
    val messages: List<ChatMessage>,
    
    @SerializedName("total")
    val total: Int
)

data class CreateConversationRequest(
    @SerializedName("title")
    val title: String? = null,
    
    @SerializedName("prediction_id")
    val predictionId: String? = null,
    
    @SerializedName("initial_message")
    val initialMessage: String? = null
)

data class SendMessageRequest(
    @SerializedName("content")
    val content: String
)

// =============== User Models ===============

/**
 * User Profile
 */
data class UserProfile(
    @SerializedName("uid")
    val uid: String? = null,
    
    @SerializedName("user_id")
    val userId: String? = null,
    
    @SerializedName("email")
    val email: String? = null,
    
    @SerializedName("display_name")
    val displayName: String? = null,
    
    @SerializedName("photo_url")
    val photoUrl: String? = null,
    
    @SerializedName("phone_number")
    val phoneNumber: String? = null,
    
    @SerializedName("farm_location")
    val farmLocation: String? = null,
    
    @SerializedName("farm_size")
    val farmSize: Float? = null,
    
    @SerializedName("notification_enabled")
    val notificationEnabled: Boolean = true,
    
    @SerializedName("total_predictions")
    val totalPredictions: Int = 0,
    
    @SerializedName("settings")
    val settings: UserSettings? = null,
    
    @SerializedName("created_at")
    val createdAt: String? = null,
    
    @SerializedName("last_login")
    val lastLogin: String? = null
)

data class UserSettings(
    @SerializedName("notifications_enabled")
    val notificationsEnabled: Boolean = true,
    
    @SerializedName("language")
    val language: String = "vi"
)

data class UpdateProfileRequest(
    @SerializedName("display_name")
    val displayName: String? = null,
    
    @SerializedName("phone_number")
    val phoneNumber: String? = null,
    
    @SerializedName("farm_location")
    val farmLocation: String? = null,
    
    @SerializedName("farm_size")
    val farmSize: Float? = null,
    
    @SerializedName("notification_enabled")
    val notificationEnabled: Boolean? = null,
    
    @SerializedName("settings")
    val settings: UserSettings? = null
)

/**
 * Dashboard Statistics
 */
data class DashboardStats(
    @SerializedName("total_predictions")
    val totalPredictions: Int,
    
    @SerializedName("healthy_count")
    val healthyCount: Int,
    
    @SerializedName("diseased_count")
    val diseasedCount: Int,
    
    @SerializedName("recent_predictions")
    val recentPredictions: List<PredictionHistory>
)

// =============== Disease Models ===============

data class DiseasesListResponse(
    @SerializedName("diseases")
    val diseases: List<DiseaseListItem>,
    
    @SerializedName("total")
    val total: Int,
    
    @SerializedName("severity_level")
    val severityLevel: String? = null,
    
    @SerializedName("query")
    val query: String? = null,
    
    @SerializedName("results")
    val results: List<DiseaseListItem>? = null
)

data class DiseaseListItem(
    @SerializedName("class")
    val diseaseClass: String,
    
    @SerializedName("name")
    val name: String,
    
    @SerializedName("name_vi")
    val nameVi: String? = null,
    
    @SerializedName("description")
    val description: String? = null,
    
    @SerializedName("severity")
    val severity: String
)

// =============== Model Info ===============

/**
 * Model Info
 */
data class ModelInfo(
    @SerializedName("model_type")
    val modelType: String,
    
    @SerializedName("num_classes")
    val numClasses: Int,
    
    @SerializedName("classes")
    val classes: List<String>,
    
    @SerializedName("device")
    val device: String,
    
    @SerializedName("input_size")
    val inputSize: List<Int>
)

// =============== Health Check ===============

data class HealthResponse(
    @SerializedName("status")
    val status: String,
    
    @SerializedName("message")
    val message: String? = null,
    
    @SerializedName("timestamp")
    val timestamp: String? = null
)
