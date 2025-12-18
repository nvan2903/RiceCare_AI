package com.example.ricecare_ai.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ricecare_ai.data.api.RetrofitInstance
import com.example.ricecare_ai.data.model.*
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import org.json.JSONObject
import java.io.BufferedReader

/**
 * ViewModel for Chat functionality
 * Handles conversations and messages with AI
 */
class ChatViewModel : ViewModel() {
    
    private val apiService = RetrofitInstance.api
    private val auth = FirebaseAuth.getInstance()
    
    // Conversations list state
    private val _conversationsState = MutableStateFlow<UiState<ConversationsResponse>>(UiState.Idle)
    val conversationsState: StateFlow<UiState<ConversationsResponse>> = _conversationsState.asStateFlow()
    
    // Current conversation detail state
    private val _conversationDetailState = MutableStateFlow<UiState<ConversationDetail>>(UiState.Idle)
    val conversationDetailState: StateFlow<UiState<ConversationDetail>> = _conversationDetailState.asStateFlow()
    
    // Messages list state
    private val _messagesState = MutableStateFlow<UiState<List<ChatMessage>>>(UiState.Idle)
    val messagesState: StateFlow<UiState<List<ChatMessage>>> = _messagesState.asStateFlow()
    
    // Send message state (for SSE streaming)
    private val _sendMessageState = MutableStateFlow<UiState<String>>(UiState.Idle)
    val sendMessageState: StateFlow<UiState<String>> = _sendMessageState.asStateFlow()
    
    // Streaming response accumulator
    private val _streamingResponse = MutableStateFlow("")
    val streamingResponse: StateFlow<String> = _streamingResponse.asStateFlow()
    
    // Current conversation ID
    private var currentConversationId: String? = null
    
    /**
     * Get auth token
     */
    private suspend fun getAuthToken(): String? {
        return try {
            val user = auth.currentUser
            user?.getIdToken(false)?.result?.token?.let { "Bearer $it" }
        } catch (e: Exception) {
            null
        }
    }
    
    /**
     * Load all conversations for the user
     */
    fun loadConversations(limit: Int = 20, offset: Int = 0) {
        viewModelScope.launch {
            _conversationsState.value = UiState.Loading
            
            try {
                val token = getAuthToken()
                if (token == null) {
                    _conversationsState.value = UiState.Error("Vui lòng đăng nhập để sử dụng chat")
                    return@launch
                }
                
                val response = apiService.getConversations(limit, offset, token)
                
                if (response.isSuccessful && response.body()?.success == true) {
                    response.body()?.data?.let {
                        _conversationsState.value = UiState.Success(it)
                    } ?: run {
                        _conversationsState.value = UiState.Error("No data received")
                    }
                } else {
                    _conversationsState.value = UiState.Error(
                        response.body()?.error ?: "Không thể tải danh sách hội thoại"
                    )
                }
            } catch (e: Exception) {
                _conversationsState.value = UiState.Error("Lỗi kết nối: ${e.message}")
            }
        }
    }
    
    /**
     * Create a new conversation
     */
    fun createConversation(
        title: String? = null,
        predictionId: String? = null,
        initialMessage: String? = null,
        onSuccess: (Conversation) -> Unit = {}
    ) {
        viewModelScope.launch {
            try {
                val token = getAuthToken()
                if (token == null) {
                    _conversationsState.value = UiState.Error("Vui lòng đăng nhập")
                    return@launch
                }
                
                val request = CreateConversationRequest(
                    title = title,
                    predictionId = predictionId,
                    initialMessage = initialMessage
                )
                
                val response = apiService.createConversation(request, token)
                
                if (response.isSuccessful && response.body()?.success == true) {
                    response.body()?.data?.let { conversation ->
                        currentConversationId = conversation.id
                        onSuccess(conversation)
                        // Reload conversations list
                        loadConversations()
                    }
                }
            } catch (e: Exception) {
                _conversationsState.value = UiState.Error("Không thể tạo hội thoại: ${e.message}")
            }
        }
    }
    
    /**
     * Create conversation from a prediction
     */
    fun createChatFromPrediction(
        predictionId: String,
        onSuccess: (String) -> Unit = {}
    ) {
        viewModelScope.launch {
            try {
                val token = getAuthToken()
                if (token == null) {
                    _conversationsState.value = UiState.Error("Vui lòng đăng nhập")
                    return@launch
                }
                
                val response = apiService.createChatFromPrediction(predictionId, token)
                
                if (response.isSuccessful && response.body()?.success == true) {
                    response.body()?.data?.let { createChatResponse ->
                        currentConversationId = createChatResponse.conversationId
                        onSuccess(createChatResponse.conversationId)
                        // Load the new conversation
                        loadConversationDetail(createChatResponse.conversationId)
                    }
                } else if (response.code() == 404) {
                    _conversationsState.value = UiState.Error("Dự đoán không tồn tại hoặc đã bị xóa. Vui lòng thực hiện dự đoán mới.")
                } else {
                    val errorMsg = response.body()?.message ?: "Không thể tạo chat"
                    _conversationsState.value = UiState.Error(errorMsg)
                }
            } catch (e: Exception) {
                _conversationsState.value = UiState.Error("Không thể tạo chat: ${e.message}")
            }
        }
    }
    
