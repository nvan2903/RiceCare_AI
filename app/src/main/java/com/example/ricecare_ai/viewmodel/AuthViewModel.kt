package com.example.ricecare_ai.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

/**
 * ViewModel for authentication
 */
class AuthViewModel : ViewModel() {
    
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var googleSignInClient: GoogleSignInClient? = null
    
    private val _authState = MutableStateFlow(AuthState())
    val authState: StateFlow<AuthState> = _authState.asStateFlow()
    
    init {
        // Check if user is already signed in
        checkCurrentUser()
    }
    
    /**
     * Check current authenticated user
     */
    private fun checkCurrentUser() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            updateAuthState(currentUser)
        }
    }
    
    /**
     * Sign in with Google credential
     */
    fun signInWithGoogle(idToken: String) {
        viewModelScope.launch {
            _authState.update { it.copy(isLoading = true, error = null) }
            
            try {
                val credential = GoogleAuthProvider.getCredential(idToken, null)
                val result = auth.signInWithCredential(credential).await()
                result.user?.let { user ->
                    updateAuthState(user)
                } ?: run {
                    _authState.update { 
                        it.copy(
                            isLoading = false,
                            error = "Sign in failed"
                        ) 
                    }
                }
            } catch (e: Exception) {
                _authState.update { 
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Sign in failed"
                    ) 
                }
            }
        }
    }
    
    /**
     * Sign in with email and password
     */
    fun signInWithEmail(email: String, password: String) {
        viewModelScope.launch {
            _authState.update { it.copy(isLoading = true, error = null) }
            
            try {
                val result = auth.signInWithEmailAndPassword(email, password).await()
                result.user?.let { user ->
                    updateAuthState(user)
                } ?: run {
                    _authState.update { 
                        it.copy(
                            isLoading = false,
                            error = "Sign in failed"
                        ) 
                    }
                }
            } catch (e: Exception) {
                _authState.update { 
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Sign in failed"
                    ) 
                }
            }
        }
    }
    
    /**
     * Sign up with email and password
     */
    fun signUpWithEmail(email: String, password: String) {
        viewModelScope.launch {
            _authState.update { it.copy(isLoading = true, error = null) }
            
            try {
                val result = auth.createUserWithEmailAndPassword(email, password).await()
                result.user?.let { user ->
                    updateAuthState(user)
                } ?: run {
                    _authState.update { 
                        it.copy(
                            isLoading = false,
                            error = "Sign up failed"
                        ) 
                    }
                }
            } catch (e: Exception) {
                _authState.update { 
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Sign up failed"
                    ) 
                }
            }
        }
    }
    
    /**
     * Set Google Sign-In client for proper logout
     */
    fun setGoogleSignInClient(client: GoogleSignInClient) {
        googleSignInClient = client
    }
    
    /**
     * Sign out current user
     */
    fun signOut() {
        // Sign out from Firebase
        auth.signOut()
        // Sign out from Google to allow account picker next time
        googleSignInClient?.signOut()
        _authState.update { AuthState() }
    }
    
    /**
     * Get current ID token for API calls
     */
    suspend fun getIdToken(): String? {
        return try {
            auth.currentUser?.getIdToken(false)?.await()?.token
        } catch (e: Exception) {
            null
        }
    }
    
    /**
     * Refresh ID token
     */
    fun refreshToken(onComplete: (String?) -> Unit) {
        viewModelScope.launch {
            val token = getIdToken()
            _authState.update { it.copy(token = token) }
            onComplete(token)
        }
    }
    
    /**
     * Update auth state from Firebase user
     */
    private fun updateAuthState(user: FirebaseUser) {
        viewModelScope.launch {
            val token = try {
                user.getIdToken(false).await()?.token
            } catch (e: Exception) {
                null
            }
            
            _authState.update { 
                it.copy(
                    isLoggedIn = true,
                    isLoading = false,
                    userId = user.uid,
                    userEmail = user.email,
                    displayName = user.displayName,
                    photoUrl = user.photoUrl?.toString(),  // Get Google profile picture URL
                    token = token,
                    error = null
                ) 
            }
        }
    }
    
    /**
     * Dismiss error
     */
    fun dismissError() {
        _authState.update { it.copy(error = null) }
    }
}
