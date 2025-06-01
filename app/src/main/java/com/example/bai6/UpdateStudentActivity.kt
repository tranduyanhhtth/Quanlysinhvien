package com.example.bai6

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class UpdateStudentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_student)

        val editTextName = findViewById<EditText>(R.id.editTextName)
        val editTextMSSV = findViewById<EditText>(R.id.editTextMSSV)
        val editTextEmail = findViewById<EditText>(R.id.editTextEmail)
        val editTextPhone = findViewById<EditText>(R.id.editTextPhone)
        val buttonUpdate = findViewById<Button>(R.id.buttonUpdate)

        val student = intent.getSerializableExtra("student") as? Student
        val position = intent.getIntExtra("position", -1)

        if (student == null || position == -1) {
            Toast.makeText(this, "Lỗi: Không nhận được thông tin sinh viên", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        editTextName.setText(student.name)
        editTextMSSV.setText(student.mssv)
        editTextMSSV.isEnabled = false // Vô hiệu hóa chỉnh sửa MSSV vì đang để là khóa chính
        editTextEmail.setText(student.email)
        editTextPhone.setText(student.phone)

        buttonUpdate.setOnClickListener {
            val name = editTextName.text.toString().trim()
            val email = editTextEmail.text.toString().trim()
            val phone = editTextPhone.text.toString().trim()

            if (name.isNotEmpty() && email.isNotEmpty() && phone.isNotEmpty()) {
                val updatedStudent = Student(name, student.mssv, email, phone)
                val intent = Intent()
                intent.putExtra("student", updatedStudent)
                intent.putExtra("position", position)
                setResult(RESULT_OK, intent)
                finish()
            } else {
                Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show()
            }
        }
    }
}