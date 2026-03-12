package com.studentmanagement.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.studentmanagement.databinding.ItemStudentBinding
import com.studentmanagement.model.Student

enum class SortOption { NAME, MARKS, ROLL_NUMBER }

class StudentAdapter(
    private val onView: (Student) -> Unit,
    private val onEdit: (Student) -> Unit,
    private val onDelete: (Student) -> Unit
) : ListAdapter<Student, StudentAdapter.StudentViewHolder>(DiffCallback) {

    object DiffCallback : DiffUtil.ItemCallback<Student>() {
        override fun areItemsTheSame(oldItem: Student, newItem: Student): Boolean = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Student, newItem: Student): Boolean = oldItem == newItem
    }

    private var currentSort = SortOption.NAME

    fun setSort(sortOption: SortOption) {
        currentSort = sortOption
        submitStudentList(currentList)
    }

    fun submitStudentList(students: List<Student>) {
        val sorted = when (currentSort) {
            SortOption.NAME -> students.sortedBy { it.name.lowercase() }
            SortOption.MARKS -> students.sortedByDescending { it.marks }
            SortOption.ROLL_NUMBER -> students.sortedBy { it.rollNumber.lowercase() }
        }
        submitList(sorted)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val binding = ItemStudentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StudentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class StudentViewHolder(private val binding: ItemStudentBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(student: Student) = with(binding) {
            tvName.text = student.name
            tvRoll.text = "Roll: ${student.rollNumber}"
            tvDepartment.text = student.department
            tvMarks.text = "Marks: ${student.marks}"
            btnView.setOnClickListener { onView(student) }
            btnEdit.setOnClickListener { onEdit(student) }
            btnDelete.setOnClickListener { onDelete(student) }
        }
    }
}
