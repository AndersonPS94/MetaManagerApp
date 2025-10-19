package com.desafiodevspace.metamanager.presentation.screen

import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.desafiodevspace.metamanager.presentation.viewmodel.GoalViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoalDetailScreen(
    navController: NavController? = null,
    goalId: String? = null,
    viewModel: GoalViewModel = hiltViewModel()
) {
    val goals by viewModel.goals.collectAsState()
    val goal = goals.find { it.id == goalId }
    val helpSuggestion by viewModel.helpSuggestion.collectAsState()

    var showHelpDialog by remember { mutableStateOf(false) }
    var userSituation by remember { mutableStateOf("") }

    if (showHelpDialog) {
        // ... (o código do diálogo permanece o mesmo)
    }

    if (helpSuggestion != null) {
        // ... (o código do diálogo permanece o mesmo)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Progresso da Meta") },
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
            val animatedProgress by animateFloatAsState(targetValue = progress, label = "progressAnimation")

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 16.dp)
            ) {
                item {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(
                                progress = animatedProgress,
                                modifier = Modifier.size(180.dp),
                                strokeWidth = 12.dp,
                                strokeCap = StrokeCap.Round,
                                trackColor = MaterialTheme.colorScheme.surfaceVariant
                            )
                            Text(
                                text = "${(animatedProgress * 100).toInt()}%",
                                style = MaterialTheme.typography.headlineLarge,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
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

                item { Divider() }

                items(goal.dailyTasks) { dailyTask ->
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
                        onClick = { navController?.navigate("analytics/${goal.id}") },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Ver histórico e Analytics")
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
