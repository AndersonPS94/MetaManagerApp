package com.desafiodevspace.metamanager.domain.usecase

import com.desafiodevspace.metamanager.domain.repository.GoalRepository
import javax.inject.Inject

class GetAllGoalsUseCase @Inject constructor(
    private val repository: GoalRepository
) {
    operator fun invoke() = repository.getAllGoals()
}
