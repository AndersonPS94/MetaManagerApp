package com.desafiodevspace.metamanager.domain.usecase

import com.desafiodevspace.metamanager.data.model.Goal
import com.desafiodevspace.metamanager.domain.repository.AIRepository
import javax.inject.Inject

class GetHelpUseCase @Inject constructor(
    private val repository: AIRepository
) {
    suspend operator fun invoke(goal: Goal, situation: String): String {
        val planDetails = goal.dailyTasks.joinToString("\n") { dailyTask ->
            "Dia ${dailyTask.day}:\n" + dailyTask.tasks.joinToString("\n") { task ->
                "- ${task.description} (Concluída: ${if (task.isCompleted) "Sim" else "Não"})"
            }
        }

        val prompt = """
        Estou trabalhando na meta: '${goal.title}'.
        O prazo é ${goal.targetDate.toDate()}.
        
        O meu plano atual é:
        $planDetails
        
        A minha situação é: '$situation'.
        
        Preciso de ajuda. Por favor, me dê sugestões concisas e práticas sobre como posso ajustar meu plano ou minha rotina para atingir minha meta. O resultado deve ser apenas a sua sugestão, sem nenhum texto adicional.
        """

        return repository.generateGenericResponse(prompt)
    }
}
