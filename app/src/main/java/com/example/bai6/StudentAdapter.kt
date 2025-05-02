package com.example.bai6

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.appcompat.app.AlertDialog

class StudentAdapter(
    context: Context,
    private val students: ArrayList<Student>,
    private val onUpdate: (Student, Int) -> Unit
) : ArrayAdapter<Student>(context, 0, students) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.item_student, parent, false)

        val student = getItem(position)
        if (student != null) {
            view.findViewById<TextView>(R.id.textViewName).text = student.name
            view.findViewById<TextView>(R.id.textViewMSSV).text = student.mssv
        }

        return view
    }

    fun handleContextMenu(itemId: Int, position: Int): Boolean {
        val student = getItem(position) ?: return false
        when (itemId) {
            R.id.action_update -> {
                onUpdate(student, position)
                return true
            }
            R.id.action_delete -> {
                AlertDialog.Builder(context)
                    .setTitle("Xác nhận")
                    .setMessage("Bạn có chắc muốn xóa ${student.name}?")
                    .setPositiveButton("Xóa") { _, _ ->
                        students.removeAt(position)
                        notifyDataSetChanged()
                    }
                    .setNegativeButton("Hủy", null)
                    .show()
                return true
            }
            R.id.action_call -> {
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:${student.phone}")
                context.startActivity(intent)
                return true
            }
            R.id.action_email -> {
                val intent = Intent(Intent.ACTION_SENDTO)
                intent.data = Uri.parse("mailto:${student.email}")
                context.startActivity(intent)
                return true
            }
            else -> return false
        }
    }
}