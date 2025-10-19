package com.desafiodevspace.metamanager.domain.repository

interface AIRepository {
    suspend fun generatePlan(meta: String, prazo: String): String
    suspend fun generateGenericResponse(prompt: String): String
}
