package com.desafiodevspace.metamanager.presentation.viewmodel

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.desafiodevspace.metamanager.data.model.DailyTask
import com.desafiodevspace.metamanager.data.model.Goal
import com.desafiodevspace.metamanager.data.model.Task
import com.desafiodevspace.metamanager.domain.usecase.AddGoalUseCase
import com.desafiodevspace.metamanager.domain.usecase.DeleteGoalUseCase
import com.desafiodevspace.metamanager.domain.usecase.GeneratePlanUseCase
import com.desafiodevspace.metamanager.domain.usecase.GetAllGoalsUseCase
import com.desafiodevspace.metamanager.domain.usecase.GetHelpUseCase
import com.desafiodevspace.metamanager.domain.usecase.ShareUseCase
import com.desafiodevspace.metamanager.domain.usecase.UpdateGoalUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@ExperimentalCoroutinesApi
class GoalViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: GoalViewModel
    private val context: Context = mockk(relaxed = true)
    private val getAllGoalsUseCase: GetAllGoalsUseCase = mockk(relaxed = true)
    private val addGoalUseCase: AddGoalUseCase = mockk(relaxed = true)
    private val updateGoalUseCase: UpdateGoalUseCase = mockk(relaxed = true)
    private val deleteGoalUseCase: DeleteGoalUseCase = mockk(relaxed = true)
    private val generatePlanUseCase: GeneratePlanUseCase = mockk(relaxed = true)
    private val getHelpUseCase: GetHelpUseCase = mockk(relaxed = true)
    private val shareUseCase: ShareUseCase = mockk(relaxed = true)

    private val fakeTask = Task(description = "Initial Task", isCompleted = false)
    private val fakeDailyTask = DailyTask(day = 1, tasks = listOf(fakeTask))
    private val fakeGoal = Goal(1, "Fake Goal", "", 0.0, 0.0, Date(), listOf(fakeDailyTask))

    @Before
    fun setup() {
        coEvery { getAllGoalsUseCase.invoke() } returns flowOf(listOf(fakeGoal))

        viewModel = GoalViewModel(
            context = context,
            getAllGoalsUseCase = getAllGoalsUseCase,
            addGoalUseCase = addGoalUseCase,
            updateGoalUseCase = updateGoalUseCase,
            deleteGoalUseCase = deleteGoalUseCase,
            generatePlanUseCase = generatePlanUseCase,
            getHelpUseCase = getHelpUseCase,
            shareUseCase = shareUseCase
        )
    }

    @Test
    fun `loadGoals should update goals state flow on init`() = runBlocking {
        val goals = viewModel.goals.first()
        assertEquals(1, goals.size)
        assertEquals("Fake Goal", goals[0].title)
    }

    @Test
    fun `generatePlanForNewGoal should update generatedPlan and isLoading state`() = runBlocking {
        val newGoal = Goal(2, "New Goal", "Desc", 100.0, 0.0, Date(), emptyList())
        val fakePlan = "Dia 1: Fazer algo."
        val targetDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(newGoal.targetDate.toDate())
        coEvery { generatePlanUseCase(newGoal.title, targetDate) } returns fakePlan

        viewModel.generatePlanForNewGoal(newGoal)

        coVerify { generatePlanUseCase(newGoal.title, targetDate) }
        assertEquals(fakePlan, viewModel.generatedPlan.value)
    }

    @Test
    fun `toggleTaskCompletion should call updateGoalUseCase with updated task`() {
        val goalSlot = slot<Goal>()
        coEvery { updateGoalUseCase(capture(goalSlot)) } returns Unit

        viewModel.toggleTaskCompletion(fakeGoal, fakeDailyTask, fakeTask)

        coVerify { updateGoalUseCase(any()) }
        val capturedGoal = goalSlot.captured
        val updatedTask = capturedGoal.dailyTasks.first().tasks.first()
        assertTrue(updatedTask.isCompleted)
    }

    @Test
    fun `addTask should call updateGoalUseCase with new task added`() {
        val goalSlot = slot<Goal>()
        coEvery { updateGoalUseCase(capture(goalSlot)) } returns Unit
        val newTaskDescription = "A new task"

        viewModel.addTask(fakeGoal, fakeDailyTask, newTaskDescription)

        coVerify { updateGoalUseCase(any()) }
        val capturedGoal = goalSlot.captured
        val lastTask = capturedGoal.dailyTasks.first().tasks.last()
        assertEquals(2, capturedGoal.dailyTasks.first().tasks.size)
        assertEquals(newTaskDescription, lastTask.description)
    }

    @Test
    fun `removeTask should call updateGoalUseCase with task removed`() {
        val goalSlot = slot<Goal>()
        coEvery { updateGoalUseCase(capture(goalSlot)) } returns Unit

        viewModel.removeTask(fakeGoal, fakeDailyTask, fakeTask)

        coVerify { updateGoalUseCase(any()) }
        val capturedGoal = goalSlot.captured
        assertTrue(capturedGoal.dailyTasks.first().tasks.isEmpty())
    }

    @Test
    fun `calculateConsecutiveDays should return 2 when first two days are completed`() {
        val goal = Goal(
            id = 1, title = "Test Goal", description = "Description", totalAmount = 100.0, currentAmount = 20.0, targetDate = Date(),
            dailyTasks = listOf(
                DailyTask(1, listOf(Task("T1", true), Task("T2", true))),
                DailyTask(2, listOf(Task("T3", true))),
                DailyTask(3, listOf(Task("T4", false))) // Sequence breaks here
            )
        )

        val consecutiveDays = viewModel.calculateConsecutiveDays(goal)

        assertEquals(2, consecutiveDays)
    }

    @Test
    fun `calculateConsecutiveDays should return 0 when first day is not completed`() {
        val goal = Goal(
            id = 1, title = "Test Goal", description = "Description", totalAmount = 100.0, currentAmount = 20.0, targetDate = Date(),
            dailyTasks = listOf(
                DailyTask(1, listOf(Task("T1", false))), // Sequence breaks here
                DailyTask(2, listOf(Task("T2", true)))
            )
        )

        val consecutiveDays = viewModel.calculateConsecutiveDays(goal)

        assertEquals(0, consecutiveDays)
    }
}
