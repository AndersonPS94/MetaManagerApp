package com.desafiodevspace.metamanager.data.remote.dto

data class OpenAIRequest(
    val model: String,
    val prompt: String,
    val max_tokens: Int,
    val temperature: Double
)
