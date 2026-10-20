package com.example.cowmanagementsystem

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.mutableStateMapOf
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.time.LocalDate

class add_notifications : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_notifications)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        GetAllInfo()
    }

    fun GetAllInfo(){


        val saveButton = findViewById<Button>(R.id.button)

        saveButton.setOnClickListener {
            val nameNotifications = findViewById<EditText>(R.id.editTextDate6)
            val desNotifications = findViewById<EditText>(R.id.editTextDate4)
            
            val dbHelper = DatabaseHelper(this)
            val data = mutableStateMapOf(
                "title" to nameNotifications.text.toString(),
                "description" to desNotifications.text.toString(),
                "cowId" to "1",
                "userId" to "1",
                "createdOn" to "1",
                "processed" to "0"
              

                )
            dbHelper.voegNaamToe(data, "notifications")
        }

    }
}