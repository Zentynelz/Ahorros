package com.example.ahorros.data.repository

import com.example.ahorros.data.model.Plan
import com.example.ahorros.data.remote.PlanApiService
import com.example.ahorros.util.Resource
import retrofit2.HttpException
import java.io.IOException

class PlansRepositoryImpl(
    private val apiService: PlanApiService
) : PlansRepository {

    override suspend fun getPlans(): Resource<List<Plan>> {
        return try {
            val response = apiService.getPlans()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    Resource.Success(body)
                } else {
                    Resource.Error("Respuesta vacía del servidor")
                }
            } else {
                val errorMessage = "Error ${'$'}{response.code()}: ${'$'}{response.message()}"
                Resource.Error(errorMessage)
            }
        } catch (e: HttpException) {
            Resource.Error("Error de servidor: ${'$'}{e.message()}")
        } catch (e: IOException) {
            Resource.Error("Error de conexión. Verifica tu red")
        } catch (e: Exception) {
            Resource.Error("Error inesperado: ${'$'}{e.localizedMessage}")
        }
    }
}