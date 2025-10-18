package com.desafiodevspace.metamanager.data.repository

import com.desafiodevspace.metamanager.data.local.GoalDao
import com.desafiodevspace.metamanager.data.model.Goal
import com.desafiodevspace.metamanager.domain.repository.GoalRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class GoalRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val goalDao: GoalDao
) : GoalRepository {

    override fun getAllGoals(): Flow<List<Goal>> {
        return goalDao.getAllGoals().onEach { 
            // Quando o fluxo do Room for coletado, tentamos atualizar com dados do Firebase
            val remoteGoals = firestore.collection("goals").get().await().toObjects(Goal::class.java)
            goalDao.deleteAll()
            goalDao.insertAll(remoteGoals)
        }
    }

    override suspend fun addGoal(goal: Goal): String {
        val documentReference = firestore.collection("goals").add(goal).await()
        // Atualiza o Goal com o ID gerado e salva no Room
        val newGoal = goal.copy(id = documentReference.id)
        goalDao.insert(newGoal)
        return documentReference.id
    }

    override suspend fun updateGoal(goal: Goal) {
        firestore.collection("goals").document(goal.id).set(goal).await()
        goalDao.insert(goal) // O OnConflictStrategy.REPLACE cuida da atualização
    }

    override suspend fun deleteGoal(goal: Goal) {
        firestore.collection("goals").document(goal.id).delete().await()
        // A sincronização no getAllGoals() cuidará da remoção no Room
    }
}
