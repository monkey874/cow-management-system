package com.example.cowmanagementsystem

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.RadioGroup
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.mutableStateMapOf
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class makeUser : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_make_user)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        makeAccount()
    }

    fun makeAccount(){
        val userName = findViewById<TextView>(R.id.editTextDate6)
        val password = findViewById<TextView>(R.id.editTextDate4)
        val makeUser = findViewById<Button>(R.id.button)

        val statusGroup = findViewById<RadioGroup>(R.id.genderGroup)
        val value = statusGroup.checkedRadioButtonId

        val dbHelper = DatabaseHelper(this)
        makeUser.setOnClickListener {

        val data = mutableStateMapOf(
            "userName" to userName.text.toString(),
            "password" to password.text.toString(),
            "roleId" to "null",
            "active" to value.toString(),
            )
        dbHelper.voegNaamToe(data, "users")
        }





    }
}