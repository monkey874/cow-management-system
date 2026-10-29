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
        val cowConnection = findViewById<TextView>(R.id.cowConnetion)
        val user = findViewById<TextView>(R.id.user)
        val dataOfBuld = findViewById<TextView>(R.id.aangemaaktOp)
        val processed = findViewById<TextView>(R.id.proccesd)

        val deleteButton = findViewById<Button>(R.id.button1)


        // set all the values on the notificationDetails page
        if (notificationDataList.isNotEmpty()) {
        val notification = notificationDataList[0]

            notificationsTitlePlaceholder.text = ("Melding: " + notification["title"])
            notificationsDescriptionPlaceholder.text = ("De informatie van melding word hier weegegeven.")

            notificationsTitleExtra.text = ("Titel: " + notification["title"])
            notificationsDescriptionExtra.text = ("Omschrijving: " +notification["description"])
            cowConnection.text = ("Gekoppeld met koe: " + notification["cowId"])
            user.text = ("Gemaakt door gebruiker: " + notification["userId"])
            dataOfBuld.text = ("Aagemaakt op: " + notification["createdOn"])
            processed.text = ("verwerkt: " + notification["processed"])

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