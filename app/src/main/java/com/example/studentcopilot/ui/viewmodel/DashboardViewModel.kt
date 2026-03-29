package com.example.studentcopilot.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studentcopilot.data.repository.AppRepository
import com.example.studentcopilot.domain.model.Assignment
import com.example.studentcopilot.domain.model.Exam
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class DashboardViewModel(private val repository: AppRepository) : ViewModel() {

    private val _studyPlanLoading = MutableStateFlow(false)
    val studyPlanLoading: StateFlow<Boolean> = _studyPlanLoading

    private val _studyPlanText = MutableStateFlow("")
    val studyPlanText: StateFlow<String> = _studyPlanText

    private val _studyPlanError = MutableStateFlow("")
    val studyPlanError: StateFlow<String> = _studyPlanError

    private val upcomingDays: Long = TimeUnit.DAYS.toMillis(14)
    private val now: Long get() = System.currentTimeMillis()
    private val futureDate: Long get() = now + upcomingDays

    val upcomingAssignments: StateFlow<List<Assignment>> = repository
        .getAssignmentsBetweenDates(now, futureDate)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    val upcomingExams: StateFlow<List<Exam>> = repository
        .getExamsBetweenDates(now, futureDate)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    val nearestDeadline: StateFlow<String> = combine(
        upcomingAssignments,
        upcomingExams
    ) { assignments, exams ->
        val allDeadlines = mutableListOf<Pair<String, Long>>()
        assignments.forEach { a ->
            allDeadlines.add("${a.title} (${a.courseName})" to a.dueDate)
        }
        exams.forEach { e ->
            allDeadlines.add("${e.title} (${e.courseName})" to e.examDate)
        }
        if (allDeadlines.isEmpty()) {
            "No upcoming deadlines"
        } else {
            val nearest = allDeadlines.minByOrNull { it.second }
            nearest?.first ?: "No upcoming deadlines"
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), "No upcoming deadlines")

    val summaryStats: StateFlow<String> = combine(
        upcomingAssignments,
        upcomingExams
    ) { assignments, exams ->
        val assignmentCount = assignments.size
        val examCount = exams.size
        "$assignmentCount assignments • $examCount exams due soon"
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), "0 assignments • 0 exams")

    fun generateStudyPlan() {
        viewModelScope.launch {
            _studyPlanLoading.value = true
            _studyPlanError.value = ""
            _studyPlanText.value = ""

            try {
                val assignments = upcomingAssignments.value
                val exams = upcomingExams.value

                if (assignments.isEmpty() && exams.isEmpty()) {
                    _studyPlanText.value = "No upcoming assignments or exams to study for!"
                } else {
                    val plan = repository.generateStudyPlan(assignments, exams)
                    _studyPlanText.value = plan
                }
            } catch (e: Exception) {
                _studyPlanError.value = "Failed to generate study plan: ${e.message}"
            } finally {
                _studyPlanLoading.value = false
            }
        }
    }

    fun clearStudyPlan() {
        _studyPlanText.value = ""
        _studyPlanError.value = ""
    }
}
