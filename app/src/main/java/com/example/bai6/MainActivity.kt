package com.example.bai6

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MainActivity : AppCompatActivity() {

    private lateinit var listViewStudents: ListView
    private lateinit var studentAdapter: StudentAdapter
    private val students = ArrayList<Student>()
//    private lateinit var sharedPreferences: SharedPreferences
//    private val PREFS_NAME = "StudentPrefs"
//    private val KEY_STUDENTS = "students"
    private lateinit var db: StudentDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        db = StudentDatabase(this)
        loadStudents()

        try {
            listViewStudents = findViewById(R.id.listViewStudents)
            studentAdapter = StudentAdapter(this, students) { student, position ->
                val intent = Intent(this, UpdateStudentActivity::class.java)
                intent.putExtra("student", student)
                intent.putExtra("position", position)
                startActivityForResult(intent, REQUEST_CODE_UPDATE)
            }
            listViewStudents.adapter = studentAdapter

            // Đăng ký context menu cho ListView
            registerForContextMenu(listViewStudents)

            // Kiểm tra quyền CALL_PHONE
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.CALL_PHONE),
                    REQUEST_CODE_CALL_PERMISSION
                )
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Lỗi khởi tạo: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        try {
            menuInflater.inflate(R.menu.menu_main, menu)
            Toast.makeText(this, "Menu được nạp", Toast.LENGTH_SHORT).show()
            return true
        } catch (e: Exception) {
            Toast.makeText(this, "Lỗi nạp menu: ${e.message}", Toast.LENGTH_LONG).show()
            return false
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_add_student -> {
                startActivityForResult(Intent(this, AddStudentActivity::class.java), REQUEST_CODE_ADD)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateContextMenu(
        menu: ContextMenu,
        v: View,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater.inflate(R.menu.menu_context_student, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val info = item.menuInfo as AdapterView.AdapterContextMenuInfo
        return studentAdapter.handleContextMenu(item.itemId, info.position)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_ADD -> {
                    val student = data?.getSerializableExtra("student") as? Student
                    if (student != null) {
//                        students.add(student)
//                        saveStudents()
                        db.addStudent(student)
                        loadStudents()
                        studentAdapter.notifyDataSetChanged()
                        Toast.makeText(this, "Thêm sinh viên thành công", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Lỗi thêm sinh viên", Toast.LENGTH_SHORT).show()
                    }
                }
                REQUEST_CODE_UPDATE -> {
                    val student = data?.getSerializableExtra("student") as? Student
                    val position = data?.getIntExtra("position", -1) ?: -1
                    if (student != null && position != -1) {
//                        students[position] = student
//                        saveStudents()
//                        studentAdapter.notifyDataSetChanged()
                        db.updateStudent(student)
                        loadStudents()
                        studentAdapter.notifyDataSetChanged()
                        Toast.makeText(this, "Cập nhật sinh viên thành công", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Lỗi cập nhật sinh viên", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_CALL_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(
                    this,
                    "Cần quyền gọi điện để sử dụng chức năng này",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

//    private fun saveStudents() {
//        val gson = Gson()
//        val json = gson.toJson(students)
//        sharedPreferences.edit().putString(KEY_STUDENTS, json).apply()
//    }

//    private fun loadStudents() {
//        val gson = Gson()
//        val json = sharedPreferences.getString(KEY_STUDENTS, null)
//        if (json != null) {
//            val type = object : TypeToken<ArrayList<Student>>() {}.type
//            val savedStudents = gson.fromJson<ArrayList<Student>>(json, type)
//            students.clear()
//            students.addAll(savedStudents)
//        }
//    }

    private fun loadStudents() {
        students.clear()
        students.addAll(db.getAllStudents())
//        studentAdapter.notifyDataSetChanged()
    }

    companion object {
        const val REQUEST_CODE_ADD = 1
        const val REQUEST_CODE_UPDATE = 2
        const val REQUEST_CODE_CALL_PERMISSION = 100
    }
}