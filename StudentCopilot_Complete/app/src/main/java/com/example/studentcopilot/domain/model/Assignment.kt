package com.example.studentcopilot.domain.model

data class Assignment(
    val id: Long = 0,
    val title: String,
    val courseId: Long,
    val courseName: String = "", // For display convenience
    val dueDate: Long, // Unix timestamp in milliseconds
    val completed: Boolean = false
)
