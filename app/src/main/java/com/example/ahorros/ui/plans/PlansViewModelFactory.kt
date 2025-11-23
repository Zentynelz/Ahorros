package com.example.ahorros.ui.plans

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ahorros.data.remote.RetrofitClient
import com.example.ahorros.data.repository.PlansRepositoryImpl

class PlansViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PlansViewModel::class.java)) {
            val api = RetrofitClient.api
            val repository = PlansRepositoryImpl(api)
            @Suppress("UNCHECKED_CAST")
            return PlansViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}