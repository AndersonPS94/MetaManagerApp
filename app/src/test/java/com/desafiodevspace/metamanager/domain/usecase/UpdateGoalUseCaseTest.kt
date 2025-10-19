package com.desafiodevspace.metamanager.domain.usecase

import com.desafiodevspace.metamanager.data.model.Goal
import com.desafiodevspace.metamanager.domain.repository.GoalRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.util.Date

class UpdateGoalUseCaseTest {

    private lateinit var useCase: UpdateGoalUseCase
    private val repository: GoalRepository = mockk()

    @Before
    fun setup() {
        useCase = UpdateGoalUseCase(repository)
    }

    @Test
    fun `invoke should call repository to update goal`() = runBlocking {
        // Arrange
        val fakeGoal = Goal(1, "Test Goal", "Description", 100.0, 0.0, Date(), emptyList())
        val goalSlot = slot<Goal>()
        coEvery { repository.updateGoal(capture(goalSlot)) } returns Unit

        // Act
        useCase.invoke(fakeGoal)

        // Assert
        coVerify(exactly = 1) { repository.updateGoal(any()) }
        assertEquals(fakeGoal, goalSlot.captured)
    }
}
