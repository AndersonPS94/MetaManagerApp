package com.desafiodevspace.metamanager.presentation.screen

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.desafiodevspace.metamanager.data.model.Goal
import com.desafiodevspace.metamanager.presentation.viewmodel.GoalViewModel
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoalListScreen(
    navController: NavController,
    viewModel: GoalViewModel = hiltViewModel()
) {
    val goals by viewModel.goals.collectAsState()
    var goalToDelete by remember { mutableStateOf<Goal?>(null) }

    if (goalToDelete != null) {
        AlertDialog(
            onDismissRequest = { goalToDelete = null },
            title = { Text("Excluir Meta") },
            text = { Text("Tem certeza que deseja excluir a meta \"${goalToDelete!!.title}\"? Esta ação não pode ser desfeita.") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.deleteGoal(goalToDelete!!)
                    goalToDelete = null
                }) { Text("Excluir") }
            },
            dismissButton = { TextButton(onClick = { goalToDelete = null }) { Text("Cancelar") } }
        )
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Suas Metas") }) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("add_goal") },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Adicionar Meta")
            }
        }
    ) { padding ->
        if (goals.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("Nenhuma meta ainda. Toque em '+' para começar!")
            }
        } else {
            LazyColumn(
                contentPadding = padding,
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                items(goals, key = { it.id }) { goal ->
                    GoalCard(
                        goal = goal,
                        onClick = { navController.navigate("goal_detail/${goal.id}") },
                        onEdit = { navController.navigate("edit_goal/${goal.id}") },
                        onDelete = { goalToDelete = goal }
                    )
                }
            }
        }
    }
}

@Composable
fun GoalCard(
    goal: Goal,
    onClick: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    val totalTasks = goal.dailyTasks.flatMap { it.tasks }.size
    val completedTasks = goal.dailyTasks.flatMap { it.tasks }.count { it.isCompleted }
    val progress = if (totalTasks > 0) completedTasks.toFloat() / totalTasks.toFloat() else 0f
    val animatedProgress by animateFloatAsState(targetValue = progress, label = "listProgress")

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.clickable(onClick = onClick)) {
            Row(
                modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(
                        progress = { animatedProgress },
                        modifier = Modifier.size(56.dp),
                        strokeWidth = 4.dp,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                    Text(
                        text = "${(animatedProgress * 100).toInt()}%",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = goal.title,
                        style = MaterialTheme.typography.titleLarge,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = "Prazo: ${SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(goal.targetDate.toDate())}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth().padding(end = 8.dp, bottom = 4.dp),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(onClick = onEdit) {
                    Icon(Icons.Default.Edit, contentDescription = "Editar Meta", tint = MaterialTheme.colorScheme.secondary)
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = "Excluir Meta", tint = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}
