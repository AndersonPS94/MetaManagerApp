package com.desafiodevspace.metamanager.data.remote

import com.desafiodevspace.metamanager.data.remote.dto.ChatCompletionRequest
import com.desafiodevspace.metamanager.data.remote.dto.ChatCompletionResponse
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface OpenAIApi {
    @POST("v1/chat/completions")
    suspend fun createChatCompletion(
        @Header("Authorization") apiKey: String,
        @Body request: ChatCompletionRequest
    ): ChatCompletionResponse
}
