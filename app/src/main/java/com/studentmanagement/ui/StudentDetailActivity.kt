package com.studentmanagement.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.studentmanagement.databinding.ActivityStudentDetailBinding
import com.studentmanagement.model.Student
import com.studentmanagement.utils.Constants

class StudentDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStudentDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudentDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val student = Student(
            id = intent.getIntExtra(Constants.EXTRA_STUDENT_ID, 0),
            name = intent.getStringExtra(Constants.EXTRA_STUDENT_NAME).orEmpty(),
            rollNumber = intent.getStringExtra(Constants.EXTRA_STUDENT_ROLL).orEmpty(),
            department = intent.getStringExtra(Constants.EXTRA_STUDENT_DEPARTMENT).orEmpty(),
            semester = intent.getIntExtra(Constants.EXTRA_STUDENT_SEMESTER, 0),
            phoneNumber = intent.getStringExtra(Constants.EXTRA_STUDENT_PHONE).orEmpty(),
            email = intent.getStringExtra(Constants.EXTRA_STUDENT_EMAIL).orEmpty(),
            marks = intent.getDoubleExtra(Constants.EXTRA_STUDENT_MARKS, 0.0),
            attendance = intent.getDoubleExtra(Constants.EXTRA_STUDENT_ATTENDANCE, 0.0)
        )

        with(binding) {
            tvName.text = student.name
            tvRoll.text = "Roll Number: ${student.rollNumber}"
            tvDepartment.text = "Department: ${student.department}"
            tvSemester.text = "Semester: ${student.semester}"
            tvEmail.text = "Email: ${student.email}"
            tvPhone.text = "Phone: ${student.phoneNumber}"
            tvMarks.text = "Marks: ${student.marks}"
            tvAttendance.text = "Attendance: ${student.attendance}%"
            tvPerformance.text = "Performance: ${student.performanceStatus}"
        }
    }
}
