package com.desafiodevspace.metamanager.domain.repository

interface OpenAIRepository {
    suspend fun generatePlan(prompt: String): String
}
