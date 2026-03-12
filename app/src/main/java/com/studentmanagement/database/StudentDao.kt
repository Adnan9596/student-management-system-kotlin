package com.studentmanagement.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.studentmanagement.model.Student

@Dao
interface StudentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStudent(student: Student)

    @Update
    suspend fun updateStudent(student: Student)

    @Delete
    suspend fun deleteStudent(student: Student)

    @Query("SELECT * FROM students ORDER BY name COLLATE NOCASE ASC")
    fun getAllStudents(): LiveData<List<Student>>

    @Query(
        """
        SELECT * FROM students
        WHERE name LIKE '%' || :query || '%'
        OR rollNumber LIKE '%' || :query || '%'
        OR department LIKE '%' || :query || '%'
        ORDER BY name COLLATE NOCASE ASC
        """
    )
    fun searchStudents(query: String): LiveData<List<Student>>
}
