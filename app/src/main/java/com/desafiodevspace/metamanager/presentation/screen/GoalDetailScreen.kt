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
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.desafiodevspace.metamanager.presentation.viewmodel.GoalViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoalDetailScreen(
    navController: NavController,
    goalId: String?,
    viewModel: GoalViewModel = hiltViewModel()
) {
    val goals by viewModel.goals.collectAsState()
    val goal = goals.find { it.id == goalId }
    val helpSuggestion by viewModel.helpSuggestion.collectAsState()

    var showHelpDialog by remember { mutableStateOf(false) }
    var userSituation by remember { mutableStateOf("") }

    if (showHelpDialog) {
        AlertDialog(
            onDismissRequest = { showHelpDialog = false },
            title = { Text("Precisa de Ajuda?") },
            text = {
                OutlinedTextField(
                    value = userSituation,
                    onValueChange = { userSituation = it },
                    label = { Text("Descreva sua situação (ex: estou atrasado)") }
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (goal != null) {
                            viewModel.getHelp(goal, userSituation)
                        }
                        showHelpDialog = false
                    }
                ) {
                    Text("Pedir Ajuda")
                }
            },
            dismissButton = { TextButton(onClick = { showHelpDialog = false }) { Text("Cancelar") } }
        )
    }

    if (helpSuggestion != null) {
        AlertDialog(
            onDismissRequest = { viewModel.clearHelpSuggestion() },
            title = { Text("Sugestão da IA") },
            text = { Text(helpSuggestion!!) },
            confirmButton = { TextButton(onClick = { viewModel.clearHelpSuggestion() }) { Text("OK") } }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(goal?.title ?: "Detalhes da Meta") },
                actions = {
                    if (goal != null) {
                        IconButton(onClick = { viewModel.shareGoal(goal) }) {
                            Icon(Icons.Default.Share, contentDescription = "Compartilhar")
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            if (goal != null) {
                FloatingActionButton(onClick = { navController.navigate("edit_goal/${goal.id}") }) {
                    Icon(Icons.Default.Edit, contentDescription = "Editar Plano")
                }
            }
        }
    ) {
        Column(modifier = Modifier.padding(it).padding(16.dp)) {
            if (goal != null) {
                Text(text = "Descrição: ${goal.description ?: "N/A"}")
                Spacer(modifier = Modifier.height(16.dp))

                if (goal.dailyTasks.isEmpty()) {

                } else {
                    Button(onClick = { showHelpDialog = true }) {
                        Text("Preciso de Ajuda")
                    }
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
                                    Checkbox(
                                        checked = task.isCompleted,
                                        onCheckedChange = {
                                            viewModel.toggleTaskCompletion(goal, dailyTask, task)
                                        }
                                    )
                                    Text(text = task.description)
                                }
                            }
                        }
                    }
                }

            } else {
                CircularProgressIndicator()
            }
        }
    }
}
