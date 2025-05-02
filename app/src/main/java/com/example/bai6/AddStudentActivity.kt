package com.example.bai6

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class AddStudentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_student)

        val editTextName = findViewById<EditText>(R.id.editTextName)
        val editTextMSSV = findViewById<EditText>(R.id.editTextMSSV)
        val editTextEmail = findViewById<EditText>(R.id.editTextEmail)
        val editTextPhone = findViewById<EditText>(R.id.editTextPhone)
        val buttonSave = findViewById<Button>(R.id.buttonSave)

        buttonSave.setOnClickListener {
            val name = editTextName.text.toString()
            val mssv = editTextMSSV.text.toString()
            val email = editTextEmail.text.toString()
            val phone = editTextPhone.text.toString()

            if (name.isNotEmpty() && mssv.isNotEmpty() && email.isNotEmpty() && phone.isNotEmpty()) {
                val student = Student(name, mssv, email, phone)
                val intent = Intent()
                intent.putExtra("student", student)
                setResult(RESULT_OK, intent)
                finish()
            }
        }
    }
}