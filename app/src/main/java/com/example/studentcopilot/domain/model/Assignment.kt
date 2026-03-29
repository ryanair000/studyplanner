package com.example.studentcopilot.domain.model

data class Assignment(
    val id: Long = 0,
    val title: String,
    val courseId: Long,
    val courseName: String = "",
    val dueDate: Long,
    val completed: Boolean = false
)
