package com.example.ahorros.ui.addpayment

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.ahorros.data.model.Member

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPaymentScreen(
    viewModel: AddPaymentViewModel,
    onPaymentRegistered: () -> Unit,
    onNavigateBack: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    val event by viewModel.events.collectAsState()

    // Manejo de eventos
    event?.let {
        when (it) {
            is AddPaymentEvent.PaymentRegistered -> {
                onPaymentRegistered()
                viewModel.consumeEvent()
            }

            is AddPaymentEvent.Error -> {
                viewModel.consumeEvent()
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Registrar Nuevo Pago") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Atrás")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Dropdown de miembros
            MemberDropdown(
                members = state.members,
                selectedMemberId = state.selectedMemberId,
                onMemberSelected = viewModel::onMemberSelected,
                isError = state.memberError != null,
            )
            if (state.memberError != null) {
                Text(
                    state.memberError!!,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.Start)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Campo de monto
            OutlinedTextField(
                value = state.amount,
                onValueChange = viewModel::onAmountChange,
                label = { Text("Monto del Pago") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = state.amountError != null,
                modifier = Modifier.fillMaxWidth()
            )

            if (state.amountError != null) {
                Text(
                    state.amountError!!,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.Start)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (state.generalError != null) {
                Text(
                    state.generalError!!,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            // Botón de registrar
            Button(
                onClick = viewModel::registerPayment,
                enabled = !state.isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    Text("Registrar Pago")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemberDropdown(
    members: List<Member>,
    selectedMemberId: String?,
    onMemberSelected: (String) -> Unit,
    isError: Boolean
) {
    var expanded by remember { mutableStateOf(false) }

    val selectedMember = members.find { it.id == selectedMemberId }
    val label = selectedMember?.name ?: "Seleccione un Miembro"

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = label,
            onValueChange = {},
            readOnly = true,
            label = { Text("Miembro") },
            isError = isError,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
            modifier = Modifier.menuAnchor().fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            members.forEach { member ->
                DropdownMenuItem(
                    text = { Text(member.name) },
                    onClick = {
                        onMemberSelected(member.id)
                        expanded = false
                    }
                )
            }
        }
    }
}
