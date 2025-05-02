package com.example.bai6

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
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

        val student = intent.getSerializableExtra("student") as Student
        val position = intent.getIntExtra("position", -1)

        editTextName.setText(student.name)
        editTextMSSV.setText(student.mssv)
        editTextEmail.setText(student.email)
        editTextPhone.setText(student.phone)

        buttonUpdate.setOnClickListener {
            val name = editTextName.text.toString()
            val mssv = editTextMSSV.text.toString()
            val email = editTextEmail.text.toString()
            val phone = editTextPhone.text.toString()

            if (name.isNotEmpty() && mssv.isNotEmpty() && email.isNotEmpty() && phone.isNotEmpty()) {
                val updatedStudent = Student(name, mssv, email, phone)
                val intent = Intent()
                intent.putExtra("student", updatedStudent)
                intent.putExtra("position", position)
                setResult(RESULT_OK, intent)
                finish()
            }
        }
    }
}