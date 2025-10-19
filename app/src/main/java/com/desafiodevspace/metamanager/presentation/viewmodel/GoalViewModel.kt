package com.desafiodevspace.metamanager.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
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
import com.desafiodevspace.metamanager.presentation.NotificationWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class GoalViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val getAllGoalsUseCase: GetAllGoalsUseCase,
    private val addGoalUseCase: AddGoalUseCase,
    private val updateGoalUseCase: UpdateGoalUseCase,
    private val deleteGoalUseCase: DeleteGoalUseCase,
    private val generatePlanUseCase: GeneratePlanUseCase,
    private val getHelpUseCase: GetHelpUseCase,
    private val shareUseCase: ShareUseCase
) : ViewModel() {

    private val _goals = MutableStateFlow<List<Goal>>(emptyList())
    val goals: StateFlow<List<Goal>> = _goals.asStateFlow()

    private val _helpSuggestion = MutableStateFlow<String?>(null)
    val helpSuggestion: StateFlow<String?> = _helpSuggestion.asStateFlow()

    private val _generatedPlan = MutableStateFlow<String?>(null)
    val generatedPlan: StateFlow<String?> = _generatedPlan.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private var tempGoal: Goal? = null

    init {
        loadGoals()
    }

    private fun loadGoals() {
        viewModelScope.launch {
            getAllGoalsUseCase().collect { _goals.value = it }
        }
    }

    fun generatePlanForNewGoal(goal: Goal) {
        tempGoal = goal
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val targetDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(goal.targetDate.toDate())
                _generatedPlan.value = generatePlanUseCase(goal.title, targetDate)
            } catch (e: HttpException) {
                if (e.code() == 429) {
                    _generatedPlan.value = "Erro: Limite de requisições da API da OpenAI excedido. Verifique seu plano e detalhes de faturamento."
                } else {
                    _generatedPlan.value = "Erro de rede ao se comunicar com a IA. Tente novamente."
                }
            } catch (e: Exception) {
                _generatedPlan.value = "Ocorreu um erro inesperado ao gerar o plano. Tente novamente."
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun regeneratePlan() {
        tempGoal?.let { generatePlanForNewGoal(it) }
    }

    fun saveGeneratedPlan() {
        viewModelScope.launch {
            val planText = _generatedPlan.value ?: return@launch
            if (planText.startsWith("Erro:")) return@launch

            val goalToSave = tempGoal ?: return@launch

            val dailyTasks = parsePlanToDailyTasks(planText)
            val finalGoal = goalToSave.copy(dailyTasks = dailyTasks)

            addGoalUseCase(finalGoal)
            scheduleDailyNotifications()
            tempGoal = null
            _generatedPlan.value = null
        }
    }

    fun updateGoal(goal: Goal) {
        viewModelScope.launch { updateGoalUseCase(goal) }
    }

    fun deleteGoal(goal: Goal) {
        viewModelScope.launch { deleteGoalUseCase(goal) }
    }

    fun toggleTaskCompletion(goal: Goal, dailyTask: DailyTask, taskToToggle: Task) {
        val updatedGoal = goal.copy(
            dailyTasks = goal.dailyTasks.map {
                if (it.day == dailyTask.day) {
                    it.copy(tasks = it.tasks.map { task ->
                        if (task == taskToToggle) task.copy(isCompleted = !task.isCompleted) else task
                    })
                } else {
                    it
                }
            }
        )
        updateGoal(updatedGoal)
    }

    fun addTask(goal: Goal, dailyTask: DailyTask, description: String) {
        if (description.isBlank()) return
        val newTask = Task(description = description)
        val updatedGoal = goal.copy(
            dailyTasks = goal.dailyTasks.map {
                if (it.day == dailyTask.day) {
                    it.copy(tasks = it.tasks + newTask)
                } else {
                    it
                }
            }
        )
        updateGoal(updatedGoal)
    }

    fun removeTask(goal: Goal, dailyTask: DailyTask, taskToRemove: Task) {
        val updatedGoal = goal.copy(
            dailyTasks = goal.dailyTasks.map {
                if (it.day == dailyTask.day) {
                    it.copy(tasks = it.tasks - taskToRemove)
                } else {
                    it
                }
            }
        )
        updateGoal(updatedGoal)
    }

    fun getHelp(goal: Goal, situation: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _helpSuggestion.value = getHelpUseCase(goal, situation)
            } catch (e: HttpException) {
                _helpSuggestion.value = "Erro: Limite de requisições da API da OpenAI excedido."
            } catch (e: Exception) {
                _helpSuggestion.value = "Ocorreu um erro inesperado. Tente novamente."
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun acceptHelpSuggestion(goal: Goal, suggestion: String) {
        if (suggestion.startsWith("Erro:")) return
        val newDailyTasks = parsePlanToDailyTasks(suggestion)
        val updatedGoal = goal.copy(dailyTasks = newDailyTasks)
        updateGoal(updatedGoal)
        clearHelpSuggestion()
    }

    fun clearHelpSuggestion() {
        _helpSuggestion.value = null
    }

    fun shareGoal(goal: Goal) {
        shareUseCase(context, goal)
    }

    fun calculateConsecutiveDays(goal: Goal): Int {
        var consecutiveDays = 0
        goal.dailyTasks.sortedBy { it.day }.forEach { dailyTask ->
            if (dailyTask.tasks.isNotEmpty() && dailyTask.tasks.all { it.isCompleted }) {
                consecutiveDays++
            } else {
                return consecutiveDays // A sequência é quebrada
            }
        }
        return consecutiveDays
    }

    private fun scheduleDailyNotifications() {
        val workRequest = PeriodicWorkRequestBuilder<NotificationWorker>(24, TimeUnit.HOURS).build()
        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "daily_goal_notification",
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )
    }

    private fun parsePlanToDailyTasks(planText: String): List<DailyTask> {
        val dailyTasks = mutableListOf<DailyTask>()
        val cleanedText = planText.replace("*", "").trim()
        val lines = cleanedText.lines().filter { it.isNotBlank() }

        var currentDay = -1
        val currentTasks = mutableListOf<Task>()

        lines.forEach { line ->
            val trimmedLine = line.trim()
            if (trimmedLine.startsWith("Dia", ignoreCase = true)) {
                // Salva o dia anterior antes de começar um novo
                if (currentDay != -1 && currentTasks.isNotEmpty()) {
                    dailyTasks.add(DailyTask(day = currentDay, tasks = currentTasks.toList()))
                    currentTasks.clear()
                }
                currentDay = trimmedLine.substringAfter("Dia").substringBefore(":").trim().toIntOrNull() ?: -1
            } else if (currentDay != -1 && trimmedLine.firstOrNull()?.isDigit() == true) {
                // Linha de tarefa (começa com "1.", "2.", etc.)
                val taskDescription = trimmedLine.substringAfter(".").trim()
                if (taskDescription.isNotEmpty()) {
                    currentTasks.add(Task(description = taskDescription))
                }
            }
        }

        // Salva o último dia
        if (currentDay != -1 && currentTasks.isNotEmpty()) {
            dailyTasks.add(DailyTask(day = currentDay, tasks = currentTasks.toList()))
        }

        return dailyTasks
    }
}
