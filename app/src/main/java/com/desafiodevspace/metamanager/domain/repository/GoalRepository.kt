package com.desafiodevspace.metamanager.domain.repository

import com.desafiodevspace.metamanager.data.model.Goal
import kotlinx.coroutines.flow.Flow

interface GoalRepository {
    fun getAllGoals(): Flow<List<Goal>>
    suspend fun addGoal(goal: Goal): String
    suspend fun updateGoal(goal: Goal)
    suspend fun deleteGoal(goal: Goal)
}
