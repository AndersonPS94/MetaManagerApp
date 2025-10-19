package com.desafiodevspace.metamanager.presentation

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.desafiodevspace.metamanager.MainActivity
import com.desafiodevspace.metamanager.data.model.Goal
import com.desafiodevspace.metamanager.di.RepositoryModule
import com.desafiodevspace.metamanager.domain.repository.GoalRepository
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.Date
import javax.inject.Inject

@UninstallModules(RepositoryModule::class)
@HiltAndroidTest
class GoalScreenTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Inject
    lateinit var repository: GoalRepository

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun goalScreen_displaysGoalFromRepository() {
        // Arrange: Add a fake goal to our fake repository
        val testGoal = Goal(id = "1", title = "Minha Meta de Teste", description = "", totalAmount = 100.0, currentAmount = 0.0, targetDate = Date(), dailyTasks = emptyList())
        runBlocking {
            repository.addGoal(testGoal)
        }

        // Act: The UI should recompose and display the new goal.
        // We don't need to do anything here as the screen is already visible.

        // Assert: Check if a node with the goal's title exists.
        composeTestRule.onNodeWithText("Minha Meta de Teste").assertExists()
    }
}
