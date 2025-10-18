package com.desafiodevspace.metamanager.domain.usecase

import com.desafiodevspace.metamanager.domain.repository.OpenAIRepository
import javax.inject.Inject

class GeneratePlanUseCase @Inject constructor(
    private val repository: OpenAIRepository
) {
    suspend operator fun invoke(goalTitle: String, targetDate: String): String {
        val prompt = "Crie um plano de tarefas diárias para a meta '$goalTitle' a ser concluída até '$targetDate'. " +
            "O plano deve ser dividido em dias (ex: Dia 1, Dia 2, etc.) e cada dia deve ter uma lista de micro-tarefas. " +
            "O resultado deve ser apenas o plano, sem nenhum texto adicional."
        return repository.generatePlan(prompt)
    }
}
