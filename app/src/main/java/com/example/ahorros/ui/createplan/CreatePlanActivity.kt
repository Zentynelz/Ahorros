package com.example.ahorros.ui.createplan

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.*
import com.example.ahorros.data.remote.RetrofitClient
import com.example.ahorros.data.remote.PlanApiService
import com.example.ahorros.data.repository.PlansRepositoryImpl // Asumir la implementación

class CreatePlanActivity : ComponentActivity() {

    private val viewModel: CreatePlanViewModel by viewModels {

        CreatePlanViewModelFactory(PlansRepositoryImpl(RetrofitClient.api))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    CreatePlanScreen(
                        viewModel = viewModel,
                        onPlanCreated = {
                            // Al crear el plan, se finaliza la actividad y se regresa a la anterior
                            finish()
                        },
                        onNavigateBack = {
                            finish()
                        }
                    )
                }
            }
        }
    }
}

// Placeholder para la Factory del ViewModel
class CreatePlanViewModelFactory(private val repository: PlansRepositoryImpl) : androidx.lifecycle.ViewModelProvider.Factory {
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CreatePlanViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CreatePlanViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}