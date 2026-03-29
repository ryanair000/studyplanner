package com.example.studentcopilot.domain.model

data class Exam(
    val id: Long = 0,
    val title: String,
    val courseId: Long,
    val courseName: String = "",
    val examDate: Long
)
