package com.example.ahorros.ui.addpayment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ahorros.data.model.Member
import com.example.ahorros.data.model.Payment
import com.example.ahorros.util.Resource
import com.example.ahorros.data.repository.MembersRepository
import com.example.ahorros.data.repository.PaymentsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AddPaymentState(
    val members: List<Member> = emptyList(),
    val selectedMemberId: String? = null,
    val amount: String = "",
    val isLoading: Boolean = false,
    val isPaymentRegistered: Boolean = false,
    val amountError: String? = null,
    val memberError: String? = null,
    val generalError: String? = null
)

sealed class AddPaymentEvent {
    object PaymentRegistered : AddPaymentEvent()
    data class Error(val message: String) : AddPaymentEvent()
}

class AddPaymentViewModel(
    private val planId: String,
    private val membersRepository: MembersRepository,
    private val paymentsRepository: PaymentsRepository
) : ViewModel() {

    private val _state = MutableStateFlow(AddPaymentState())
    val state: StateFlow<AddPaymentState> = _state

    private val _events = MutableStateFlow<AddPaymentEvent?>(null)
    val events: StateFlow<AddPaymentEvent?> = _events

    init {
        loadMembers()
    }

    private fun loadMembers() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            when (val result = membersRepository.getMembersByPlan(planId)) {
                is Resource.Success -> {
                    _state.update {
                        it.copy(
                            members = result.data ?: emptyList(),
                            isLoading = false
                        )
                    }
                }
                is Resource.Error -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            generalError = result.message
                        )
                    }
                }
                is Resource.Loading -> {
                    _state.update { it.copy(isLoading = true) }
                }
            }
        }
    }

    fun onMemberSelected(memberId: String) {
        _state.update {
            it.copy(
                selectedMemberId = memberId,
                memberError = null,
                generalError = null
            )
        }
    }

    fun onAmountChange(amount: String) {
        _state.update {
            it.copy(
                amount = amount,
                amountError = null,
                generalError = null
            )
        }
    }

    fun registerPayment() {
        if (!validateInputs()) return

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            val amountLong = state.value.amount.toLong()
            val memberId = state.value.selectedMemberId!!

            val newPayment = Payment(
                id = "",
                memberId = memberId,
                planId = planId,
                amount = amountLong,
                date = "" // el backend lo asigna
            )

            when (val result = paymentsRepository.createPayment(newPayment)) {
                is Resource.Success -> {
                    _state.update { it.copy(isLoading = false, isPaymentRegistered = true) }
                    _events.value = AddPaymentEvent.PaymentRegistered
                }
                is Resource.Error -> {
                    _state.update { it.copy(isLoading = false, generalError = result.message) }
                    _events.value = AddPaymentEvent.Error(result.message ?: "Error desconocido")
                }
                is Resource.Loading -> {
                    _state.update { it.copy(isLoading = true) }
                }
            }
        }
    }

    private fun validateInputs(): Boolean {
        var valid = true
        val current = _state.value

        if (current.selectedMemberId == null) {
            _state.update { it.copy(memberError = "Debe seleccionar un miembro.") }
            valid = false
        }

        val amount = current.amount.toLongOrNull()
        if (amount == null || amount <= 0) {
            _state.update {
                it.copy(amountError = "El monto debe ser un número positivo.")
            }
            valid = false
        }

        return valid
    }

    fun consumeEvent() {
        _events.value = null
    }
}
