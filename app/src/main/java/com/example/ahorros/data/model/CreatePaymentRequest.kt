package com.example.ahorros.data.model

/**
 * Request para crear un nuevo pago.
 * No incluye el ID porque MongoDB lo genera automáticamente.
 */
data class CreatePaymentRequest(
    val memberId: String,
    val planId: String,
    val amount: Long
)
