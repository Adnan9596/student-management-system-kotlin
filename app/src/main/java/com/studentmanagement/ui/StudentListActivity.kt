package com.studentmanagement.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.google.android.material.snackbar.Snackbar
import com.studentmanagement.adapter.SortOption
import com.studentmanagement.adapter.StudentAdapter
import com.studentmanagement.databinding.ActivityStudentListBinding
import com.studentmanagement.model.Student
import com.studentmanagement.utils.Constants
import com.studentmanagement.viewmodel.StudentViewModel

class StudentListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStudentListBinding
    private val studentViewModel: StudentViewModel by viewModels()

    private val studentAdapter = StudentAdapter(
        onView = { openDetail(it) },
        onEdit = { openEdit(it) },
        onDelete = { confirmDelete(it) }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudentListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerStudents.adapter = studentAdapter

        binding.searchBar.editText?.setOnEditorActionListener { _, _, _ ->
            studentViewModel.setSearchQuery(binding.searchBar.editText?.text.toString())
            true
        }
        binding.searchBar.editText?.addTextChangedListener { text ->
            studentViewModel.setSearchQuery(text?.toString().orEmpty())
        }

        if (intent.getBooleanExtra("open_search", false)) {
            binding.searchBar.editText?.requestFocus()
        }

        binding.chipName.setOnClickListener { studentAdapter.setSort(SortOption.NAME) }
        binding.chipMarks.setOnClickListener { studentAdapter.setSort(SortOption.MARKS) }
        binding.chipRoll.setOnClickListener { studentAdapter.setSort(SortOption.ROLL_NUMBER) }

        studentViewModel.searchedStudents.observe(this) { students ->
            studentAdapter.submitStudentList(students)
        }
    }

    private fun openDetail(student: Student) {
        startActivity(Intent(this, StudentDetailActivity::class.java).apply { putStudentExtras(student) })
    }

    private fun openEdit(student: Student) {
        startActivity(Intent(this, EditStudentActivity::class.java).apply { putStudentExtras(student) })
    }

    private fun confirmDelete(student: Student) {
        AlertDialog.Builder(this)
            .setTitle("Delete Student")
            .setMessage("Are you sure you want to delete ${student.name}?")
            .setPositiveButton("Delete") { _, _ ->
                studentViewModel.delete(student)
                Snackbar.make(binding.root, "Student deleted", Snackbar.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun Intent.putStudentExtras(student: Student) {
        putExtra(Constants.EXTRA_STUDENT_ID, student.id)
        putExtra(Constants.EXTRA_STUDENT_NAME, student.name)
        putExtra(Constants.EXTRA_STUDENT_ROLL, student.rollNumber)
        putExtra(Constants.EXTRA_STUDENT_DEPARTMENT, student.department)
        putExtra(Constants.EXTRA_STUDENT_SEMESTER, student.semester)
        putExtra(Constants.EXTRA_STUDENT_PHONE, student.phoneNumber)
        putExtra(Constants.EXTRA_STUDENT_EMAIL, student.email)
        putExtra(Constants.EXTRA_STUDENT_MARKS, student.marks)
        putExtra(Constants.EXTRA_STUDENT_ATTENDANCE, student.attendance)
    }
}
