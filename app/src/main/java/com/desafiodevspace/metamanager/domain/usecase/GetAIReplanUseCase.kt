package com.desafiodevspace.metamanager.domain.usecase

import com.desafiodevspace.metamanager.data.model.Goal
import com.desafiodevspace.metamanager.domain.repository.OpenAIRepository
import javax.inject.Inject

class GetAIReplanUseCase @Inject constructor(
    private val openAIRepository: OpenAIRepository
) {
    suspend operator fun invoke(goal: Goal, userSituation: String): String {
        return openAIRepository.getReplan(goal, userSituation)
    }
}
