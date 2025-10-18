package com.desafiodevspace.metamanager.data.repository

import com.desafiodevspace.metamanager.data.remote.OpenAIApi
import com.desafiodevspace.metamanager.data.remote.dto.OpenAIRequest
import com.desafiodevspace.metamanager.domain.repository.OpenAIRepository
import javax.inject.Inject

class OpenAIRepositoryImpl @Inject constructor(
    private val openAIApi: OpenAIApi
) : OpenAIRepository {

    private val apiKey = "YOUR_OPENAI_API_KEY" // TODO: Move to a secure place

    override suspend fun generatePlan(prompt: String): String {
        val request = OpenAIRequest(
            model = "text-davinci-003", // Or another suitable model
            prompt = prompt,
            max_tokens = 500,
            temperature = 0.7
        )
        val response = openAIApi.getCompletion("Bearer $apiKey", request)
        return response.choices.firstOrNull()?.text ?: ""
    }
}
