package com.example.ahorros.data.remote

import com.example.ahorros.data.model.Plan
import retrofit2.Response
import retrofit2.http.GET

interface PlanApiService {
    @GET("plans")
    suspend fun getPlans(): Response<List<Plan>>
}