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
import com.example.studentcopilot.ui.viewmodel.AssignmentsViewModel
import com.example.studentcopilot.util.DateUtil
import java.util.Calendar

@Composable
fun AssignmentsScreen(viewModel: AssignmentsViewModel) {
    val assignments by viewModel.assignments.collectAsState()
    val courses by viewModel.courses.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        // Assignments list
        if (assignments.isEmpty()) {
            EmptyState("No assignments yet.\nTap + to add an assignment!")
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                item {
                    Text(
                        text = "Assignments",
                        style = MaterialTheme.typography.headlineLarge,
                        modifier = Modifier.padding(16.dp)
                    )
                }
                items(assignments) { assignment ->
                    TaskCard(
                        title = assignment.title,
                        courseName = assignment.courseName,
                        dueDate = DateUtil.formatDate(assignment.dueDate),
                        isOverdue = DateUtil.isOverdue(assignment.dueDate),
                        completed = assignment.completed,
                        onClick = {
                            // Could navigate to edit screen here
                        }
                    )
                }
            }
        }

        // FAB
        AddButton(
            onClick = { showAddDialog = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        )
    }

    // Add assignment dialog
    if (showAddDialog) {
        AddAssignmentDialog(
            courses = courses,
            onAdd = { title, courseId, dueDate ->
                viewModel.addAssignment(title, courseId, dueDate)
                showAddDialog = false
            },
            onDismiss = { showAddDialog = false }
        )
    }
}

@Composable
fun AddAssignmentDialog(
    courses: List<com.example.studentcopilot.domain.model.Course>,
    onAdd: (String, Long, Long) -> Unit,
    onDismiss: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var selectedCourseId by remember { mutableStateOf(0L) }
    var selectedDueDate by remember { mutableStateOf(System.currentTimeMillis() + 86400000) } // Tomorrow by default
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
                DialogHeader("Add Assignment", onDismiss)

                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    TextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Assignment Title") },
                        placeholder = { Text("e.g., Chapter 5 Exercises") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    // Course dropdown
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
                        // Invisible button to open dropdown
                        Button(
                            onClick = { coursesDropdownExpanded = true },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                                .align(Alignment.Center),
                            shape = MaterialTheme.shapes.extraSmall
                        ) {}
                    }

                    // Due date picker
                    TextField(
                        value = DateUtil.formatDate(selectedDueDate),
                        onValueChange = {},
                        label = { Text("Due Date") },
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
                        // Simple date picker using calendar
                        SimpleDatePickerDialog(
                            initialDate = selectedDueDate,
                            onDateSelected = {
                                selectedDueDate = it
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
                                    onAdd(title, selectedCourseId, selectedDueDate)
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

@Composable
fun SimpleDatePickerDialog(
    initialDate: Long,
    onDateSelected: (Long) -> Unit,
    onDismiss: () -> Unit
) {
    val calendar = Calendar.getInstance().apply { timeInMillis = initialDate }
    var dayOfMonth by remember { mutableStateOf(calendar.get(Calendar.DAY_OF_MONTH)) }
    var month by remember { mutableStateOf(calendar.get(Calendar.MONTH)) }
    var year by remember { mutableStateOf(calendar.get(Calendar.YEAR)) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .padding(16.dp),
            shape = MaterialTheme.shapes.large,
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Select Due Date", style = MaterialTheme.typography.headlineSmall)

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    DateInputField(
                        label = "Day",
                        value = dayOfMonth,
                        onValueChange = { dayOfMonth = it },
                        modifier = Modifier.weight(1f)
                    )
                    DateInputField(
                        label = "Month",
                        value = month + 1,
                        onValueChange = { month = it - 1 },
                        modifier = Modifier.weight(1f)
                    )
                    DateInputField(
                        label = "Year",
                        value = year,
                        onValueChange = { year = it },
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

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
                            val newCalendar = Calendar.getInstance().apply {
                                set(year, month, dayOfMonth)
                            }
                            onDateSelected(newCalendar.timeInMillis)
                        }
                    ) {
                        Text("OK")
                    }
                }
            }
        }
    }
}

@Composable
fun DateInputField(
    label: String,
    value: Int,
    onValueChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = value.toString(),
        onValueChange = { newValue ->
            newValue.toIntOrNull()?.let { onValueChange(it) }
        },
        label = { Text(label) },
        modifier = modifier,
        singleLine = true
    )
}
