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

class DeleteGoalUseCaseTest {

    private lateinit var useCase: DeleteGoalUseCase
    private val repository: GoalRepository = mockk()

    @Before
    fun setup() {
        useCase = DeleteGoalUseCase(repository)
    }

    @Test
    fun `invoke should call repository to delete goal`() = runBlocking {
        // Arrange
        val fakeGoal = Goal(1, "Test Goal", "Description", 100.0, 0.0, Date(), emptyList())
        val goalSlot = slot<Goal>()
        coEvery { repository.deleteGoal(capture(goalSlot)) } returns Unit

        // Act
        useCase.invoke(fakeGoal)

        // Assert
        coVerify(exactly = 1) { repository.deleteGoal(any()) }
        assertEquals(fakeGoal, goalSlot.captured)
    }
}
