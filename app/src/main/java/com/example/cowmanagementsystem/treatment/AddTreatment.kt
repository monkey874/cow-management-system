package com.example.cowmanagementsystem.treatment

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
import com.example.cowmanagementsystem.dasboard.ActivityDashboard

class AddTreatment : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_treatment)
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

            // all text field placeholder and groups
            val treatmentNamePlaceholder = findViewById<EditText>(R.id.editTextDate6)
            val treatmentDescriptionPlaceholder = findViewById<EditText>(R.id.editTextDate4)

            val treatmentNameText = treatmentNamePlaceholder.text.toString()
            val treatmentDescriptionText = treatmentDescriptionPlaceholder.text.toString()

            // all treatmentInfo in a mutableStateMapOf and push everything to the database
            val dbHelper = DatabaseHelper(this)
            val treatmentInfo = mutableStateMapOf(
                "treatmentName" to treatmentNameText,
                "treatmentDescription" to treatmentDescriptionText,
            )
            dbHelper.addItem(treatmentInfo, "treatments")

            val intent = Intent(this, ActivityDashboard::class.java)
            startActivity(intent)
        }

    }
}