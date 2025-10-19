package com.desafiodevspace.metamanager.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.desafiodevspace.metamanager.data.model.Goal
import com.desafiodevspace.metamanager.presentation.viewmodel.GoalViewModel
import com.google.firebase.Timestamp
import java.net.URLDecoder
import java.nio.charset.StandardCharsets
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GeneratedPlanScreen(
    navController: NavController,
    viewModel: GoalViewModel = hiltViewModel(),
    title: String?,
    description: String?,
    targetDateMillis: Long?
) {
    val generatedPlan by viewModel.generatedPlan.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    // Inicia a geração do plano assim que a tela é carregada
    LaunchedEffect(key1 = Unit) {
        if (title != null && targetDateMillis != null) {
            val decodedTitle = URLDecoder.decode(title, StandardCharsets.UTF_8.toString())
            val decodedDescription = description?.let { URLDecoder.decode(it, StandardCharsets.UTF_8.toString()) }

            val goal = Goal(
                title = decodedTitle,
                description = decodedDescription ?: "",
                targetDate = Timestamp(Date(targetDateMillis))
            )
            viewModel.generatePlanForNewGoal(goal)
        }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Seu plano diário") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (isLoading) {
                CircularProgressIndicator()
            } else {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .fillMaxWidth()
                ) {
                    Text(text = generatedPlan ?: "Nenhum plano gerado.")
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    OutlinedButton(
                        onClick = { viewModel.regeneratePlan() },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Regerar com IA")
                    }
                    Button(
                        onClick = {
                            viewModel.saveGeneratedPlan()
                            navController.navigate("goal_list") {
                                popUpTo("goal_list") { inclusive = true }
                            }
                        },
                        modifier = Modifier.weight(1f),
                        enabled = !isLoading && generatedPlan?.startsWith("Erro:") == false
                    ) {
                        Text("Aceitar plano")
                    }
                }
            }
        }
    }
}
