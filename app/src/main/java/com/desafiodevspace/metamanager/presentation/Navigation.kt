package com.desafiodevspace.metamanager.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.desafiodevspace.metamanager.presentation.screen.AddGoalScreen
import com.desafiodevspace.metamanager.presentation.screen.AnalyticsScreen
import com.desafiodevspace.metamanager.presentation.screen.EditGoalScreen
import com.desafiodevspace.metamanager.presentation.screen.GeneratedPlanScreen
import com.desafiodevspace.metamanager.presentation.screen.GoalDetailScreen
import com.desafiodevspace.metamanager.presentation.screen.GoalListScreen

@Composable
fun Navigation(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(navController, startDestination = "goal_list", modifier = modifier) {
        composable("goal_list") {
            GoalListScreen(navController = navController)
        }
        composable("add_goal") {
            AddGoalScreen(navController = navController)
        }
        composable("generated_plan") {
            GeneratedPlanScreen(navController = navController)
        }
        composable(
            route = "goal_detail/{goalId}",
            arguments = listOf(navArgument("goalId") { type = NavType.StringType })
        ) {
            GoalDetailScreen(
                navController = navController,
                goalId = it.arguments?.getString("goalId")
            )
        }
        composable(
            route = "edit_goal/{goalId}",
            arguments = listOf(navArgument("goalId") { type = NavType.StringType })
        ) {
            EditGoalScreen(
                navController = navController,
                goalId = it.arguments?.getString("goalId")
            )
        }
        composable(
            route = "analytics/{goalId}",
            arguments = listOf(navArgument("goalId") { type = NavType.StringType })
        ) {
            AnalyticsScreen(
                goalId = it.arguments?.getString("goalId"),
                navController = navController
            )
        }
    }
}
