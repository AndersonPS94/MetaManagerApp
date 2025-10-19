package com.desafiodevspace.metamanager.presentation

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.work.ListenableWorker
import androidx.work.testing.TestListenableWorkerBuilder
import com.desafiodevspace.metamanager.data.model.Goal
import com.desafiodevspace.metamanager.data.model.Task
import com.desafiodevspace.metamanager.di.RepositoryModule
import com.desafiodevspace.metamanager.domain.repository.GoalRepository
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.Date
import javax.inject.Inject

@UninstallModules(RepositoryModule::class)
@HiltAndroidTest
class NotificationWorkerTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var repository: GoalRepository

    private lateinit var context: Context

    @Before
    fun setup() {
        hiltRule.inject()
        context = ApplicationProvider.getApplicationContext()
    }

    @Test
    fun testWorker_whenTasksArePending_returnsSuccess() = runBlocking {
        // Arrange: Add a goal with an incomplete task to the fake repo
        val pendingTask = Task(description = "Pending Task", isCompleted = false)
        val goalWithPendingTasks = Goal("1", "Test", "", 1.0, 0.0, Date(), listOf(), listOf(pendingTask))
        repository.addGoal(goalWithPendingTasks)

        // Act: Build and run the worker
        val worker = TestListenableWorkerBuilder<NotificationWorker>(context).build()
        val result = worker.doWork()

        // Assert
        assertEquals(ListenableWorker.Result.success(), result)
    }

    @Test
    fun testWorker_whenNoTasksArePending_returnsSuccess() = runBlocking {
        // Arrange: Repository is empty by default in tests

        // Act: Build and run the worker
        val worker = TestListenableWorkerBuilder<NotificationWorker>(context).build()
        val result = worker.doWork()

        // Assert
        assertEquals(ListenableWorker.Result.success(), result)
    }
}
