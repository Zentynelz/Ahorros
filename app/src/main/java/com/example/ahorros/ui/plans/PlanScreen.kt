package com.example.ahorros.ui.plans

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ahorros.data.model.Plan

@Composable
fun PlansScreen(
    viewModel: PlansViewModel,
    onPlanClick: (Plan) -> Unit,
    onCreatePlanClick: () -> Unit,
    onAddPaymentClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.observeAsState(PlansUiState(isLoading = true))

    Surface(modifier = modifier.fillMaxSize()) {
        when {
            uiState.isLoading -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator()
                }
            }

            uiState.error != null -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = uiState.error ?: "Error desconocido", style = MaterialTheme.typography.bodyLarge)
                    Button(onClick = { viewModel.loadPlans() }, modifier = Modifier.padding(top = 12.dp)) {
                        Text(text = "Reintentar")
                    }
                }
            }

            else -> {
                Column(modifier = Modifier.fillMaxSize()) {
                    PlansList(
                        plans = uiState.plans,
                        onPlanClick = onPlanClick,
                        modifier = Modifier.weight(1f)
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(onClick = onCreatePlanClick, modifier = Modifier.weight(1f).padding(end = 8.dp)) {
                            Text(text = "Crear Plan")
                        }
                        Button(onClick = onAddPaymentClick, modifier = Modifier.weight(1f).padding(start = 8.dp)) {
                            Text(text = "Registrar Pago")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PlansList(
    plans: List<Plan>,
    onPlanClick: (Plan) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(plans) { plan ->
            PlanItem(plan = plan) { onPlanClick(plan) }
        }
    }
}

@Composable
fun PlanItem(plan: Plan, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = plan.name, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold))
            plan.motive?.let {
                Text(text = it, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(top = 4.dp))
            }
            Text(
                text = "Meta: ${'$'}{plan.targetAmount}",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 8.dp)
            )
            Text(
                text = "Meses: ${'$'}{plan.months}",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PlanItemPreview() {
    val plan = Plan(
        id = "1",
        name = "Ahorro familiar",
        motive = "Vacaciones",
        targetAmount = 5000000,
        months = 8,
        createdAt = "2025-11-16T17:00:00.000Z"
    )
    PlanItem(plan = plan, onClick = {})
}