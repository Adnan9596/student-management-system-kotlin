package com.studentmanagement.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "students")
data class Student(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val rollNumber: String,
    val department: String,
    val semester: Int,
    val phoneNumber: String,
    val email: String,
    val marks: Double,
    val attendance: Double
) {
    val performanceStatus: String
        get() = when {
            marks >= 85 -> "Excellent"
            marks >= 70 -> "Good"
            marks >= 50 -> "Average"
            else -> "Needs Improvement"
        }
}
