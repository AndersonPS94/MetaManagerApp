package com.desafiodevspace.metamanager.data.repository

import com.desafiodevspace.metamanager.BuildConfig
import com.desafiodevspace.metamanager.data.model.Goal
import com.desafiodevspace.metamanager.data.remote.OpenAIService
import com.desafiodevspace.metamanager.data.remote.dto.OpenAIRequest
import com.desafiodevspace.metamanager.domain.repository.HelpRepository
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

class HelpRepositoryImpl @Inject constructor(
    private val openAIService: OpenAIService
) : HelpRepository {

    override suspend fun getReplan(goal: Goal, userSituation: String): String {
        val prompt = createPromptForReplan(goal, userSituation)
        val request = OpenAIRequest(
            model = "text-davinci-003",
            prompt = prompt,
            maxTokens = 250,
            temperature = 0.7
        )

        val apiKey = "Bearer ${BuildConfig.OPENAI_API_KEY}"
        val response = openAIService.getCompletion(apiKey, request)

        return response.choices.firstOrNull()?.text?.trim() ?: "Não foi possível gerar uma sugestão."
    }

    private fun createPromptForReplan(goal: Goal, userSituation: String): String {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val targetDate = dateFormat.format(goal.targetDate.toDate())
        val completedTasks = goal.dailyTasks.flatMap { it.tasks }.count { it.isCompleted }
        val totalTasks = goal.dailyTasks.flatMap { it.tasks }.size

        return """
        Você é um assistente de produtividade. Um usuário precisa de ajuda com a seguinte meta:
        
        Meta: ${goal.title}
        Descrição: ${goal.description}
        Prazo Final: $targetDate
        Progresso Atual: $completedTasks de $totalTasks tarefas concluídas.

        O usuário descreveu sua situação assim: "$userSituation"

        Com base nisso, gere um novo plano de ação conciso em formato de lista (dias ou passos) para ajudar o usuário a voltar aos trilhos. A resposta deve ser apenas o plano, sem frases introdutórias.
        """
    }
}
