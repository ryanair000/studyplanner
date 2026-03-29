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
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.studentcopilot.ui.components.AddButton
import com.example.studentcopilot.ui.components.DialogHeader
import com.example.studentcopilot.ui.components.EmptyState
import com.example.studentcopilot.ui.components.TaskCard
import com.example.studentcopilot.ui.viewmodel.ExamsViewModel
import com.example.studentcopilot.util.DateUtil
import java.util.Calendar

@Composable
fun ExamsScreen(viewModel: ExamsViewModel) {
    val exams by viewModel.exams.collectAsState()
    val courses by viewModel.courses.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        if (exams.isEmpty()) {
            EmptyState("No exams yet.\nTap + to add an exam!")
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                item {
                    Text(
                        text = "Exams",
                        style = MaterialTheme.typography.headlineLarge,
                        modifier = Modifier.padding(16.dp)
                    )
                }
                items(exams) { exam ->
                    TaskCard(
                        title = exam.title,
                        courseName = exam.courseName,
                        dueDate = DateUtil.formatDate(exam.examDate),
                        isOverdue = DateUtil.isOverdue(exam.examDate),
                        onClick = {}
                    )
                }
            }
        }

        AddButton(
            onClick = { showAddDialog = true }
        )
    }

    if (showAddDialog) {
        AddExamDialog(
            courses = courses,
            onAdd = { title, courseId, examDate ->
                viewModel.addExam(title, courseId, examDate)
                showAddDialog = false
            },
            onDismiss = { showAddDialog = false }
        )
    }
}

@Composable
fun AddExamDialog(
    courses: List<com.example.studentcopilot.domain.model.Course>,
    onAdd: (String, Long, Long) -> Unit,
    onDismiss: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var selectedCourseId by remember { mutableStateOf(0L) }
    var selectedExamDate by remember { mutableStateOf(System.currentTimeMillis() + 604800000) }
    var showDatePicker by remember { mutableStateOf(false) }
    var coursesDropdownExpanded by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(16.dp),
            shape = MaterialTheme.shapes.large,
            color = MaterialTheme.colorScheme.surface
        ) {
            Column {
                DialogHeader("Add Exam", onDismiss)

                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    TextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Exam Title") },
                        placeholder = { Text("e.g., Midterm Exam") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    Box(modifier = Modifier.fillMaxWidth()) {
                        TextField(
                            value = courses.find { it.id == selectedCourseId }?.name ?: "Select Course",
                            onValueChange = {},
                            label = { Text("Course") },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = false,
                            readOnly = true
                        )
                        DropdownMenu(
                            expanded = coursesDropdownExpanded,
                            onDismissRequest = { coursesDropdownExpanded = false },
                            modifier = Modifier.fillMaxWidth(0.9f)
                        ) {
                            courses.forEach { course ->
                                DropdownMenuItem(
                                    text = { Text(course.name) },
                                    onClick = {
                                        selectedCourseId = course.id
                                        coursesDropdownExpanded = false
                                    }
                                )
                            }
                        }
                        Button(
                            onClick = { coursesDropdownExpanded = true },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                                .align(Alignment.Center),
                            shape = MaterialTheme.shapes.extraSmall
                        ) {}
                    }

                    TextField(
                        value = DateUtil.formatDate(selectedExamDate),
                        onValueChange = {},
                        label = { Text("Exam Date") },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = false,
                        readOnly = true
                    )
                    Button(
                        onClick = { showDatePicker = true },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Pick Date")
                    }

                    if (showDatePicker) {
                        SimpleDatePickerDialog(
                            initialDate = selectedExamDate,
                            onDateSelected = {
                                selectedExamDate = it
                                showDatePicker = false
                            },
                            onDismiss = { showDatePicker = false }
                        )
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End)
                    ) {
                        Button(onClick = onDismiss) {
                            Text("Cancel")
                        }
                        Button(
                            onClick = {
                                if (title.isNotBlank() && selectedCourseId != 0L) {
                                    onAdd(title, selectedCourseId, selectedExamDate)
                                }
                            },
                            enabled = title.isNotBlank() && selectedCourseId != 0L
                        ) {
                            Text("Add")
                        }
                    }
                }
            }
        }
    }
}
