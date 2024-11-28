package com.example.miniprojetokotlin2

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ViaCepApi {
    @GET("{cep}/json/")
    suspend fun getAddress(@Path("cep") cep: String): Response<ViaCepResponse>
}

// Data class para o retorno da API
data class ViaCepResponse(
    val cep: String,
    val logradouro: String,
    val bairro: String,
    val localidade: String,
    val uf: String
)