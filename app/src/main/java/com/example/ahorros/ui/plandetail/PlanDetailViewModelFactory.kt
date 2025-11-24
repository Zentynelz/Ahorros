package com.example.ahorros.ui.plandetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ahorros.data.remote.RetrofitClient
import com.example.ahorros.data.repository.MembersRepositoryImpl
import com.example.ahorros.data.repository.PaymentsRepositoryImpl
import com.example.ahorros.data.repository.PlansRepositoryImpl

/**
 * Factory que permite crear el ViewModel recibiendo planId.
 */
class PlanDetailViewModelFactory(
    private val planId: String
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PlanDetailViewModel::class.java)) {

            val api = RetrofitClient.api

            val plansRepository = PlansRepositoryImpl(api)
            val membersRepository = MembersRepositoryImpl(api)
            val paymentsRepository = PaymentsRepositoryImpl(api)

            @Suppress("UNCHECKED_CAST")
            return PlanDetailViewModel(
                planId = planId,
                plansRepository = plansRepository,
                membersRepository = membersRepository,
                paymentsRepository = paymentsRepository
            ) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