    /**
     * Load conversation detail with messages
     */
    fun loadConversationDetail(conversationId: String) {
        viewModelScope.launch {
            _conversationDetailState.value = UiState.Loading
            currentConversationId = conversationId
            
            try {
                val token = getAuthToken()
                if (token == null) {
                    _conversationDetailState.value = UiState.Error("Vui lòng đăng nhập")
                    return@launch
                }
                
                val response = apiService.getConversation(conversationId, token)
                
                if (response.isSuccessful && response.body()?.success == true) {
                    response.body()?.data?.let { detail ->
                        _conversationDetailState.value = UiState.Success(detail)
                        _messagesState.value = UiState.Success(detail.messages)
                    } ?: run {
                        _conversationDetailState.value = UiState.Error("Không có dữ liệu")
                    }
                } else {
                    _conversationDetailState.value = UiState.Error(
                        response.body()?.error ?: "Không thể tải hội thoại"
                    )
                }
            } catch (e: Exception) {
                _conversationDetailState.value = UiState.Error("Lỗi: ${e.message}")
            }
        }
    }
    
    /**
     * Load messages for a conversation
     */
    fun loadMessages(conversationId: String, limit: Int = 50) {
        viewModelScope.launch {
            _messagesState.value = UiState.Loading
            
            try {
                val token = getAuthToken()
                if (token == null) {
                    _messagesState.value = UiState.Error("Vui lòng đăng nhập")
                    return@launch
                }
                
                val response = apiService.getMessages(conversationId, limit, token)
                
                if (response.isSuccessful && response.body()?.success == true) {
                    response.body()?.data?.let { messagesResponse ->
                        _messagesState.value = UiState.Success(messagesResponse.messages)
                    }
                } else {
                    _messagesState.value = UiState.Error("Không thể tải tin nhắn")
                }
            } catch (e: Exception) {
                _messagesState.value = UiState.Error("Lỗi: ${e.message}")
            }
        }
    }
    
    /**
     * Send a message and receive streaming response
     */
    fun sendMessage(conversationId: String, content: String) {
        viewModelScope.launch {
            _sendMessageState.value = UiState.Loading
            _streamingResponse.value = ""
            
            try {
                val token = getAuthToken()
                if (token == null) {
                    _sendMessageState.value = UiState.Error("Vui lòng đăng nhập")
                    return@launch
                }
                
                // Add user message to local state first
                val currentMessages = (_messagesState.value as? UiState.Success)?.data?.toMutableList() ?: mutableListOf()
                val userMessage = ChatMessage(
                    id = "temp_${System.currentTimeMillis()}",
                    conversationId = conversationId,
                    role = "user",
                    content = content,
                    timestamp = java.time.Instant.now().toString()
                )
                currentMessages.add(userMessage)
                _messagesState.value = UiState.Success(currentMessages)
                
                val request = SendMessageRequest(content = content)
                val response = apiService.sendMessage(conversationId, request, token)
                
                if (response.isSuccessful) {
                    // Process SSE stream
                    processSSEResponse(response.body(), currentMessages, conversationId)
                } else {
                    _sendMessageState.value = UiState.Error("Không thể gửi tin nhắn")
                }
            } catch (e: Exception) {
                _sendMessageState.value = UiState.Error("Lỗi: ${e.message}")
            }
        }
    }
    
    /**
     * Process Server-Sent Events response
     */
    private suspend fun processSSEResponse(
        responseBody: ResponseBody?,
        currentMessages: MutableList<ChatMessage>,
        conversationId: String
    ) {
        if (responseBody == null) {
            _sendMessageState.value = UiState.Error("Empty response")
            return
        }
        
        try {
            val reader = BufferedReader(responseBody.charStream())
            var line: String?
            var fullResponse = ""
            var assistantMessageId = ""
            
            while (reader.readLine().also { line = it } != null) {
                if (line?.startsWith("data:") == true) {
                    val jsonStr = line?.substring(5)?.trim() ?: continue
                    
                    try {
                        val json = JSONObject(jsonStr)
                        val event = json.optString("event", "")
                        
                        when (event) {
                            "start" -> {
                                assistantMessageId = json.optString("message_id", "")
                            }
                            "chunk" -> {
                                val accumulated = json.optString("accumulated", "")
                                fullResponse = accumulated
                                _streamingResponse.value = accumulated
                            }
                            "end" -> {
                                fullResponse = json.optString("full_response", fullResponse)
                                assistantMessageId = json.optString("message_id", assistantMessageId)
                                
                                // Add assistant message to local state
                                val assistantMessage = ChatMessage(
                                    id = assistantMessageId,
                                    conversationId = conversationId,
                                    role = "assistant",
                                    content = fullResponse,
                                    timestamp = java.time.Instant.now().toString()
                                )
                                currentMessages.add(assistantMessage)
                                _messagesState.value = UiState.Success(currentMessages)
                                _sendMessageState.value = UiState.Success(fullResponse)
                            }
                        }
                    } catch (e: Exception) {
                        // Skip malformed JSON
                    }
                }
            }
            
            reader.close()
        } catch (e: Exception) {
            _sendMessageState.value = UiState.Error("Lỗi xử lý phản hồi: ${e.message}")
        }
    }
    
    /**
     * Delete a conversation
     */
    fun deleteConversation(conversationId: String, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            try {
                val token = getAuthToken()
                if (token == null) return@launch
                
                val response = apiService.deleteConversation(conversationId, token)
                
                if (response.isSuccessful && response.body()?.success == true) {
                    onSuccess()
                    // Reload conversations
                    loadConversations()
                }
            } catch (e: Exception) {
                _conversationsState.value = UiState.Error("Không thể xóa: ${e.message}")
            }
        }
    }
    
    /**
     * Clear streaming response
     */
    fun clearStreamingResponse() {
        _streamingResponse.value = ""
        _sendMessageState.value = UiState.Idle
    }
    
    /**
     * Reset all states
     */
    fun reset() {
        _conversationsState.value = UiState.Idle
        _conversationDetailState.value = UiState.Idle
        _messagesState.value = UiState.Idle
        _sendMessageState.value = UiState.Idle
        _streamingResponse.value = ""
        currentConversationId = null
    }
}
