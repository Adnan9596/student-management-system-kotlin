package com.studentmanagement.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.studentmanagement.databinding.ActivityStatisticsBinding
import com.studentmanagement.viewmodel.StudentViewModel

class StatisticsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStatisticsBinding
    private val studentViewModel: StudentViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStatisticsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        studentViewModel.allStudents.observe(this) { students ->
            val marksList = students.map { it.marks }
            val avg = marksList.average().takeIf { !it.isNaN() } ?: 0.0
            val highest = marksList.maxOrNull() ?: 0.0
            val lowest = marksList.minOrNull() ?: 0.0
            val topName = students.maxByOrNull { it.marks }?.name ?: "N/A"

            binding.tvTotal.text = students.size.toString()
            binding.tvAverage.text = "%.2f".format(avg)
            binding.tvHighest.text = highest.toString()
            binding.tvLowest.text = lowest.toString()
            binding.tvTopPerformer.text = topName
        }
    }
}
