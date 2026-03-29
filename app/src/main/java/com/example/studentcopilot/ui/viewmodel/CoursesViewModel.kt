package com.example.studentcopilot.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studentcopilot.data.repository.AppRepository
import com.example.studentcopilot.domain.model.Course
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CoursesViewModel(private val repository: AppRepository) : ViewModel() {

    val courses: StateFlow<List<Course>> = repository
        .getAllCourses()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    fun addCourse(name: String) {
        if (name.isBlank()) return
        viewModelScope.launch {
            repository.addCourse(name.trim())
        }
    }
}
