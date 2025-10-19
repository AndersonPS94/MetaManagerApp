package com.desafiodevspace.metamanager.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.desafiodevspace.metamanager.data.model.DailyTask
import com.desafiodevspace.metamanager.data.model.Goal
import com.desafiodevspace.metamanager.data.model.Task
import com.desafiodevspace.metamanager.presentation.viewmodel.GoalViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoalDetailScreen(
    navController: NavController? = null, // opcional para preview
    goalId: String? = null,
    viewModel: GoalViewModel = hiltViewModel()
) {
    val goals by viewModel.goals.collectAsState()
    val goal = goals.find { it.id == goalId }
    val helpSuggestion by viewModel.helpSuggestion.collectAsState()

    var showHelpDialog by remember { mutableStateOf(false) }
    var userSituation by remember { mutableStateOf("") }

    // Dialog de ajuda
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
                TextButton(onClick = {
                    goal?.let { viewModel.getHelp(it, userSituation) }
                    showHelpDialog = false
                }) { Text("Pedir Ajuda") }
            },
            dismissButton = { TextButton(onClick = { showHelpDialog = false }) { Text("Cancelar") } }
        )
    }

    // Dialog de sugestão da IA
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
                title = { Text("Progresso") },
                navigationIcon = {
                    IconButton(onClick = { navController?.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
                    }
                },
                actions = {
                    goal?.let {
                        IconButton(onClick = { navController?.navigate("edit_goal/${it.id}") }) {
                            Icon(Icons.Default.Edit, contentDescription = "Editar Plano")
                        }
                        IconButton(onClick = { viewModel.shareGoal(it) }) {
                            Icon(Icons.Default.Share, contentDescription = "Compartilhar")
                        }
                    }
                }
            )
        }
    ) { padding ->
        if (goal != null) {
            val allTasks = goal.dailyTasks.flatMap { it.tasks }
            val totalTasks = allTasks.size
            val completedTasks = allTasks.count { it.isCompleted }
            val progress = if (totalTasks > 0) completedTasks.toFloat() / totalTasks.toFloat() else 0f

            LazyColumn(
                modifier = Modifier
                    .padding(padding)
                    .padding(horizontal = 16.dp)
            ) {
                item {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Box(contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(
                                progress = progress,
                                modifier = Modifier.size(140.dp),
                                strokeWidth = 10.dp
                            )
                            Text(
                                "${(progress * 100).toInt()}%",
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(goal.title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(24.dp))
                    }
                }

                items(allTasks) { task ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = task.isCompleted,
                            onCheckedChange = {
                                val dailyTask = goal.dailyTasks.find { it.tasks.contains(task) }
                                if (dailyTask != null) {
                                    viewModel.toggleTaskCompletion(goal, dailyTask, task)
                                }
                            }
                        )
                        Text(text = task.description)
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedButton(
                        onClick = { navController?.navigate("analytics") },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Ver histórico")
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        } else {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
    }
}

