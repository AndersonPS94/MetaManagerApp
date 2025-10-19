package com.desafiodevspace.metamanager.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ChatCompletionRequest(
    val model: String = "gpt-3.5-turbo",
    val messages: List<Message>,
    @SerializedName("max_tokens")
    val maxTokens: Int = 1500,
    val temperature: Double = 0.7
)
