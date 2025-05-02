package com.example.bai6
import android.content.Intent
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

class MainActivity : AppCompatActivity() {

    private lateinit var listViewStudents: ListView
    private lateinit var studentAdapter: StudentAdapter
    private val students = ArrayList<Student>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        try {
            listViewStudents = findViewById(R.id.listViewStudents)
            studentAdapter = StudentAdapter(this, students) { student, position ->
                val intent = Intent(this, UpdateStudentActivity::class.java)
                intent.putExtra("student", student)
                intent.putExtra("position", position)
                startActivityForResult(intent, REQUEST_CODE_UPDATE)
            }
            listViewStudents.adapter = studentAdapter

//            // Thêm dữ liệu mẫu để kiểm tra
//            if (students.isEmpty()) {
//                students.add(Student("Nguyen Van A", "SV001", "a@example.com", "0901234567"))
//                studentAdapter.notifyDataSetChanged()
//            }

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
            Toast.makeText(this, "Lỗi: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
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
                        students.add(student)
                        studentAdapter.notifyDataSetChanged()
                    }
                }
                REQUEST_CODE_UPDATE -> {
                    val student = data?.getSerializableExtra("student") as? Student
                    val position = data?.getIntExtra("position", -1) ?: -1
                    if (student != null && position != -1) {
                        students[position] = student
                        studentAdapter.notifyDataSetChanged()
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

    companion object {
        const val REQUEST_CODE_ADD = 1
        const val REQUEST_CODE_UPDATE = 2
        const val REQUEST_CODE_CALL_PERMISSION = 100
    }
}