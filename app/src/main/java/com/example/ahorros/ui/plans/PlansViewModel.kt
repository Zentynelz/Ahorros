package com.example.ahorros.ui.plans

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ahorros.data.model.Plan
import com.example.ahorros.data.repository.PlansRepository
import com.example.ahorros.util.Resource
import kotlinx.coroutines.launch

data class PlansUiState(
    val isLoading: Boolean = false,
    val plans: List<Plan> = emptyList(),
    val error: String? = null
)

class PlansViewModel(
    private val repository: PlansRepository
) : ViewModel() {

    private val _uiState = MutableLiveData(PlansUiState())
    val uiState: LiveData<PlansUiState> = _uiState

    init {
        loadPlans()
    }

    fun loadPlans() {
        _uiState.value = _uiState.value?.copy(isLoading = true, error = null)
        viewModelScope.launch {
            when (val result = repository.getPlans()) {
                is Resource.Success -> {
                    _uiState.value = PlansUiState(
                        isLoading = false,
                        plans = result.data,
                        error = null
                    )
                }

                is Resource.Error -> {
                    _uiState.value = PlansUiState(
                        isLoading = false,
                        plans = emptyList(),
                        error = result.message
                    )
                }

                is Resource.Loading -> {
                    _uiState.value = PlansUiState(isLoading = true)
                }
            }
        }
    }
}