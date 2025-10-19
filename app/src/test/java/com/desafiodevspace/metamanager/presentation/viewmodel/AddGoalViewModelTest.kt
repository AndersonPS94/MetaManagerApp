package com.desafiodevspace.metamanager.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.desafiodevspace.metamanager.domain.usecase.AddGoalUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.Date

@ExperimentalCoroutinesApi
class AddGoalViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: AddGoalViewModel
    private val addGoalUseCase: AddGoalUseCase = mockk()

    @Before
    fun setup() {
        viewModel = AddGoalViewModel(addGoalUseCase)
    }

    @Test
    fun `addGoal should transition to Success when use case succeeds`() = runBlocking {
        val goalSlot = slot<com.desafiodevspace.metamanager.data.model.Goal>()
        val fakeGoalId = "12345"
        coEvery { addGoalUseCase(capture(goalSlot)) } returns fakeGoalId

        val title = "Test Goal"
        val description = "Test Description"
        val date = Date()

        // Initial state
        assertTrue(viewModel.addGoalState.value is AddGoalState.Idle)

        // Action
        viewModel.addGoal(title, description, date)

        // Verification
        assertTrue(viewModel.addGoalState.value is AddGoalState.Loading)
        
        // After coroutine completion
        coVerify { addGoalUseCase(any()) }
        val successState = viewModel.addGoalState.value as AddGoalState.Success
        assertEquals(fakeGoalId, successState.goalId)
        assertEquals(title, goalSlot.captured.title)
        assertEquals(description, goalSlot.captured.description)
    }

    @Test
    fun `addGoal should transition to Error when use case throws exception`() = runBlocking {
        val errorMessage = "Database error"
        coEvery { addGoalUseCase(any()) } throws RuntimeException(errorMessage)

        val title = "Test Goal"
        val description = "Test Description"
        val date = Date()

        // Action
        viewModel.addGoal(title, description, date)

        // Verification
        assertTrue(viewModel.addGoalState.value is AddGoalState.Loading)
        
        // After coroutine completion
        coVerify { addGoalUseCase(any()) }
        val errorState = viewModel.addGoalState.value as AddGoalState.Error
        assertEquals(errorMessage, errorState.message)
    }
}
