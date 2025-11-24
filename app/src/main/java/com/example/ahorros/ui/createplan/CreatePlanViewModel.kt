package com.example.ahorros.ui.createplan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ahorros.data.model.CreatePlanRequest
import com.example.ahorros.util.Resource
import com.example.ahorros.data.model.Plan
import com.example.ahorros.data.repository.PlansRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// Estado de la UI para la creación de un plan
data class CreatePlanState(
    val name: String = "",
    val motive: String = "",
    val targetAmount: String = "",
    val months: String = "",
    val isLoading: Boolean = false,
    val isPlanCreated: Boolean = false,
    val nameError: String? = null,
    val targetAmountError: String? = null,
    val monthsError: String? = null,
    val generalError: String? = null
)

// Eventos que pueden ocurrir en la UI
sealed class CreatePlanEvent {
    object PlanCreated : CreatePlanEvent()
    data class Error(val message: String) : CreatePlanEvent()
}

class CreatePlanViewModel(
    private val plansRepository: PlansRepository
) : ViewModel() {

    private val _state = MutableStateFlow(CreatePlanState())
    val state: StateFlow<CreatePlanState> = _state.asStateFlow()

    private val _events = MutableStateFlow<CreatePlanEvent?>(null)
    val events: StateFlow<CreatePlanEvent?> = _events.asStateFlow()

    fun onNameChange(name: String) {
        _state.update { it.copy(name = name, nameError = null, generalError = null) }
    }

    fun onMotiveChange(motive: String) {
        _state.update { it.copy(motive = motive, generalError = null) }
    }

    fun onTargetAmountChange(amount: String) {
        _state.update { it.copy(targetAmount = amount, targetAmountError = null, generalError = null) }
    }

    fun onMonthsChange(months: String) {
        _state.update { it.copy(months = months, monthsError = null, generalError = null) }
    }

    fun createPlan() {
        if (!validateInputs()) return

        _state.update { it.copy(isLoading = true, generalError = null) }

        viewModelScope.launch {
            try {
                val targetAmountLong = _state.value.targetAmount.toLong()
                val monthsInt = _state.value.months.toInt()


                val request = CreatePlanRequest(
                    name = _state.value.name,
                    motive = _state.value.motive.ifBlank { null },
                    targetAmount = targetAmountLong,
                    months = monthsInt
                )
                // Asumimos que PlansRepository tiene un método para crear un plan
                val planResource = plansRepository.createPlan(request)
                when (planResource) {
                    is Resource.Success -> {
                        _state.update { it.copy(isLoading = false, isPlanCreated = true) }
                        _events.value = CreatePlanEvent.PlanCreated
                    }
                    is Resource.Error -> {
                        _state.update { it.copy(isLoading = false, generalError = "Error al crear el plan: ${planResource.message}") }
                        _events.value = CreatePlanEvent.Error("Error al crear el plan: ${planResource.message}")
                    }
                    is Resource.Loading -> _state.update { it.copy(isLoading = true) }
                }



            } catch (e: NumberFormatException) {
                _state.update { it.copy(isLoading = false, generalError = "Error de formato en monto o meses.") }
                _events.value = CreatePlanEvent.Error("Error de formato en monto o meses.")
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, generalError = "Error al crear el plan: ${e.message}") }
                _events.value = CreatePlanEvent.Error("Error al crear el plan: ${e.message}")
            }
        }
    }

    private fun validateInputs(): Boolean {
        var isValid = true
        val state = _state.value

        if (state.name.isBlank()) {
            _state.update { it.copy(nameError = "El nombre es obligatorio.") }
            isValid = false
        }

        val targetAmount = state.targetAmount.toLongOrNull()
        if (targetAmount == null || targetAmount <= 0) {
            _state.update { it.copy(targetAmountError = "El monto objetivo debe ser un número positivo.") }
            isValid = false
        }

        val months = state.months.toIntOrNull()
        if (months == null || months <= 0) {
            _state.update { it.copy(monthsError = "Los meses deben ser un número positivo.") }
            isValid = false
        }

        return isValid
    }

    fun consumeEvent() {
        _events.value = null
    }
}