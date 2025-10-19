package com.desafiodevspace.metamanager.presentation.screen

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.desafiodevspace.metamanager.data.model.DailyTask
import com.desafiodevspace.metamanager.presentation.viewmodel.GoalViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalyticsScreen(
    goalId: String?,
    navController: NavController? = null,
    viewModel: GoalViewModel = hiltViewModel()
) {
    val goals by viewModel.goals.collectAsState()
    val goal = goals.find { it.id == goalId }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Histórico e Analytics") },
                navigationIcon = {
                    IconButton(onClick = { navController?.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
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
            val consecutiveDays = viewModel.calculateConsecutiveDays(goal)

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    Text(
                        goal.title,
                        style = MaterialTheme.typography.headlineSmall,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(24.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        StatCard(
                            label = "Progresso",
                            value = "${(progress * 100).toInt()}%",
                            modifier = Modifier.weight(1f)
                        )
                        StatCard(
                            label = "Dias Seguidos",
                            value = consecutiveDays.toString(),
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                    Divider()
                }

                item {
                    Text(
                        "Histórico Diário",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(vertical = 16.dp).fillMaxWidth()
                    )
                }

                items(goal.dailyTasks, key = { it.day }) { dailyTask ->
                    DailyTaskHistoryCard(dailyTask)
                }
            }
        } else {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Selecione uma meta para ver os detalhes.")
            }
        }
    }
}

@Composable
private fun StatCard(label: String, value: String, modifier: Modifier = Modifier) {
    Card(modifier = modifier) {
        Column(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = value, style = MaterialTheme.typography.headlineMedium)
            Text(text = label, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
private fun DailyTaskHistoryCard(dailyTask: DailyTask) {
    val tasksInDay = dailyTask.tasks.size
    val completedInDay = dailyTask.tasks.count { it.isCompleted }
    val progress = if (tasksInDay > 0) completedInDay.toFloat() / tasksInDay.toFloat() else 0f
    val animatedProgress by animateFloatAsState(targetValue = progress, label = "historyProgress")

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Dia ${dailyTask.day}", style = MaterialTheme.typography.titleMedium)
                Text("$completedInDay/$tasksInDay", style = MaterialTheme.typography.bodyMedium)
            }
            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = animatedProgress,
                modifier = Modifier.fillMaxWidth().height(8.dp)
            )
        }
    }
}
