package com.desafiodevspace.metamanager.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.desafiodevspace.metamanager.presentation.viewmodel.GoalViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GeneratedPlanScreen(
    navController: NavController? = null, // opcional para preview
    viewModel: GoalViewModel = hiltViewModel()
) {
    val generatedPlan by viewModel.generatedPlan.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

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
                            navController?.popBackStack("goal_list", inclusive = false)
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Aceitar plano")
                    }
                }
            }
        }
    }
}

