package com.example.studentcopilot.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studentcopilot.data.repository.AppRepository
import com.example.studentcopilot.domain.model.Course
import com.example.studentcopilot.domain.model.Exam
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ExamsViewModel(private val repository: AppRepository) : ViewModel() {

    val exams: StateFlow<List<Exam>> = repository
        .getAllExams()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    val courses: StateFlow<List<Course>> = repository
        .getAllCourses()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    fun addExam(
        title: String,
        courseId: Long,
        examDate: Long
    ) {
        if (title.isBlank() || courseId == 0L) return
        viewModelScope.launch {
            repository.addExam(
                title = title.trim(),
                courseId = courseId,
                examDate = examDate
            )
        }
    }

    fun updateExam(
        id: Long,
        title: String,
        courseId: Long,
        examDate: Long
    ) {
        viewModelScope.launch {
            repository.updateExam(
                id = id,
                title = title,
                courseId = courseId,
                examDate = examDate
            )
        }
    }
}
