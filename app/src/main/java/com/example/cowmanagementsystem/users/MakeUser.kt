package com.example.cowmanagementsystem.users

import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.mutableStateMapOf
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.cowmanagementsystem.functies.BaseActivity
import com.example.cowmanagementsystem.database.DatabaseHelper
import com.example.cowmanagementsystem.R

class MakeUser : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_make_user)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        getAllInfo()
        setupBackButton()
    }

    fun getAllInfo(){
        val makeButton = findViewById<Button>(R.id.button)
        makeButton.setOnClickListener {

            // all text field placeholder and groups
            val userNamePlaceholder = findViewById<TextView>(R.id.editTextDate6)
            val passwordPlaceholder = findViewById<TextView>(R.id.editTextDate4)
            val statusGroup = findViewById<RadioGroup>(R.id.genderGroup)

            val userNameText = userNamePlaceholder.text.toString()
            val passwordText = passwordPlaceholder.text.toString()

            val status = statusGroup.checkedRadioButtonId
            val valueStaus = findViewById<RadioButton>(status).text.toString()

            // all cowInfo in a mutableStateMapOf and push everything to the database
            val dbHelper = DatabaseHelper(this)
            val data = mutableStateMapOf(
                "userName" to userNameText,
                "password" to passwordText,
                "roleId" to "null",
                "active" to valueStaus,
            )
            dbHelper.addItem(data, "users")
        }
    }
}