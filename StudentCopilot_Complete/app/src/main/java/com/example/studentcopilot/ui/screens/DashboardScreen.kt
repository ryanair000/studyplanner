package com.example.studentcopilot.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.studentcopilot.ui.components.EmptyState
import com.example.studentcopilot.ui.components.SectionDivider
import com.example.studentcopilot.ui.components.TaskCard
import com.example.studentcopilot.ui.viewmodel.DashboardViewModel
import com.example.studentcopilot.util.DateUtil

@Composable
fun DashboardScreen(viewModel: DashboardViewModel) {
    val upcomingAssignments by viewModel.upcomingAssignments.collectAsState()
    val upcomingExams by viewModel.upcomingExams.collectAsState()
    val nearestDeadline by viewModel.nearestDeadline.collectAsState()
    val summaryStats by viewModel.summaryStats.collectAsState()
    val studyPlanLoading by viewModel.studyPlanLoading.collectAsState()
    val studyPlanText by viewModel.studyPlanText.collectAsState()
    val studyPlanError by viewModel.studyPlanError.collectAsState()

    var showStudyPlan by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        // Header section
        item {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Dashboard",
                    style = MaterialTheme.typography.headlineLarge
                )

                // Summary card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Upcoming Tasks",
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = summaryStats,
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Nearest: $nearestDeadline",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }

                // Study plan button
                Button(
                    onClick = {
                        viewModel.generateStudyPlan()
                        showStudyPlan = true
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !studyPlanLoading
                ) {
                    if (studyPlanLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.padding(end = 8.dp),
                            strokeWidth = 2.dp
                        )
                    }
                    Text("Generate Study Plan")
                }
            }
        }

        // Study plan result
        if (showStudyPlan) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Your Study Plan",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Button(
                                onClick = {
                                    viewModel.clearStudyPlan()
                                    showStudyPlan = false
                                },
                                modifier = Modifier
                                    .height(32.dp)
                                    .padding(0.dp)
                            ) {
                                Text("Close", style = MaterialTheme.typography.labelSmall)
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        when {
                            studyPlanError.isNotEmpty() -> {
                                Text(
                                    text = studyPlanError,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                            studyPlanText.isNotEmpty() -> {
                                Text(
                                    text = studyPlanText,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                            studyPlanLoading -> {
                                CircularProgressIndicator(
                                    modifier = Modifier
                                        .align(Alignment.CenterHorizontally)
                                        .padding(16.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Assignments section
        if (upcomingAssignments.isNotEmpty()) {
            item {
                SectionDivider("Upcoming Assignments")
            }
            items(upcomingAssignments) { assignment ->
                TaskCard(
                    title = assignment.title,
                    courseName = assignment.courseName,
                    dueDate = DateUtil.formatDate(assignment.dueDate),
                    isOverdue = DateUtil.isOverdue(assignment.dueDate),
                    completed = assignment.completed
                )
            }
        }

        // Exams section
        if (upcomingExams.isNotEmpty()) {
            item {
                SectionDivider("Upcoming Exams")
            }
            items(upcomingExams) { exam ->
                TaskCard(
                    title = exam.title,
                    courseName = exam.courseName,
                    dueDate = DateUtil.formatDate(exam.examDate),
                    isOverdue = DateUtil.isOverdue(exam.examDate)
                )
            }
        }

        // Empty state
        if (upcomingAssignments.isEmpty() && upcomingExams.isEmpty()) {
            item {
                EmptyState("No upcoming assignments or exams.\nAdd courses to get started!")
            }
        }
    }
}
