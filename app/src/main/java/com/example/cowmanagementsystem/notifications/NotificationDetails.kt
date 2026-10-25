package com.example.cowmanagementsystem.notifications

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.cowmanagementsystem.database.DatabaseHelper
import com.example.cowmanagementsystem.R

class NotificationDetails : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_notification_details)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        getAllInfo()
    }
    @SuppressLint("SetTextI18n")
    fun  getAllInfo(){
        val notificationId = intent.getStringExtra("notificationId")
        val dbHelper = DatabaseHelper(this)
        val notificationDataList = dbHelper.getId("notifications", notificationId?.toIntOrNull() ?: 0, "notificationId" )

        // all text field placeholder and buttons
        val notificationsDescriptionPlaceholder = findViewById<TextView>(R.id.desc)
        val notificationsTitlePlaceholder = findViewById<TextView>(R.id.title)

        val notificationsDescriptionExtra = findViewById<TextView>(R.id.textOmschrijving)
        val notificationsTitleExtra = findViewById<TextView>(R.id.titleOmschrijving)
        val deleteButton = findViewById<Button>(R.id.button1)


        // set all the values on the notificationDetails page
        if (notificationDataList.isNotEmpty()) {
        val notification = notificationDataList[0]

            notificationsTitlePlaceholder.text = ("Melding: " + notification["title"])
            notificationsDescriptionPlaceholder.text = ("De informatie van melding word hier weegegeven.")

            notificationsTitleExtra.text = (notification["title"])
            notificationsDescriptionExtra.text = (notification["description"])

        }

        deleteButton.setOnClickListener {
            delete()
            val intent = Intent(this, ShowAllNotifications::class.java)
            startActivity(intent)
        }


    }

    fun delete(){
        val cowId = intent.getStringExtra("notificationId")

        val dbHelper = DatabaseHelper(this)
        dbHelper.delete(
            "notifications",
            cowId?.toIntOrNull() ?: 0,
            "notificationId",

            )
    }
}