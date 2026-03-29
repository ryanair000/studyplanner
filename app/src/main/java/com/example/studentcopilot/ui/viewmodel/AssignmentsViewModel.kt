package com.example.studentcopilot.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studentcopilot.data.repository.AppRepository
import com.example.studentcopilot.domain.model.Assignment
import com.example.studentcopilot.domain.model.Course
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AssignmentsViewModel(private val repository: AppRepository) : ViewModel() {

    val assignments: StateFlow<List<Assignment>> = repository
        .getAllAssignments()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    val courses: StateFlow<List<Course>> = repository
        .getAllCourses()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    fun addAssignment(
        title: String,
        courseId: Long,
        dueDate: Long,
        completed: Boolean = false
    ) {
        if (title.isBlank() || courseId == 0L) return
        viewModelScope.launch {
            repository.addAssignment(
                title = title.trim(),
                courseId = courseId,
                dueDate = dueDate,
                completed = completed
            )
        }
    }

    fun updateAssignment(
        id: Long,
        title: String,
        courseId: Long,
        dueDate: Long,
        completed: Boolean
    ) {
        viewModelScope.launch {
            repository.updateAssignment(
                id = id,
                title = title,
                courseId = courseId,
                dueDate = dueDate,
                completed = completed
            )
        }
    }
}
