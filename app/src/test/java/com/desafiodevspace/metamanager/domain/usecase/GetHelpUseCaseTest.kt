package com.desafiodevspace.metamanager.domain.usecase

import com.desafiodevspace.metamanager.data.model.Goal
import com.desafiodevspace.metamanager.domain.repository.HelpRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import java.util.Date

class GetHelpUseCaseTest {

    private lateinit var useCase: GetHelpUseCase
    private val repository: HelpRepository = mockk()

    @Before
    fun setup() {
        useCase = GetHelpUseCase(repository)
    }

    @Test
    fun `invoke should call repository with correct parameters`() = runBlocking {
        // Arrange
        val fakeGoal = Goal(1, "Test Goal", "Description", 100.0, 0.0, Date(), emptyList())
        val userSituation = "I'm feeling unmotivated."
        val fakeSuggestion = "Try breaking down your tasks."
        coEvery { repository.getReplan(fakeGoal, userSituation) } returns fakeSuggestion

        // Act
        useCase.invoke(fakeGoal, userSituation)

        // Assert
        coVerify(exactly = 1) { repository.getReplan(fakeGoal, userSituation) }
    }
}
