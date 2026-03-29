package com.example.studentcopilot.ui.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.School
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.studentcopilot.ui.screens.AssignmentsScreen
import com.example.studentcopilot.ui.screens.CoursesScreen
import com.example.studentcopilot.ui.screens.DashboardScreen
import com.example.studentcopilot.ui.screens.ExamsScreen
import com.example.studentcopilot.ui.viewmodel.AssignmentsViewModel
import com.example.studentcopilot.ui.viewmodel.CoursesViewModel
import com.example.studentcopilot.ui.viewmodel.DashboardViewModel
import com.example.studentcopilot.ui.viewmodel.ExamsViewModel

sealed class Screen(val route: String, val label: String) {
    object Dashboard : Screen("dashboard", "Dashboard")
    object Courses : Screen("courses", "Courses")
    object Assignments : Screen("assignments", "Assignments")
    object Exams : Screen("exams", "Exams")
}

@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController(),
    dashboardViewModel: DashboardViewModel,
    coursesViewModel: CoursesViewModel,
    assignmentsViewModel: AssignmentsViewModel,
    examsViewModel: ExamsViewModel
) {
    Column(modifier = Modifier.fillMaxSize()) {
        NavHost(
            navController = navController,
            startDestination = Screen.Dashboard.route,
            modifier = Modifier.weight(1f)
        ) {
            composable(Screen.Dashboard.route) {
                DashboardScreen(dashboardViewModel)
            }
            composable(Screen.Courses.route) {
                CoursesScreen(coursesViewModel)
            }
            composable(Screen.Assignments.route) {
                AssignmentsScreen(assignmentsViewModel)
            }
            composable(Screen.Exams.route) {
                ExamsScreen(examsViewModel)
            }
        }

        BottomNavigationBar(navController)
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val screens = listOf(
        Screen.Dashboard,
        Screen.Courses,
        Screen.Assignments,
        Screen.Exams
    )

    NavigationBar {
        screens.forEach { screen ->
            val isSelected = navController.currentDestination?.route == screen.route

            NavigationBarItem(
                icon = {
                    when (screen) {
                        Screen.Dashboard -> Icon(
                            if (isSelected) Icons.Filled.Home else Icons.Outlined.Home,
                            contentDescription = screen.label
                        )
                        Screen.Courses -> Icon(
                            if (isSelected) Icons.Filled.School else Icons.Outlined.School,
                            contentDescription = screen.label
                        )
                        Screen.Assignments -> Icon(
                            if (isSelected) Icons.Filled.CheckCircle else Icons.Outlined.CheckCircle,
                            contentDescription = screen.label
                        )
                        Screen.Exams -> Icon(
                            if (isSelected) Icons.Filled.CheckCircle else Icons.Outlined.CheckCircle,
                            contentDescription = screen.label
                        )
                    }
                },
                label = { Text(screen.label) },
                selected = isSelected,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}
