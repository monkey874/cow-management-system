package com.example.cowmanagementsystem.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.cowmanagementsystem.database.DatabaseHelper
import com.example.cowmanagementsystem.R
import com.example.cowmanagementsystem.dasboard.ActivityDashboard
import org.mindrot.jbcrypt.BCrypt

class Login : AppCompatActivity() {
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
        getAllInfo()
    }

    fun getAllInfo() {


        val userNameInput = findViewById<EditText>(R.id.gebruikersnaam)
        val userPasswordInput = findViewById<EditText>(R.id.wachtwoord)
        val subButton = findViewById<Button>(R.id.button)

        subButton.setOnClickListener {

            val dbHelper = DatabaseHelper(this)
            val loginDataList = dbHelper.getIdLog("users",userNameInput.text.toString(), "userName")
            val login = loginDataList[0]


            val validateUser = BCrypt.checkpw(userPasswordInput.text.toString(), login["password"].toString())

            if (validateUser) {
                Log.d("Is validate:", true.toString())

                    val intent = Intent(this, ActivityDashboard::class.java)
                    startActivity(intent)

            }

            UserID = login["userId"]?.toIntOrNull() ?: 0
            Log.d("UserID", UserID.toString())
        }
    }
}