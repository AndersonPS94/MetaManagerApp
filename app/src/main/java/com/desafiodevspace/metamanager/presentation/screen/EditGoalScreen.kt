package com.desafiodevspace.metamanager.presentation.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.desafiodevspace.metamanager.data.model.DailyTask
import com.desafiodevspace.metamanager.presentation.viewmodel.GoalViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditGoalScreen(
    navController: NavController,
    goalId: String?,
    viewModel: GoalViewModel = hiltViewModel()
) {
    val goals by viewModel.goals.collectAsState()
    val goal = goals.find { it.id == goalId }
    val newTaskDescription = remember { mutableStateOf("") }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Editar Plano") }) }
    ) {
        Column(modifier = Modifier.padding(it).padding(16.dp)) {
            if (goal != null) {
                LazyColumn {
                    items(goal.dailyTasks) { dailyTask ->
                        Text(
                            text = "Dia ${dailyTask.day}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                        dailyTask.tasks.forEach { task ->
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = task.description, modifier = Modifier.weight(1f))
                                IconButton(onClick = { viewModel.removeTask(goal, dailyTask, task) }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Remover Tarefa")
                                }
                            }
                        }
                        // UI para adicionar nova tarefa
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            OutlinedTextField(
                                value = newTaskDescription.value,
                                onValueChange = { newTaskDescription.value = it },
                                label = { Text("Nova Tarefa") },
                                modifier = Modifier.weight(1f)
                            )
                            IconButton(onClick = { 
                                viewModel.addTask(goal, dailyTask, newTaskDescription.value)
                                newTaskDescription.value = "" // Limpa o campo
                             }) {
                                Icon(Icons.Default.Add, contentDescription = "Adicionar Tarefa")
                            }
                        }
                    }
                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { navController.popBackStack() }, modifier = Modifier.fillMaxWidth()) {
                            Text("Salvar e Voltar")
                        }
                    }
                }

            } else {
                CircularProgressIndicator()
            }
        }
    }
}
