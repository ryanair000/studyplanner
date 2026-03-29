package com.example.studentcopilot.util

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object DateUtil {
    private val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    private val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    private val dateTimeFormat = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())

    fun formatDate(timestamp: Long): String {
        return try {
            dateFormat.format(Date(timestamp))
        } catch (e: Exception) {
            "Invalid date"
        }
    }

    fun formatDateTime(timestamp: Long): String {
        return try {
            dateTimeFormat.format(Date(timestamp))
        } catch (e: Exception) {
            "Invalid date"
        }
    }

    fun formatTime(timestamp: Long): String {
        return try {
            timeFormat.format(Date(timestamp))
        } catch (e: Exception) {
            "Invalid time"
        }
    }

    fun getDaysUntil(timestamp: Long): Int {
        val now = Calendar.getInstance()
        val diffMs = timestamp - now.timeInMillis
        return (diffMs / (1000 * 60 * 60 * 24)).toInt()
    }

    fun isOverdue(timestamp: Long): Boolean {
        return System.currentTimeMillis() > timestamp
    }

    fun isTodayOrTomorrow(timestamp: Long): Boolean {
        val today = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        val tomorrow = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_MONTH, 1)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        return timestamp >= today.timeInMillis && timestamp < tomorrow.timeInMillis.plus(86400000)
    }
}
