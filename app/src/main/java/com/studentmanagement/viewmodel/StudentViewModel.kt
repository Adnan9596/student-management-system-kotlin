package com.studentmanagement.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.studentmanagement.database.StudentDatabase
import com.studentmanagement.model.Student
import com.studentmanagement.repository.StudentRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class StudentViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: StudentRepository
    val allStudents: LiveData<List<Student>>

    private val searchQuery = MutableLiveData("")
    val searchedStudents: LiveData<List<Student>>

    init {
        val dao = StudentDatabase.getDatabase(application).studentDao()
        repository = StudentRepository(dao)
        allStudents = repository.allStudents
        searchedStudents = searchQuery.switchMap { query ->
            if (query.isBlank()) allStudents else repository.search(query.trim())
        }
    }

    fun setSearchQuery(query: String) {
        searchQuery.value = query
    }

    fun insert(student: Student) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(student)
    }

    fun update(student: Student) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(student)
    }

    fun delete(student: Student) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(student)
    }
}
