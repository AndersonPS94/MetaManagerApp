package com.desafiodevspace.metamanager.domain.usecase

import com.desafiodevspace.metamanager.data.model.Goal
import com.desafiodevspace.metamanager.domain.repository.GoalRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.util.Date

class GetAllGoalsUseCaseTest {

    private lateinit var useCase: GetAllGoalsUseCase
    private val repository: GoalRepository = mockk()

    @Before
    fun setup() {
        useCase = GetAllGoalsUseCase(repository)
    }

    @Test
    fun `invoke should return flow of goals from repository`() = runBlocking {
        // Arrange
        val fakeGoals = listOf(Goal(1, "Goal 1", "", 100.0, 0.0, Date(), emptyList()))
        coEvery { repository.getAllGoals() } returns flowOf(fakeGoals)

        // Act
        val resultFlow = useCase.invoke()
        val resultList = resultFlow.first()

        // Assert
        coVerify(exactly = 1) { repository.getAllGoals() }
        assertEquals(fakeGoals, resultList)
    }
}
