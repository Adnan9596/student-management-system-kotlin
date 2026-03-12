package com.studentmanagement.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.studentmanagement.databinding.ActivityEditStudentBinding
import com.studentmanagement.model.Student
import com.studentmanagement.utils.Constants
import com.studentmanagement.viewmodel.StudentViewModel

class EditStudentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditStudentBinding
    private val studentViewModel: StudentViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditStudentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.etName.setText(intent.getStringExtra(Constants.EXTRA_STUDENT_NAME))
        binding.etRoll.setText(intent.getStringExtra(Constants.EXTRA_STUDENT_ROLL))
        binding.etDepartment.setText(intent.getStringExtra(Constants.EXTRA_STUDENT_DEPARTMENT))
        binding.etSemester.setText(intent.getIntExtra(Constants.EXTRA_STUDENT_SEMESTER, 1).toString())
        binding.etPhone.setText(intent.getStringExtra(Constants.EXTRA_STUDENT_PHONE))
        binding.etEmail.setText(intent.getStringExtra(Constants.EXTRA_STUDENT_EMAIL))
        binding.etMarks.setText(intent.getDoubleExtra(Constants.EXTRA_STUDENT_MARKS, 0.0).toString())
        binding.etAttendance.setText(intent.getDoubleExtra(Constants.EXTRA_STUDENT_ATTENDANCE, 0.0).toString())

        binding.btnUpdate.setOnClickListener {
            val student = collectUpdatedStudent() ?: return@setOnClickListener
            studentViewModel.update(student)
            Toast.makeText(this, "Student updated", Toast.LENGTH_SHORT).show()
            Snackbar.make(binding.root, "Record updated", Snackbar.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun collectUpdatedStudent(): Student? = with(binding) {
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
            id = intent.getIntExtra(Constants.EXTRA_STUDENT_ID, 0),
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
