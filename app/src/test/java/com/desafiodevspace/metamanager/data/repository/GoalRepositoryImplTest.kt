package com.desafiodevspace.metamanager.data.repository

import com.desafiodevspace.metamanager.data.local.GoalDao
import com.desafiodevspace.metamanager.data.model.Goal
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import java.util.Date

class GoalRepositoryImplTest {

    private lateinit var repository: GoalRepositoryImpl
    private val firestore: FirebaseFirestore = mockk()
    private val goalDao: GoalDao = mockk(relaxed = true) // Relaxed, as it's not used

    private val collectionReference: CollectionReference = mockk()
    private val documentReference: DocumentReference = mockk()
    private val mockVoidTask: Task<Void> = mockk(relaxed = true)
    private val mockDocRefTask: Task<DocumentReference> = mockk(relaxed = true)

    @Before
    fun setup() {
        // Mock the general Firestore structure
        every { firestore.collection("goals") } returns collectionReference
        every { collectionReference.document(any()) } returns documentReference
        every { collectionReference.add(any()) } returns mockDocRefTask
        every { documentReference.set(any()) } returns mockVoidTask
        every { documentReference.delete() } returns mockVoidTask
        
        // Mock the behavior of Tasks to simulate success
        every { mockDocRefTask.isSuccessful } returns true
        every { mockDocRefTask.isComplete } returns true
        every { mockDocRefTask.result } returns documentReference
        every { documentReference.id } returns "fake-id-123"

        repository = GoalRepositoryImpl(firestore, goalDao)
    }

    @Test
    fun `addGoal should call firestore add and return document id`() = runBlocking {
        val fakeGoal = Goal(id = "", title = "Test Goal", description = "", totalAmount = 0.0, currentAmount = 0.0, targetDate = Date(), dailyTasks = emptyList())

        val resultId = repository.addGoal(fakeGoal)

        coVerify(exactly = 1) { collectionReference.add(fakeGoal) }
        assert(resultId == "fake-id-123")
    }

    @Test
    fun `updateGoal should call firestore set`() = runBlocking {
        val fakeGoal = Goal(id = "goal-to-update", title = "Updated Goal", description = "", totalAmount = 0.0, currentAmount = 0.0, targetDate = Date(), dailyTasks = emptyList())
        every { firestore.collection("goals").document(fakeGoal.id) } returns documentReference

        repository.updateGoal(fakeGoal)

        coVerify(exactly = 1) { documentReference.set(fakeGoal) }
    }

    @Test
    fun `deleteGoal should call firestore delete`() = runBlocking {
        val fakeGoal = Goal(id = "goal-to-delete", title = "Goal to Delete", description = "", totalAmount = 0.0, currentAmount = 0.0, targetDate = Date(), dailyTasks = emptyList())
        every { firestore.collection("goals").document(fakeGoal.id) } returns documentReference

        repository.deleteGoal(fakeGoal)

        coVerify(exactly = 1) { documentReference.delete() }
    }
}
