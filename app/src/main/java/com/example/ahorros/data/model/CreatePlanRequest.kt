package com.example.ahorros.data.model

data class CreatePlanRequest(
    val name: String,
    val motive: String?,
    val targetAmount: Long,
    val months: Int
)
