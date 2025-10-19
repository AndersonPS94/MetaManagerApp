package com.desafiodevspace.metamanager.domain.usecase

import com.desafiodevspace.metamanager.data.model.Goal
import com.desafiodevspace.metamanager.domain.repository.HelpRepository
import javax.inject.Inject

class GetHelpUseCase @Inject constructor(
    private val helpRepository: HelpRepository
) {
    suspend operator fun invoke(goal: Goal, userSituation: String): String {
        return helpRepository.getReplan(goal, userSituation)
    }
}
