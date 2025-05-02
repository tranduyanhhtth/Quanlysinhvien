package com.example.bai6

import java.io.Serializable

data class Student(
    val name: String,
    val mssv: String,
    val email: String,
    val phone: String
) : Serializable
