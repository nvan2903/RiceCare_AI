package com.example.ricecare_ai.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ricecare_ai.data.api.RetrofitInstance
import com.example.ricecare_ai.data.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for Disease Information
 * Handles disease listing, detail, and search functionality
 */
class DiseaseViewModel : ViewModel() {
    
    private val apiService = RetrofitInstance.api
    
    // Diseases list state
    private val _diseasesState = MutableStateFlow<UiState<DiseasesListResponse>>(UiState.Idle)
    val diseasesState: StateFlow<UiState<DiseasesListResponse>> = _diseasesState.asStateFlow()
    
    // Disease detail state
    private val _diseaseDetailState = MutableStateFlow<UiState<DiseaseInfo>>(UiState.Idle)
    val diseaseDetailState: StateFlow<UiState<DiseaseInfo>> = _diseaseDetailState.asStateFlow()
    
    // Search results state
    private val _searchState = MutableStateFlow<UiState<DiseasesListResponse>>(UiState.Idle)
    val searchState: StateFlow<UiState<DiseasesListResponse>> = _searchState.asStateFlow()
    
    // Model info state
    private val _modelInfoState = MutableStateFlow<UiState<ModelInfo>>(UiState.Idle)
    val modelInfoState: StateFlow<UiState<ModelInfo>> = _modelInfoState.asStateFlow()
    
    /**
     * Load all diseases
     */
    fun loadAllDiseases() {
        viewModelScope.launch {
            _diseasesState.value = UiState.Loading
            
            try {
                val response = apiService.getAllDiseases()
                
                if (response.isSuccessful && response.body()?.success == true) {
                    response.body()?.data?.let { data ->
                        _diseasesState.value = UiState.Success(data)
                    } ?: run {
                        _diseasesState.value = UiState.Error("No data received")
                    }
                } else {
                    _diseasesState.value = UiState.Error(
                        response.body()?.error ?: "Không thể tải danh sách bệnh"
                    )
                }
            } catch (e: Exception) {
                _diseasesState.value = UiState.Error("Lỗi kết nối: ${e.message}")
            }
        }
    }
    
    /**
     * Load disease detail by class name
     */
    fun loadDiseaseDetail(diseaseClass: String) {
        viewModelScope.launch {
            _diseaseDetailState.value = UiState.Loading
            
            try {
                val response = apiService.getDiseaseDetail(diseaseClass)
                
                if (response.isSuccessful && response.body()?.success == true) {
                    response.body()?.data?.let { detail ->
                        _diseaseDetailState.value = UiState.Success(detail)
                    } ?: run {
                        _diseaseDetailState.value = UiState.Error("Không có dữ liệu")
                    }
                } else {
                    _diseaseDetailState.value = UiState.Error(
                        response.body()?.error ?: "Không thể tải thông tin bệnh"
                    )
                }
            } catch (e: Exception) {
                _diseaseDetailState.value = UiState.Error("Lỗi: ${e.message}")
            }
        }
    }
    
    /**
     * Get diseases by severity level
     */
    fun loadDiseasesBySeverity(severityLevel: String) {
        viewModelScope.launch {
            _diseasesState.value = UiState.Loading
            
            try {
                val response = apiService.getDiseasesBySeverity(severityLevel)
                
                if (response.isSuccessful && response.body()?.success == true) {
                    response.body()?.data?.let { data ->
                        _diseasesState.value = UiState.Success(data)
                    } ?: run {
                        _diseasesState.value = UiState.Error("Không có dữ liệu")
                    }
                } else {
                    _diseasesState.value = UiState.Error(
                        response.body()?.error ?: "Không thể tải danh sách"
                    )
                }
            } catch (e: Exception) {
                _diseasesState.value = UiState.Error("Lỗi: ${e.message}")
            }
        }
    }
    
    /**
     * Search diseases by query
     */
    fun searchDiseases(query: String) {
        if (query.isBlank()) {
            _searchState.value = UiState.Idle
            return
        }
        
        viewModelScope.launch {
            _searchState.value = UiState.Loading
            
            try {
                val response = apiService.searchDiseases(query)
                
                if (response.isSuccessful && response.body()?.success == true) {
                    response.body()?.data?.let { data ->
                        _searchState.value = UiState.Success(data)
                    } ?: run {
                        _searchState.value = UiState.Error("Không có kết quả")
                    }
                } else {
                    _searchState.value = UiState.Error(
                        response.body()?.error ?: "Tìm kiếm thất bại"
                    )
                }
            } catch (e: Exception) {
                _searchState.value = UiState.Error("Lỗi: ${e.message}")
            }
        }
    }
    
    /**
     * Load model info
     */
    fun loadModelInfo() {
        viewModelScope.launch {
            _modelInfoState.value = UiState.Loading
            
            try {
                val response = apiService.getModelInfo()
                
                if (response.isSuccessful && response.body()?.success == true) {
                    response.body()?.data?.let { info ->
                        _modelInfoState.value = UiState.Success(info)
                    } ?: run {
                        _modelInfoState.value = UiState.Error("Không có dữ liệu")
                    }
                } else {
                    _modelInfoState.value = UiState.Error("Không thể tải thông tin model")
                }
            } catch (e: Exception) {
                _modelInfoState.value = UiState.Error("Lỗi: ${e.message}")
            }
        }
    }
    
    /**
     * Clear search state
     */
    fun clearSearch() {
        _searchState.value = UiState.Idle
    }
    
    /**
     * Clear disease detail state
     */
    fun clearDiseaseDetail() {
        _diseaseDetailState.value = UiState.Idle
    }
    
    /**
     * Reset all states
     */
    fun reset() {
        _diseasesState.value = UiState.Idle
        _diseaseDetailState.value = UiState.Idle
        _searchState.value = UiState.Idle
        _modelInfoState.value = UiState.Idle
    }
}
