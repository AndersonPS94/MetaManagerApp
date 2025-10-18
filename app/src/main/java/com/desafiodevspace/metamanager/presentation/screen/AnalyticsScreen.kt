package com.desafiodevspace.metamanager.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.desafiodevspace.metamanager.presentation.viewmodel.AnalyticsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalyticsScreen(
    viewModel: AnalyticsViewModel = hiltViewModel()
) {
    val percentageOfCompletedGoals by viewModel.percentageOfCompletedGoals.collectAsState()
    val totalTasks by viewModel.totalTasks.collectAsState()
    val completedTasks by viewModel.completedTasks.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Analytics de Progresso") }) }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Metas Concluídas", fontSize = 24.sp)
            CircularProgressIndicator(progress = percentageOfCompletedGoals)
            Text("${(percentageOfCompletedGoals * 100).toInt()}%", fontSize = 20.sp)

            Spacer(modifier = Modifier.height(32.dp))

            Text("Tarefas Concluídas", fontSize = 24.sp)
            Text("$completedTasks / $totalTasks", fontSize = 20.sp)
        }
    }
}
