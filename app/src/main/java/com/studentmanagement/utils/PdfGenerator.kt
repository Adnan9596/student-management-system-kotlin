package com.studentmanagement.utils

import android.content.ContentValues
import android.content.Context
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import com.studentmanagement.model.Student
import java.io.File
import java.io.FileOutputStream

object PdfGenerator {

    fun exportStudentReport(context: Context, students: List<Student>): Uri? {
        val document = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(1200, 1600, 1).create()
        val page = document.startPage(pageInfo)
        val canvas = page.canvas

        val titlePaint = Paint().apply { textSize = 36f; isFakeBoldText = true }
        val bodyPaint = Paint().apply { textSize = 24f }

        var y = 80
        canvas.drawText("Smart Student Management - Report", 40f, y.toFloat(), titlePaint)
        y += 50
        canvas.drawText("Total Students: ${students.size}", 40f, y.toFloat(), bodyPaint)
        y += 50

        canvas.drawText("Name", 40f, y.toFloat(), bodyPaint)
        canvas.drawText("Roll", 400f, y.toFloat(), bodyPaint)
        canvas.drawText("Department", 620f, y.toFloat(), bodyPaint)
        canvas.drawText("Marks", 980f, y.toFloat(), bodyPaint)
        y += 30

        students.forEach { student ->
            if (y > 1500) return@forEach
            canvas.drawText(student.name, 40f, y.toFloat(), bodyPaint)
            canvas.drawText(student.rollNumber, 400f, y.toFloat(), bodyPaint)
            canvas.drawText(student.department, 620f, y.toFloat(), bodyPaint)
            canvas.drawText(student.marks.toString(), 980f, y.toFloat(), bodyPaint)
            y += 34
        }

        document.finishPage(page)

        val fileName = "student_report_${System.currentTimeMillis()}.pdf"
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val resolver = context.contentResolver
                val values = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                    put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOCUMENTS)
                }
                val uri = resolver.insert(MediaStore.Files.getContentUri("external"), values)
                uri?.let {
                    resolver.openOutputStream(it)?.use { output -> document.writeTo(output) }
                }
                uri
            } else {
                val dir = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
                val file = File(dir, fileName)
                FileOutputStream(file).use { output -> document.writeTo(output) }
                Uri.fromFile(file)
            }
        } catch (_: Exception) {
            null
        } finally {
            document.close()
        }
    }
}
