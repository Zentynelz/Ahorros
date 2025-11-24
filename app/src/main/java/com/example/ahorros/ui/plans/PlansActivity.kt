package com.example.ahorros.ui.plans

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import com.example.ahorros.ui.plandetail.PlanDetailActivity
import com.example.ahorros.ui.createplan.CreatePlanActivity
import com.example.ahorros.ui.addpayment.AddPaymentActivity

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
                            val intent = Intent(this, PlanDetailActivity::class.java)
                            intent.putExtra(PlanDetailActivity.EXTRA_PLAN_ID, plan.id)
                            startActivity(intent)
                        },
                        onCreatePlanClick = {
                            val intent = Intent(this, CreatePlanActivity::class.java)
                            startActivity(intent)
                        },
                        onAddPaymentClick = {
                            val intent = Intent(this, AddPaymentActivity::class.java)
                            startActivity(intent)
                        }
                    )
                }
            }
        }
    }
}