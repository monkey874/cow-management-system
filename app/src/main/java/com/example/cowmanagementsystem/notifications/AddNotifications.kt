package com.example.cowmanagementsystem.notifications

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.mutableStateMapOf
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.cowmanagementsystem.functies.BaseActivity
import com.example.cowmanagementsystem.database.DatabaseHelper
import com.example.cowmanagementsystem.R
import com.example.cowmanagementsystem.cows.ShowAllCows
import com.example.cowmanagementsystem.login.Login

class AddNotifications : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_notifications)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        getAllInfo()
        setupBackButton()

    }

    fun getAllInfo(){

        val saveButton = findViewById<Button>(R.id.button)
        saveButton.setOnClickListener {

            // all text field placeholder
            val nameNotificationPlaceholder = findViewById<EditText>(R.id.editTextDate6)
            val descriptionNotificationPlaceholder = findViewById<EditText>(R.id.editTextDate4)

            // all the values of the placeholders
            val nameNotificationText = nameNotificationPlaceholder.text.toString()
            val descriptionNotificationText = descriptionNotificationPlaceholder.text.toString()

            // all notificationInfo in a mutableStateMapOf and push everything to the database
            val dbHelper = DatabaseHelper(this)
            val notificationInfo = mutableStateMapOf(
                "title" to nameNotificationText,
                "description" to descriptionNotificationText,
                "cowId" to "1",
                "userId" to Login.UserID.toString(),
                "createdOn" to "1",
                "processed" to "0"
            )
            dbHelper.addItem(notificationInfo, "notifications")

            val intent = Intent(this, ShowAllNotifications::class.java)
            startActivity(intent)
        }

    }
}