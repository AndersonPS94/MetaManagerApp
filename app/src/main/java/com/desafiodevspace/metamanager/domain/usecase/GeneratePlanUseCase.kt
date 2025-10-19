package com.desafiodevspace.metamanager.domain.usecase

import com.desafiodevspace.metamanager.domain.repository.AIRepository
import javax.inject.Inject

class GeneratePlanUseCase @Inject constructor(
    private val repository: AIRepository
) {
    suspend operator fun invoke(goalTitle: String, targetDate: String): String {
        return repository.generatePlan(goalTitle, targetDate)
    }
}
