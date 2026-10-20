package com.example.cowmanagementsystem.ui.theme

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.cowmanagementsystem.R
import android.widget.EditText
import android.widget.Button
import android.widget.RadioGroup
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import com.example.cowmanagementsystem.DatabaseHelper

class addCow : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_cow)
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
            val cowNumber = findViewById<EditText>(R.id.editTextDate6)
            val earTagNumber = findViewById<EditText>(R.id.editTextDate4)
            val date = findViewById<EditText>(R.id.editTextDate3)


            val gender = findViewById<RadioGroup>(R.id.genderGroup)
            val color = findViewById<RadioGroup>(R.id.colorGroup)

            val genderId = gender.checkedRadioButtonId
            val colorId = color.checkedRadioButtonId

            val dbHelper = DatabaseHelper(this)
            val data = mutableStateMapOf(
                "cowNumber" to cowNumber.text.toString(),
                "earTagNumber" to earTagNumber.text.toString(),
                "dayOfBirth" to genderId.toString(),
                "gender" to date.text.toString(),
                "color" to colorId.toString(),

            )
            dbHelper.voegNaamToe(data, "cows")
        }

    }



    }
