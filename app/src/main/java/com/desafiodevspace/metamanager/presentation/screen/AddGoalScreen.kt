package com.desafiodevspace.metamanager.presentation.screen

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.desafiodevspace.metamanager.data.model.Goal
import com.desafiodevspace.metamanager.presentation.viewmodel.GoalViewModel
import com.google.firebase.Timestamp
import java.util.Calendar
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddGoalScreen(
    navController: NavController,
    viewModel: GoalViewModel = hiltViewModel()
) {
    val title = remember { mutableStateOf("") }
    val description = remember { mutableStateOf("") }
    val targetDate = remember { mutableStateOf(Date()) }
    val context = LocalContext.current

    val calendar = Calendar.getInstance()
    val datePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            calendar.set(year, month, dayOfMonth)
            targetDate.value = calendar.time
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    Scaffold(
        topBar = { TopAppBar(title = { Text("Nova Meta") }) }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = title.value,
                onValueChange = { title.value = it },
                label = { Text("Título da meta") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = description.value,
                onValueChange = { description.value = it },
                label = { Text("Descrição (opcional)") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = { datePickerDialog.show() }) {
                Text("Data alvo: ${java.text.SimpleDateFormat("dd/MM/yyyy").format(targetDate.value)}")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    val newGoal = Goal(
                        title = title.value,
                        description = description.value,
                        targetDate = Timestamp(targetDate.value)
                    )
                    viewModel.generatePlanForNewGoal(newGoal)
                    navController.navigate("generated_plan")
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Gerar plano com IA")
            }
        }
    }
}
