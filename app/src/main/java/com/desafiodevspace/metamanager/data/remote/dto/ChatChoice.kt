package com.desafiodevspace.metamanager.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ChatChoice(
    val index: Int,
    val message: ResponseMessage?,
    @SerializedName("finish_reason")
    val finishReason: String?
)
