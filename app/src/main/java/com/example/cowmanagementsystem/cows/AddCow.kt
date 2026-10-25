package com.example.cowmanagementsystem.cows

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.mutableStateMapOf
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.cowmanagementsystem.functies.BaseActivity
import com.example.cowmanagementsystem.database.DatabaseHelper
import com.example.cowmanagementsystem.R

class AddCow : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_cow)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        getAllInfo()
        setupBackButton()

    }

    fun getAllInfo() {

        val saveButton = findViewById<Button>(R.id.button)
        saveButton.setOnClickListener {

            // all text field placeholder and groups
            val cowNumberPlaceholder = findViewById<EditText>(R.id.editTextDate6)
            val earTagNumberPlaceholder = findViewById<EditText>(R.id.editTextDate4)
            val datePlaceholder = findViewById<EditText>(R.id.editTextDate3)

            val genderGroup = findViewById<RadioGroup>(R.id.genderGroup)
            val colorGroup = findViewById<RadioGroup>(R.id.colorGroup)

            // all the values of the placeholders and groups
            val cowNumberText = cowNumberPlaceholder.text.toString()
            val earTagNumberText = earTagNumberPlaceholder.text.toString()
            val datePlaceholderText = datePlaceholder.text.toString()

            val checkedGender = genderGroup.checkedRadioButtonId
            val checkedColor = colorGroup.checkedRadioButtonId

            val valueGenderText = findViewById<RadioButton>(checkedGender).text.toString()
            val valueColorText = findViewById<RadioButton>(checkedColor).text.toString()

            // all cowInfo in a mutableStateMapOf and push everything to the database
            val dbHelper = DatabaseHelper(this)
            val cowInfo = mutableStateMapOf(
                "cowNumber" to cowNumberText,
                "earTagNumber" to earTagNumberText,
                "dayOfBirth" to datePlaceholderText,
                "gender" to valueGenderText,
                "color" to valueColorText,
            )
            dbHelper.addItem(cowInfo, "cows")

            val intent = Intent(this, ShowAllCows::class.java)
            startActivity(intent)
        }

    }


}