package com.studentmanagement.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.studentmanagement.databinding.ActivityAddStudentBinding
import com.studentmanagement.model.Student
import com.studentmanagement.viewmodel.StudentViewModel

class AddStudentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddStudentBinding
    private val studentViewModel: StudentViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStudentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSave.setOnClickListener {
            val student = collectStudent() ?: return@setOnClickListener
            studentViewModel.insert(student)
            Toast.makeText(this, "Student added", Toast.LENGTH_SHORT).show()
            Snackbar.make(binding.root, "Record saved in database", Snackbar.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun collectStudent(): Student? = with(binding) {
        val name = etName.text.toString().trim()
        val roll = etRoll.text.toString().trim()
        val department = etDepartment.text.toString().trim()
        val semester = etSemester.text.toString().trim().toIntOrNull()
        val phone = etPhone.text.toString().trim()
        val email = etEmail.text.toString().trim()
        val marks = etMarks.text.toString().trim().toDoubleOrNull()
        val attendance = etAttendance.text.toString().trim().toDoubleOrNull()

        if (name.isEmpty() || roll.isEmpty() || department.isEmpty() || semester == null ||
            phone.isEmpty() || email.isEmpty() || marks == null || attendance == null
        ) {
            Snackbar.make(binding.root, "Please fill all fields correctly", Snackbar.LENGTH_SHORT).show()
            return null
        }

        Student(
            name = name,
            rollNumber = roll,
            department = department,
            semester = semester,
            phoneNumber = phone,
            email = email,
            marks = marks,
            attendance = attendance
        )
    }
}
