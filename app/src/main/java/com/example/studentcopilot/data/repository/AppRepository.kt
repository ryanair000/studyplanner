package com.example.studentcopilot.data.repository

import com.example.studentcopilot.data.local.dao.AssignmentDao
import com.example.studentcopilot.data.local.dao.CourseDao
import com.example.studentcopilot.data.local.dao.ExamDao
import com.example.studentcopilot.data.local.entity.AssignmentEntity
import com.example.studentcopilot.data.local.entity.CourseEntity
import com.example.studentcopilot.data.local.entity.ExamEntity
import com.example.studentcopilot.data.remote.api.ApiService
import com.example.studentcopilot.data.remote.dto.AssignmentDto
import com.example.studentcopilot.data.remote.dto.ExamDto
import com.example.studentcopilot.data.remote.dto.StudyPlanRequest
import com.example.studentcopilot.domain.model.Assignment
import com.example.studentcopilot.domain.model.Course
import com.example.studentcopilot.domain.model.Exam
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

class AppRepository(
    private val courseDao: CourseDao,
    private val assignmentDao: AssignmentDao,
    private val examDao: ExamDao,
    private val apiService: ApiService
) {
    suspend fun addCourse(name: String): Long {
        return courseDao.insertCourse(CourseEntity(name = name))
    }

    fun getAllCourses(): Flow<List<Course>> {
        return courseDao.getAllCourses().map { entities ->
            entities.map { Course(id = it.id, name = it.name) }
        }
    }

    suspend fun getCourseById(courseId: Long): Course? {
        return courseDao.getCourseById(courseId)?.let {
            Course(id = it.id, name = it.name)
        }
    }

    suspend fun addAssignment(
        title: String,
        courseId: Long,
        dueDate: Long,
        completed: Boolean = false
    ): Long {
        return assignmentDao.insertAssignment(
            AssignmentEntity(
                title = title,
                courseId = courseId,
                dueDate = dueDate,
                completed = completed
            )
        )
    }

    fun getAllAssignments(): Flow<List<Assignment>> {
        return combine(
            assignmentDao.getAllAssignments(),
            courseDao.getAllCourses()
        ) { assignments, courses ->
            val courseMap = courses.associateBy { it.id }
            assignments.map {
                Assignment(
                    id = it.id,
                    title = it.title,
                    courseId = it.courseId,
                    courseName = courseMap[it.courseId]?.name ?: "Unknown",
                    dueDate = it.dueDate,
                    completed = it.completed
                )
            }
        }
    }

    fun getAssignmentsBetweenDates(startDate: Long, endDate: Long): Flow<List<Assignment>> {
        return combine(
            assignmentDao.getAssignmentsBetweenDates(startDate, endDate),
            courseDao.getAllCourses()
        ) { assignments, courses ->
            val courseMap = courses.associateBy { it.id }
            assignments.map {
                Assignment(
                    id = it.id,
                    title = it.title,
                    courseId = it.courseId,
                    courseName = courseMap[it.courseId]?.name ?: "Unknown",
                    dueDate = it.dueDate,
                    completed = it.completed
                )
            }
        }
    }

    suspend fun updateAssignment(
        id: Long,
        title: String,
        courseId: Long,
        dueDate: Long,
        completed: Boolean
    ) {
        assignmentDao.updateAssignment(
            AssignmentEntity(
                id = id,
                title = title,
                courseId = courseId,
                dueDate = dueDate,
                completed = completed
            )
        )
    }

    suspend fun addExam(
        title: String,
        courseId: Long,
        examDate: Long
    ): Long {
        return examDao.insertExam(
            ExamEntity(
                title = title,
                courseId = courseId,
                examDate = examDate
            )
        )
    }

    fun getAllExams(): Flow<List<Exam>> {
        return combine(
            examDao.getAllExams(),
            courseDao.getAllCourses()
        ) { exams, courses ->
            val courseMap = courses.associateBy { it.id }
            exams.map {
                Exam(
                    id = it.id,
                    title = it.title,
                    courseId = it.courseId,
                    courseName = courseMap[it.courseId]?.name ?: "Unknown",
                    examDate = it.examDate
                )
            }
        }
    }

    fun getExamsBetweenDates(startDate: Long, endDate: Long): Flow<List<Exam>> {
        return combine(
            examDao.getExamsBetweenDates(startDate, endDate),
            courseDao.getAllCourses()
        ) { exams, courses ->
            val courseMap = courses.associateBy { it.id }
            exams.map {
                Exam(
                    id = it.id,
                    title = it.title,
                    courseId = it.courseId,
                    courseName = courseMap[it.courseId]?.name ?: "Unknown",
                    examDate = it.examDate
                )
            }
        }
    }

    suspend fun updateExam(id: Long, title: String, courseId: Long, examDate: Long) {
        examDao.updateExam(
            ExamEntity(
                id = id,
                title = title,
                courseId = courseId,
                examDate = examDate
            )
        )
    }

    suspend fun generateStudyPlan(
        upcomingAssignments: List<Assignment>,
        upcomingExams: List<Exam>
    ): String {
        return try {
            val assignmentDtos = upcomingAssignments.map {
                AssignmentDto(
                    id = it.id,
                    title = it.title,
                    courseName = it.courseName,
                    dueDate = it.dueDate
                )
            }

            val examDtos = upcomingExams.map {
                ExamDto(
                    id = it.id,
                    title = it.title,
                    courseName = it.courseName,
                    examDate = it.examDate
                )
            }

            val request = StudyPlanRequest(
                assignments = assignmentDtos,
                exams = examDtos
            )

            val response = apiService.generateStudyPlan(request)
            response.studyPlan
        } catch (e: Exception) {
            "Error generating study plan: ${e.message}"
        }
    }
}
