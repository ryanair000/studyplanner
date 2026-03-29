package com.example.studentcopilot.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.studentcopilot.data.local.entity.ExamEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ExamDao {
    @Insert
    suspend fun insertExam(exam: ExamEntity): Long

    @Update
    suspend fun updateExam(exam: ExamEntity)

    @Delete
    suspend fun deleteExam(exam: ExamEntity)

    @Query("SELECT * FROM exams ORDER BY examDate ASC")
    fun getAllExams(): Flow<List<ExamEntity>>

    @Query("SELECT * FROM exams WHERE examDate BETWEEN :startDate AND :endDate ORDER BY examDate ASC")
    fun getExamsBetweenDates(startDate: Long, endDate: Long): Flow<List<ExamEntity>>

    @Query("SELECT * FROM exams WHERE id = :examId")
    suspend fun getExamById(examId: Long): ExamEntity?

    @Query("SELECT * FROM exams WHERE courseId = :courseId ORDER BY examDate ASC")
    fun getExamsByCourse(courseId: Long): Flow<List<ExamEntity>>
}
