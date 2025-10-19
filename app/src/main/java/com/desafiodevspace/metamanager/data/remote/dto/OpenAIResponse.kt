package com.desafiodevspace.metamanager.data.remote.dto

data class OpenAIResponse(
    val choices: List<Choice>
) {
    data class Choice(
        val text: String
    )
}
