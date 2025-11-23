package com.example.ahorros.data.model


import com.google.gson.annotations.SerializedName

/**
 * Representa un plan de ahorro proporcionado por el backend.
 */
data class Plan(
    @SerializedName("_id")
    val id: String,
    val name: String,
    val motive: String?,
    val targetAmount: Long,
    val months: Int,
    val createdAt: String
)
