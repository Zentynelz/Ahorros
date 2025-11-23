package com.example.ahorros.ui.plans

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface

class PlansActivity : ComponentActivity() {

    private val viewModel: PlansViewModel by viewModels { PlansViewModelFactory() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface {
                    PlansScreen(
                        viewModel = viewModel,
                        onPlanClick = { plan ->
                            // TODO: Navegar al detalle del plan seleccionado
                        }
                    )
                }
            }
        }
    }
}