package com.example.ahorros.ui.createplan

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CreatePlanScreen(
    viewModel: CreatePlanViewModel,
    onPlanCreated: () -> Unit,
    onNavigateBack: () -> Unit
) {
    val state = viewModel.state.collectAsState().value

    Column(modifier = Modifier.padding(16.dp)) {

        // Nombre
        OutlinedTextField(
            value = state.name,
            onValueChange = viewModel::onNameChange,
            label = { Text("Nombre del plan") },
            modifier = Modifier.fillMaxWidth()
        )

        if (state.nameError != null) {
            Text(
                text = state.nameError!!,
                color = MaterialTheme.colorScheme.error
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Motivo
        OutlinedTextField(
            value = state.motive,
            onValueChange = viewModel::onMotiveChange,
            label = { Text("Motivo (opcional)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Monto objetivo
        OutlinedTextField(
            value = state.targetAmount,
            onValueChange = viewModel::onTargetAmountChange,
            label = { Text("Monto objetivo") },
            modifier = Modifier.fillMaxWidth()
        )

        if (state.targetAmountError != null) {
            Text(
                text = state.targetAmountError!!,
                color = MaterialTheme.colorScheme.error
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Meses
        OutlinedTextField(
            value = state.months,
            onValueChange = viewModel::onMonthsChange,
            label = { Text("Meses") },
            modifier = Modifier.fillMaxWidth()
        )

        if (state.monthsError != null) {
            Text(
                text = state.monthsError!!,
                color = MaterialTheme.colorScheme.error
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = { viewModel.createPlan() },
            enabled = !state.isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Crear Plan")
        }

        if (state.isPlanCreated) {
            onPlanCreated()
        }

        if (state.generalError != null) {
            Text(
                text = state.generalError!!,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}
