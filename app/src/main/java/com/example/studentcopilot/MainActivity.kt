package com.example.studentcopilot

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import com.example.studentcopilot.data.local.database.AppDatabase
import com.example.studentcopilot.data.remote.api.ApiClient
import com.example.studentcopilot.data.repository.AppRepository
import com.example.studentcopilot.ui.navigation.AppNavigation
import com.example.studentcopilot.ui.theme.StudentCopilotTheme
import com.example.studentcopilot.ui.viewmodel.AssignmentsViewModel
import com.example.studentcopilot.ui.viewmodel.CoursesViewModel
import com.example.studentcopilot.ui.viewmodel.DashboardViewModel
import com.example.studentcopilot.ui.viewmodel.ExamsViewModel
import com.example.studentcopilot.ui.viewmodel.ViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = AppDatabase.getDatabase(this)

        val repository = AppRepository(
            courseDao = database.courseDao(),
            assignmentDao = database.assignmentDao(),
            examDao = database.examDao(),
            apiService = ApiClient.apiService
        )

        val factory = ViewModelFactory(repository)

        setContent {
            StudentCopilotTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val dashboardViewModel = ViewModelProvider(
                        this@MainActivity,
                        factory
                    )[DashboardViewModel::class.java]

                    val coursesViewModel = ViewModelProvider(
                        this@MainActivity,
                        factory
                    )[CoursesViewModel::class.java]

                    val assignmentsViewModel = ViewModelProvider(
                        this@MainActivity,
                        factory
                    )[AssignmentsViewModel::class.java]

                    val examsViewModel = ViewModelProvider(
                        this@MainActivity,
                        factory
                    )[ExamsViewModel::class.java]

                    val navController = rememberNavController()

                    AppNavigation(
                        navController = navController,
                        dashboardViewModel = dashboardViewModel,
                        coursesViewModel = coursesViewModel,
                        assignmentsViewModel = assignmentsViewModel,
                        examsViewModel = examsViewModel
                    )
                }
            }
        }
    }
}
