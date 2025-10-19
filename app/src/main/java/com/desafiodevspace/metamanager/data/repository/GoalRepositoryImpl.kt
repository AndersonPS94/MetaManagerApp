package com.desafiodevspace.metamanager.data.repository

import com.desafiodevspace.metamanager.data.local.GoalDao
import com.desafiodevspace.metamanager.data.model.Goal
import com.desafiodevspace.metamanager.domain.repository.GoalRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GoalRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val goalDao: GoalDao
) : GoalRepository {

    override fun getAllGoals(): Flow<List<Goal>> {
        return firestore.collection("goals").snapshots().map { snapshot ->
            snapshot.toObjects(Goal::class.java)
        }
    }

    override suspend fun addGoal(goal: Goal): String {
        return try {
            val documentReference = firestore.collection("goals").add(goal).await()
            Timber.d("Meta adicionada com sucesso no Firebase. ID: %s", documentReference.id)
            documentReference.id
        } catch (e: Exception) {
            Timber.e(e, "Erro ao adicionar meta")
            ""
        }
    }

    override suspend fun updateGoal(goal: Goal) {
        Timber.d("Atualizando meta com ID: %s", goal.id)
        try {
            firestore.collection("goals").document(goal.id).set(goal).await()
            Timber.d("Meta atualizada com sucesso no Firebase.")
        } catch (e: Exception) {
            Timber.e(e, "Erro ao atualizar meta")
        }
    }

    override suspend fun deleteGoal(goal: Goal) {
        Timber.d("Excluindo meta com ID: %s", goal.id)
        try {
            firestore.collection("goals").document(goal.id).delete().await()
            Timber.d("Meta excluída com sucesso do Firebase.")
        } catch (e: Exception) {
            Timber.e(e, "Erro ao excluir meta")
        }
    }
}
