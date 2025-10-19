package com.desafiodevspace.metamanager.data.repository

import com.desafiodevspace.metamanager.data.local.GoalDao
import com.desafiodevspace.metamanager.data.model.Goal
import com.desafiodevspace.metamanager.domain.repository.GoalRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject

class GoalRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val goalDao: GoalDao
) : GoalRepository {

    override fun getAllGoals(): Flow<List<Goal>> {
        return goalDao.getAllGoals().onStart {
            Timber.d("Iniciando sincronização única com o Firebase.")
            try {
                val snapshot = firestore.collection("goals").get().await()
                val remoteGoals = snapshot.documents.mapNotNull { document ->
                    document.toObject(Goal::class.java)?.copy(id = document.id)
                }
                goalDao.deleteAll()
                goalDao.insertAll(remoteGoals)
                Timber.d("Sincronização com o Firebase concluída com sucesso.")
            } catch (e: Exception) {
                Timber.e(e, "Erro ao sincronizar metas com o Firebase")
            }
        }
    }

    override suspend fun addGoal(goal: Goal): String {
        Timber.d("Adicionando nova meta")
        return try {
            val documentReference = firestore.collection("goals").add(goal).await()
            val newGoal = goal.copy(id = documentReference.id)
            goalDao.insert(newGoal) // Adiciona ao Room com o ID do Firebase
            Timber.d("Meta adicionada com sucesso no Firebase e Room. ID: %s", documentReference.id)
            documentReference.id
        } catch (e: Exception) {
            Timber.e(e, "Erro ao adicionar meta")
            ""
        }
    }

    override suspend fun updateGoal(goal: Goal) {
        Timber.d("Atualizando meta com ID: %s", goal.id)
        try {
            goalDao.insert(goal) // Atualiza o Room primeiro para feedback imediato na UI
            firestore.collection("goals").document(goal.id).set(goal).await()
            Timber.d("Meta atualizada com sucesso no Room e Firebase.")
        } catch (e: Exception) {
            Timber.e(e, "Erro ao atualizar meta")
        }
    }

    override suspend fun deleteGoal(goal: Goal) {
        Timber.d("Excluindo meta com ID: %s", goal.id)
        try {
            goalDao.delete(goal) // Deleta do Room primeiro
            firestore.collection("goals").document(goal.id).delete().await()
            Timber.d("Meta excluída com sucesso do Room e Firebase.")
        } catch (e: Exception) {
            Timber.e(e, "Erro ao excluir meta")
        }
    }
}
