package com.desafiodevspace.metamanager.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.desafiodevspace.metamanager.domain.repository.GoalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class AnalyticsViewModel @Inject constructor(
    private val goalRepository: GoalRepository
) : ViewModel() {

    val percentageOfCompletedGoals: StateFlow<Float> = goalRepository.getAllGoals().map {
        val completedGoals = it.count { goal -> goal.dailyTasks.all { dt -> dt.tasks.all { t -> t.isCompleted } } }
        if (it.isEmpty()) 0f else (completedGoals.toFloat() / it.size.toFloat())
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0f)

    val totalTasks: StateFlow<Int> = goalRepository.getAllGoals().map {
        it.sumOf { goal -> goal.dailyTasks.sumOf { dt -> dt.tasks.size } }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val completedTasks: StateFlow<Int> = goalRepository.getAllGoals().map {
        it.sumOf { goal -> goal.dailyTasks.sumOf { dt -> dt.tasks.count { t -> t.isCompleted } } }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

}
