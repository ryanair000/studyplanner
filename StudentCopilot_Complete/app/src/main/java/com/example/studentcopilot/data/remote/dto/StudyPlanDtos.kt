package com.example.studentcopilot.data.remote.dto

import com.google.gson.annotations.SerializedName

// Request DTO sent to the AI study plan endpoint
data class StudyPlanRequest(
    @SerializedName("assignments")
    val assignments: List<AssignmentDto>,
    @SerializedName("exams")
    val exams: List<ExamDto>
)

data class AssignmentDto(
    @SerializedName("id")
    val id: Long,
    @SerializedName("title")
    val title: String,
    @SerializedName("course_name")
    val courseName: String,
    @SerializedName("due_date")
    val dueDate: Long
)

data class ExamDto(
    @SerializedName("id")
    val id: Long,
    @SerializedName("title")
    val title: String,
    @SerializedName("course_name")
    val courseName: String,
    @SerializedName("exam_date")
    val examDate: Long
)

// Response DTO received from the AI study plan endpoint
data class StudyPlanResponse(
    @SerializedName("study_plan")
    val studyPlan: String,
    @SerializedName("status")
    val status: String = "success"
)
