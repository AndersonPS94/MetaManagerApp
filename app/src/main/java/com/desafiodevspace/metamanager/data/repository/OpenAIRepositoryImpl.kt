package com.desafiodevspace.metamanager.data.repository

import com.desafiodevspace.metamanager.data.model.Goal
import com.desafiodevspace.metamanager.data.remote.OpenAIService
import com.desafiodevspace.metamanager.domain.repository.OpenAIRepository
import javax.inject.Inject

class OpenAIRepositoryImpl @Inject constructor(
    private val openAIService: OpenAIService
) : OpenAIRepository {

    override suspend fun getReplan(goal: Goal, userSituation: String): String {
        val prompt = createPrompt(goal, userSituation)
        // Aqui viria a lógica para chamar o openAIService
        // Por enquanto, vamos retornar uma resposta mockada
        return "Aqui está um plano de replanejamento sugerido pela IA."
    }

    private fun createPrompt(goal: Goal, userSituation: String): String {
        // Lógica para criar o prompt para a API da OpenAI
        return "Para a meta '${goal.title}' com data final em ${goal.targetDate}, o usuário disse: '$userSituation'. Sugira um novo plano."
    }
}
