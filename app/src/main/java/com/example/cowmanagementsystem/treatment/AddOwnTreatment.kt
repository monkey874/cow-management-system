package com.example.cowmanagementsystem.treatment

import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.mutableStateMapOf
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.cowmanagementsystem.database.DatabaseHelper
import com.example.cowmanagementsystem.R
import com.example.cowmanagementsystem.cows.ShowAllCows
import com.example.cowmanagementsystem.login.Login

class AddOwnTreatment : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_own_treatment)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        getAllInfo()
    }
    fun getAllInfo(){
        // all text field placeholder and groups and buttons
        val subBtn = findViewById<Button>(R.id.button)

        // all treatmentInfo in a mutableStateMapOf and push everything to the database
        subBtn.setOnClickListener {
            val treatmentInfo = mutableStateMapOf(
                "treatmentId" to "1",
                "extraTreatmentDescription" to "e",
                "cowId" to ShowAllCows.cowId.toString(),
                "userID" to Login.UserID.toString(),
                "doneOn" to ""

            )
            val dbHelper = DatabaseHelper(this)
            dbHelper.addItem(treatmentInfo, "cowTreatments")
        }
    }
}