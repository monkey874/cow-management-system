package com.example.cowmanagementsystem

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.mutableStateMapOf
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import org.mindrot.jbcrypt.BCrypt

class login : AppCompatActivity() {
    companion object{
        var UserID: Int = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        test()
    }

    fun test(){
        val userNameInput = findViewById<EditText>(R.id.gebruikersnaam)
        val userPasswordInput = findViewById<EditText>(R.id.wachtwoord)
        val subButton = findViewById<Button>(R.id.button)

        subButton.setOnClickListener {
            val dbHelper = DatabaseHelper(this)
            val info = dbHelper.getIdLog("users", userNameInput.text.toString(), "userName")
            val firstobject = info[0]

            val validateUser = BCrypt.checkpw(userPasswordInput.text.toString(),firstobject["password"].toString())
            if (validateUser){
                Log.d("Is validate:", validateUser.toString())
            }
            UserID = firstobject["userId"]?.toIntOrNull() ?: 0
            Log.d("UserID", UserID.toString())
        }
    }
}