package com.desafiodevspace.metamanager.data.remote.dto

import com.google.gson.annotations.SerializedName

data class OpenAIRequest(
    val model: String,
    val prompt: String,
    @SerializedName("max_tokens")
    val maxTokens: Int,
    val temperature: Double
)
