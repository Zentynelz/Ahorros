package com.example.ahorros.ui.addpayment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ahorros.data.repository.MembersRepositoryImpl
import com.example.ahorros.data.repository.PaymentsRepositoryImpl

class AddPaymentViewModelFactory(
    private val planId: String,
    private val membersRepository: MembersRepositoryImpl,
    private val paymentsRepository: PaymentsRepositoryImpl
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddPaymentViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AddPaymentViewModel(planId, membersRepository, paymentsRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
