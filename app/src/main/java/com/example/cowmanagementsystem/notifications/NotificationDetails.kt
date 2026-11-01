package com.example.cowmanagementsystem.notifications

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.cowmanagementsystem.database.DatabaseHelper
import com.example.cowmanagementsystem.R
import com.example.cowmanagementsystem.functies.BaseActivity

class NotificationDetails : BaseActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
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
        setupBackButton()
    }
    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    fun getAllInfo() {

        // Haal het melding-ID op
        val notificationId = intent.getStringExtra("notificationId")?.toIntOrNull()

        if (notificationId == null) {
            Toast.makeText(this, "Geen melding gevonden", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        val dbHelper = DatabaseHelper(this)

        // Haal de melding inclusief koeiennummer en gebruikersnaam op
        val notificationDataList = dbHelper.joinNotification(notificationId)

        // Koppel de TextViews aan de XML
        val notificationsDescriptionPlaceholder = findViewById<TextView>(R.id.desc)
        val notificationsTitlePlaceholder = findViewById<TextView>(R.id.title)
        val notificationsDescriptionExtra = findViewById<TextView>(R.id.textOmschrijving)
        val notificationsTitleExtra = findViewById<TextView>(R.id.titleOmschrijving)
        val cowConnection = findViewById<TextView>(R.id.cowConnetion)
        val user = findViewById<TextView>(R.id.user)
        val dataOfBurt = findViewById<TextView>(R.id.aangemaaktOp)
        val processed = findViewById<TextView>(R.id.proccesd)
        val deleteButton = findViewById<Button>(R.id.button1)

        // Controleer of de melding bestaat
        if (notificationDataList.isNotEmpty()) {

            val notification = notificationDataList[0]

            // Haal alle gegevens op
            val title = notification["title"] ?: ""
            val description = notification["description"] ?: ""
            val cowNumber = notification["cowNumber"] ?: "Onbekende koe"
            val userName = notification["userName"] ?: "Onbekende gebruiker"
            val createdOn = notification["createdOn"] ?: ""
            val processedStatus = notification["processed"] ?: ""

            // Vul de pagina
            notificationsTitlePlaceholder.text = "Melding: $title"

            notificationsDescriptionPlaceholder.text = "De informatie van deze melding wordt hier weergegeven."

            notificationsTitleExtra.text = "Titel: $title"
            notificationsDescriptionExtra.text = "Omschrijving: $description"

            cowConnection.text = "Gekoppeld met koe: $cowNumber"
            user.text = "Gemaakt door gebruiker: $userName"

            dataOfBurt.text = "Aangemaakt op: $createdOn"
            processed.text = "Verwerkt: $processedStatus"

        } else {
            finish()
            return
        }

        // Verwijder de melding
        deleteButton.setOnClickListener {

            dbHelper.delete(
                "notifications",
                notificationId,
                "notificationId"
            )


            val intent = Intent(this, ShowAllNotifications::class.java)
            startActivity(intent)
            finish()
        }
    }

}