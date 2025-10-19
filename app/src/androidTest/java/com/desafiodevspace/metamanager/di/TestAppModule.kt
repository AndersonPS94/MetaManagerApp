package com.desafiodevspace.metamanager.di

import com.desafiodevspace.metamanager.data.model.Goal
import com.desafiodevspace.metamanager.domain.repository.GoalRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Singleton

// Fake Repository for UI Tests
class FakeGoalRepository : GoalRepository {
    private val goals = mutableListOf<Goal>()

    override fun getAllGoals(): Flow<List<Goal>> {
        return flowOf(goals)
    }

    override suspend fun addGoal(goal: Goal): String {
        goals.add(goal.copy(id = "fake-id"))
        return "fake-id"
    }

    override suspend fun updateGoal(goal: Goal) {
        // Not implemented for this test
    }

    override suspend fun deleteGoal(goal: Goal) {
        // Not implemented for this test
    }
}

@Module
@InstallIn(SingletonComponent::class)
object TestAppModule {

    @Provides
    @Singleton
    fun provideGoalRepository(): GoalRepository {
        return FakeGoalRepository()
    }
}
