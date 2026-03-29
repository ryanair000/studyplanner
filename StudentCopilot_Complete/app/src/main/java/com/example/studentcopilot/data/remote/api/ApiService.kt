package com.example.studentcopilot.data.remote.api

import com.example.studentcopilot.data.remote.dto.StudyPlanRequest
import com.example.studentcopilot.data.remote.dto.StudyPlanResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("/api/study-plan")
    suspend fun generateStudyPlan(@Body request: StudyPlanRequest): StudyPlanResponse
}
