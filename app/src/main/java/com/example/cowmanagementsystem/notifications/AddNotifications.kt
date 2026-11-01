package com.example.cowmanagementsystem.notifications

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateMapOf
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.cowmanagementsystem.R
import com.example.cowmanagementsystem.database.DatabaseHelper
import com.example.cowmanagementsystem.functies.BaseActivity
import com.example.cowmanagementsystem.functies.ValidateInput
import com.example.cowmanagementsystem.functies.generateList
import com.example.cowmanagementsystem.login.Login
import java.time.LocalDate
import java.time.format.DateTimeFormatter


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
        prepareDropdown()

    }

    fun prepareDropdown(){
        val needValues = listOf("cowID", "cowNumber")

        val dbHelper = DatabaseHelper(this)
        val cowDataItems = dbHelper.getItems("cows", needValues)

        val cowIds = mutableListOf<String>()
        val cowNumbers = mutableListOf<String>()

        for (cowData in cowDataItems) {
            cowData["cowID"]?.let { cowIds.add(it) }
            cowData["cowNumber"]?.let { cowNumbers.add(it) }
        }

        val def = generateList(this)
        def.vulKoeienDropdown(cowNumbers)

    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun getAllInfo(){
        val defclass = ValidateInput()
        val dbHelper = DatabaseHelper(this)
        val saveButton = findViewById<Button>(R.id.button)
        saveButton.setOnClickListener {

            // all text field & simmers and a placeholder
            val nameNotificationPlaceholder = findViewById<EditText>(R.id.editTextDate6)
            val descriptionNotificationPlaceholder = findViewById<EditText>(R.id.editTextDate4)
            val spinnerPlaceholder = findViewById<Spinner>(R.id.spinnerCows)

            if (!defclass.validateField(findViewById(R.id.editTextDate6), findViewById(R.id.meldingNameError), "De naam van de melding mag niet leeg zijn")) return@setOnClickListener
            if (!defclass.validateField(findViewById(R.id.editTextDate4), findViewById(R.id.meldingOmschrijvingError), "De omschrijving van de melding mag niet leeg zijn")) return@setOnClickListener


            // all the values of the placeholders
            val nameNotificationText = nameNotificationPlaceholder.text.toString()
            val descriptionNotificationText = descriptionNotificationPlaceholder.text.toString()
            val selectedCowText = spinnerPlaceholder.selectedItem.toString()
            val cowInfo = dbHelper.getString("cows",selectedCowText,"cowNumber")
            val cowId = cowInfo[0]["cowID"].toString()

            // all notificationInfo in a mutableStateMapOf and push everything to the database
            val notificationInfo = mutableStateMapOf(
                "title" to nameNotificationText,
                "description" to descriptionNotificationText,
                "cowId" to cowId,
                "userId" to Login.UserID.toString(),
                "createdOn" to LocalDate.now().format(
                    DateTimeFormatter.ofPattern("dd-MM-yyyy")).toString(),
                "processed" to "Nee"
            )
            dbHelper.addItem(notificationInfo, "notifications")

            val intent = Intent(this, ShowAllNotifications::class.java)
            startActivity(intent)
        }

    }
}