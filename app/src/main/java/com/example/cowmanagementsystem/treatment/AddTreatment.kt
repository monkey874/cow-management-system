package com.example.cowmanagementsystem.treatment

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateMapOf
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.cowmanagementsystem.functies.BaseActivity
import com.example.cowmanagementsystem.database.DatabaseHelper
import com.example.cowmanagementsystem.R
import com.example.cowmanagementsystem.dasboard.ActivityDashboard
import com.example.cowmanagementsystem.functies.ValidateInput

class AddTreatment : BaseActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
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

    @RequiresApi(Build.VERSION_CODES.O)
    fun getAllInfo(){
        val saveButton = findViewById<Button>(R.id.button)
        val defClass = ValidateInput()
        saveButton.setOnClickListener {

            // all text field placeholder and groups
            val treatmentNamePlaceholder = findViewById<EditText>(R.id.editTextDate6)
            val treatmentDescriptionPlaceholder = findViewById<EditText>(R.id.editTextDate4)

            if (!defClass.validateField(findViewById(R.id.editTextDate6), findViewById(R.id.nameError), "De naam van de behandeling mag niet leeg zijn")) return@setOnClickListener
            if (!defClass.validateField(findViewById(R.id.editTextDate4), findViewById(R.id.omschrijvingError), "De omschrijving van de behandeling mag niet leeg zijn")) return@setOnClickListener

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