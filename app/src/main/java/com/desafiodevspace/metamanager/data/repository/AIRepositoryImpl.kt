package com.desafiodevspace.metamanager.data.repository

import com.desafiodevspace.metamanager.BuildConfig
import com.desafiodevspace.metamanager.data.remote.OpenAIApi
import com.desafiodevspace.metamanager.data.remote.dto.ChatCompletionRequest
import com.desafiodevspace.metamanager.data.remote.dto.Message
import com.desafiodevspace.metamanager.domain.repository.AIRepository
import timber.log.Timber
import javax.inject.Inject

class AIRepositoryImpl @Inject constructor(
    private val api: OpenAIApi
) : AIRepository {

    private val apiKey = BuildConfig.OPENAI_API_KEY

    override suspend fun generatePlan(meta: String, prazo: String): String {
        Timber.d("Gerando plano com a API de Chat da OpenAI para a meta: $meta")
        if (apiKey.isBlank()) {
            Timber.e("A chave de API da OpenAI não foi configurada no BuildConfig.")
            return "Erro: A chave de API da OpenAI não foi configurada."
        }

        return try {
            val prompt = "Crie um plano de tarefas diárias para a meta '$meta' a ser concluída até '$prazo'. " +
                         "Divida o plano por dias (Dia 1, Dia 2...) e liste micro-tarefas detalhadas."

            val request = ChatCompletionRequest(
                messages = listOf(
                    Message("system", "Você é um assistente que cria planos de ação diários."),
                    Message("user", prompt)
                )
            )

            val response = api.createChatCompletion("Bearer $apiKey", request)
            val content = response.choices.firstOrNull()?.message?.content ?: "Não foi possível gerar o plano."
            Timber.d("Plano gerado com sucesso.")
            content
        } catch (e: Exception) {
            Timber.e(e, "Erro ao gerar plano com a API de Chat da OpenAI")
            "Erro ao gerar plano: ${e.message}"
        }
    }

    override suspend fun generateGenericResponse(prompt: String): String {
        Timber.d("Gerando resposta genérica com a API de Chat da OpenAI")
        if (apiKey.isBlank()) {
            Timber.e("A chave de API da OpenAI não foi configurada no BuildConfig.")
            return "Erro: A chave de API da OpenAI não foi configurada."
        }

        return try {
            val request = ChatCompletionRequest(
                messages = listOf(
                    Message("system", "Você é um assistente prestativo."),
                    Message("user", prompt)
                )
            )

            val response = api.createChatCompletion("Bearer $apiKey", request)
            val content = response.choices.firstOrNull()?.message?.content ?: "Não foi possível gerar a resposta."
            Timber.d("Resposta genérica gerada com sucesso.")
            content
        } catch (e: Exception) {
            Timber.e(e, "Erro ao gerar resposta genérica com a API de Chat da OpenAI")
            "Erro ao gerar resposta: ${e.message}"
        }
    }
}
