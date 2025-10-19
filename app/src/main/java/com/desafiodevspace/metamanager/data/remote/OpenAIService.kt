package com.desafiodevspace.metamanager.data.remote

import com.desafiodevspace.metamanager.data.remote.dto.OpenAIRequest
import com.desafiodevspace.metamanager.data.remote.dto.OpenAIResponse
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface OpenAIService {

    @POST("v1/completions")
    suspend fun getCompletion(
        @Header("Authorization") apiKey: String,
        @Body request: OpenAIRequest
    ): OpenAIResponse
}
