package com.studentmanagement.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.studentmanagement.databinding.ActivityMainBinding
import com.studentmanagement.model.Student
import com.studentmanagement.utils.Constants
import com.studentmanagement.utils.PdfGenerator
import com.studentmanagement.viewmodel.StudentViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val studentViewModel: StudentViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val prefs = getSharedPreferences(Constants.PREFS, MODE_PRIVATE)
        val isDarkMode = prefs.getBoolean(Constants.KEY_DARK_MODE, false)
        binding.themeSwitch.isChecked = isDarkMode

        setupDashboardActions()
        observeQuickStats()

        binding.themeSwitch.setOnCheckedChangeListener { _, checked ->
            prefs.edit().putBoolean(Constants.KEY_DARK_MODE, checked).apply()
            AppCompatDelegate.setDefaultNightMode(
                if (checked) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
            )
            Snackbar.make(binding.root, "Theme updated", Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun setupDashboardActions() = with(binding) {
        cardAddStudent.setOnClickListener { startActivity(Intent(this@MainActivity, AddStudentActivity::class.java)) }
        cardViewStudents.setOnClickListener { startActivity(Intent(this@MainActivity, StudentListActivity::class.java)) }
        cardSearchStudent.setOnClickListener {
            startActivity(Intent(this@MainActivity, StudentListActivity::class.java).putExtra("open_search", true))
        }
        cardStatistics.setOnClickListener { startActivity(Intent(this@MainActivity, StatisticsActivity::class.java)) }
        cardExportData.setOnClickListener {
            val observer = object : Observer<List<Student>> {
                override fun onChanged(students: List<Student>) {
                    val uri = PdfGenerator.exportStudentReport(this@MainActivity, students)
                    val message = if (uri != null) "PDF exported successfully" else "Export failed"
                    Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
                    studentViewModel.allStudents.removeObserver(this)
                }
            }
            studentViewModel.allStudents.observe(this@MainActivity, observer)
        }
    }

    private fun observeQuickStats() {
        studentViewModel.allStudents.observe(this) { students ->
            val total = students.size
            val average = students.map { it.marks }.average().takeIf { !it.isNaN() } ?: 0.0
            val topPerformer = students.maxByOrNull { it.marks }?.name ?: "N/A"

            binding.tvTotalStudents.text = total.toString()
            binding.tvAverageMarks.text = "%.2f".format(average)
            binding.tvTopPerformer.text = topPerformer
        }
    }
}
