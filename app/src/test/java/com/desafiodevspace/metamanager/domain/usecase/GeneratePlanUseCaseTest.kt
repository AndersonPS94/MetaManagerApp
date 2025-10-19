package com.desafiodevspace.metamanager.domain.usecase

import com.desafiodevspace.metamanager.domain.repository.AIRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class GeneratePlanUseCaseTest {

    private lateinit var useCase: GeneratePlanUseCase
    private val repository: AIRepository = mockk()

    @Before
    fun setup() {
        useCase = GeneratePlanUseCase(repository)
    }

    @Test
    fun `invoke should call repository with correct parameters`() = runBlocking {
        // Arrange
        val goalTitle = "Learn a new language"
        val targetDate = "31/12/2024"
        val fakePlan = "This is a fake plan."
        coEvery { repository.generatePlan(goalTitle, targetDate) } returns fakePlan

        // Act
        useCase.invoke(goalTitle, targetDate)

        // Assert
        coVerify(exactly = 1) { repository.generatePlan(goalTitle, targetDate) }
    }
}
