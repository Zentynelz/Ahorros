package com.example.ahorros.ui.addpayment

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.*
import com.example.ahorros.data.repository.MembersRepositoryImpl
import com.example.ahorros.data.repository.PaymentsRepositoryImpl
import com.example.ahorros.data.remote.PlanApiService

class AddPaymentActivity : ComponentActivity() {

    // Crear instancia del API
    private val apiService = PlanApiService.create()

    private val viewModel: AddPaymentViewModel by viewModels {
        AddPaymentViewModelFactory(
            planId = intent.getStringExtra("planId") ?: "",
            membersRepository = MembersRepositoryImpl(apiService),
            paymentsRepository = PaymentsRepositoryImpl(apiService)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    AddPaymentScreen(
                        viewModel = viewModel,
                        onPaymentRegistered = { finish() },
                        onNavigateBack = { finish() }
                    )
                }
            }
        }
    }
}
