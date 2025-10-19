package com.desafiodevspace.metamanager.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.desafiodevspace.metamanager.presentation.components.CircularProgressIndicatorWithText
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
    val isLoading by viewModel.isLoading.collectAsState()

    var showHelpDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var userSituation by remember { mutableStateOf("") }

    // Efeito para abrir o diálogo de sugestão quando ela chegar
    LaunchedEffect(helpSuggestion) {
        if (helpSuggestion != null) {
            showHelpDialog = true
        }
    }

    if (showHelpDialog) {
        AlertDialog(
            onDismissRequest = {
                showHelpDialog = false
                viewModel.clearHelpSuggestion()
            },
            title = { Text(if (helpSuggestion == null) "Precisa de Ajuda?" else "Sugestão da IA") },
            text = {
                Column {
                    if (isLoading) {
                        CircularProgressIndicator(modifier = Modifier.padding(16.dp).align(Alignment.CenterHorizontally))
                    } else {
                        if (helpSuggestion == null) {
                            OutlinedTextField(
                                value = userSituation,
                                onValueChange = { userSituation = it },
                                label = { Text("Descreva sua situação (ex: estou atrasado)") },
                                modifier = Modifier.fillMaxWidth()
                            )
                        } else {
                            Text(helpSuggestion!!)
                        }
                    }
                }
            },
            confirmButton = {
                if (helpSuggestion == null) {
                    TextButton(onClick = {
                        goal?.let { viewModel.getHelp(it, userSituation) }
                    }) { Text("Pedir Ajuda") }
                } else {
                    TextButton(onClick = {
                        goal?.let { viewModel.acceptHelpSuggestion(it, helpSuggestion!!) }
                        showHelpDialog = false
                    }) { Text("Aceitar") }
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showHelpDialog = false
                    viewModel.clearHelpSuggestion()
                }) { Text("Cancelar") }
            }
        )
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Excluir Meta") },
            text = { Text("Tem certeza que deseja excluir esta meta? A ação não pode ser desfeita.") },
            confirmButton = {
                TextButton(onClick = {
                    goal?.let { viewModel.deleteGoal(it) }
                    showDeleteDialog = false
                    navController.popBackStack()
                }) { Text("Excluir") }
            },
            dismissButton = { TextButton(onClick = { showDeleteDialog = false }) { Text("Cancelar") } }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Progresso da Meta") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                },
                actions = {
                    goal?.let {
                        IconButton(onClick = { navController.navigate("edit_goal/${it.id}") }) {
                            Icon(Icons.Default.Edit, contentDescription = "Editar Meta")
                        }
                        IconButton(onClick = { showDeleteDialog = true }) {
                            Icon(Icons.Default.Delete, contentDescription = "Excluir Meta")
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
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(16.dp)
            ) {
                 item {
                        Column(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CircularProgressIndicatorWithText(
                                progress = progress,
                                text = "${(progress * 100).toInt()}%"
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = goal.title,
                                style = MaterialTheme.typography.headlineSmall,
                                textAlign = TextAlign.Center
                            )
                             if (goal.description.isNotBlank()) {
                                Text(
                                    text = goal.description,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                            }
                        }
                    }

                    item { HorizontalDivider() }

                    items(goal.dailyTasks, key = { it.day }) { dailyTask ->
                        Column(modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)) {
                            Text(
                                text = "Dia ${dailyTask.day}",
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            dailyTask.tasks.forEach { task ->
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Checkbox(
                                        checked = task.isCompleted,
                                        onCheckedChange = {
                                            viewModel.toggleTaskCompletion(goal, dailyTask, task)
                                        }
                                    )
                                    Text(text = task.description, style = MaterialTheme.typography.bodyLarge)
                                }
                            }
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(24.dp))
                        Button(
                            onClick = { showHelpDialog = true },
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Text("Preciso de ajuda")
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedButton(
                            onClick = { navController.navigate("analytics/${goal.id}") },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Ver histórico e Analytics")
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }
            }
        } else if (!isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Meta não encontrada ou sendo carregada...")
            }
        }
    }
}
