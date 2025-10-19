package com.desafiodevspace.metamanager.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.desafiodevspace.metamanager.data.model.DailyTask
import com.desafiodevspace.metamanager.data.model.Goal
import com.desafiodevspace.metamanager.presentation.viewmodel.GoalViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditGoalScreen(
    navController: NavController,
    goalId: String?,
    viewModel: GoalViewModel = hiltViewModel()
) {
    val goals by viewModel.goals.collectAsState()
    val originalGoal = goals.find { it.id == goalId }

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    // Atualiza os estados locais quando a meta original for carregada
    LaunchedEffect(originalGoal) {
        originalGoal?.let {
            title = it.title
            description = it.description
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar Meta") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        }
    ) { padding ->
        if (originalGoal != null) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(16.dp)
            ) {
                item {
                    Column {
                        OutlinedTextField(
                            value = title,
                            onValueChange = { title = it },
                            label = { Text("Título da Meta") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        OutlinedTextField(
                            value = description,
                            onValueChange = { description = it },
                            label = { Text("Descrição") },
                            modifier = Modifier.fillMaxWidth(),
                            minLines = 3
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        HorizontalDivider()
                    }
                }

                items(originalGoal.dailyTasks, key = { it.day }) { dailyTask ->
                    DailyTaskEditor(goal = originalGoal, dailyTask = dailyTask, viewModel = viewModel)
                }

                item {
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        onClick = {
                            val updatedGoal = originalGoal.copy(
                                title = title,
                                description = description
                            )
                            viewModel.updateGoal(updatedGoal)
                            navController.popBackStack()
                        },
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        enabled = title.isNotBlank()
                    ) {
                        Text("Salvar Alterações")
                    }
                }
            }
        } else {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
    }
}

@Composable
private fun DailyTaskEditor(
    goal: Goal,
    dailyTask: DailyTask,
    viewModel: GoalViewModel
) {
    var newTaskDescription by remember { mutableStateOf("") }

    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Dia ${dailyTask.day}",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            dailyTask.tasks.forEach { task ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp)
                ) {
                    Text(text = task.description, modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodyLarge)
                    IconButton(onClick = { viewModel.removeTask(goal, dailyTask, task) }) {
                        Icon(Icons.Default.Delete, contentDescription = "Remover Tarefa", tint = MaterialTheme.colorScheme.error)
                    }
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
            ) {
                OutlinedTextField(
                    value = newTaskDescription,
                    onValueChange = { newTaskDescription = it },
                    label = { Text("Adicionar nova tarefa") },
                    modifier = Modifier.weight(1f)
                )
                IconButton(
                    enabled = newTaskDescription.isNotBlank(),
                    onClick = {
                        viewModel.addTask(goal, dailyTask, newTaskDescription)
                        newTaskDescription = "" // Limpa o campo
                    }
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Adicionar Tarefa")
                }
            }
        }
    }
}
