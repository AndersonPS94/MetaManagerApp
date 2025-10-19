package com.desafiodevspace.metamanager.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.desafiodevspace.metamanager.data.model.Goal
import com.desafiodevspace.metamanager.domain.usecase.AddGoalUseCase
import com.google.firebase.Timestamp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class AddGoalViewModel @Inject constructor(
    private val addGoalUseCase: AddGoalUseCase
) : ViewModel() {

    private val _addGoalState = MutableStateFlow<AddGoalState>(AddGoalState.Idle)
    val addGoalState: StateFlow<AddGoalState> = _addGoalState

    fun addGoal(title: String, description: String, targetDate: Date) {
        viewModelScope.launch {
            _addGoalState.value = AddGoalState.Loading
            try {
                val goal = Goal(
                    title = title,
                    description = description,
                    targetDate = Timestamp(targetDate)
                )
                val goalId = addGoalUseCase(goal)
                _addGoalState.value = AddGoalState.Success(goalId)
            } catch (e: Exception) {
                _addGoalState.value = AddGoalState.Error(e.message ?: "Unknown error")
            }
        }
    }
}

sealed class AddGoalState {
    object Idle : AddGoalState()
    object Loading : AddGoalState()
    data class Success(val goalId: String) : AddGoalState()
    data class Error(val message: String) : AddGoalState()
}
