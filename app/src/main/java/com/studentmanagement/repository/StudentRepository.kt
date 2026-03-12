package com.studentmanagement.repository

import androidx.lifecycle.LiveData
import com.studentmanagement.database.StudentDao
import com.studentmanagement.model.Student

class StudentRepository(private val studentDao: StudentDao) {
    val allStudents: LiveData<List<Student>> = studentDao.getAllStudents()

    suspend fun insert(student: Student) = studentDao.insertStudent(student)
    suspend fun update(student: Student) = studentDao.updateStudent(student)
    suspend fun delete(student: Student) = studentDao.deleteStudent(student)
    fun search(query: String): LiveData<List<Student>> = studentDao.searchStudents(query)
}
