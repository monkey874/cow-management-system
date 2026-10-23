package com.example.cowmanagementsystem

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class userDetails : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_user_details)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets

        }
        test()
    }
        fun  test(){
            val cowId = intent.getStringExtra("userId")

            val dbHelper = DatabaseHelper(this)

            val info = dbHelper.getId("users", cowId?.toIntOrNull() ?: 0, "userId" )

            val userName = findViewById<TextView>(R.id.gebruikersnaam)
            val role = findViewById<TextView>(R.id.role)
            val status = findViewById<TextView>(R.id.status)

            val title = findViewById<TextView>(R.id.title)
            val desc = findViewById<TextView>(R.id.desc)

            Log.d("123", "$info")

            val cow = info[0]
            if (info.isNotEmpty()) {


                title.text = "Gebruiker: ${cow["userName"]}"
                desc.text = "De informatie van gebruiker ${cow["userName"]} word hier weegegeven."

                userName.text = cow["userName"]
                role.text = cow["roleId"]
                status.text = cow["active"]


            }

            val btn = findViewById<Button>(R.id.button1)
            btn.setOnClickListener {
                delehte()
                val intent = Intent(
                    this,
                    showAllUsers::class.java
                )


                startActivity(intent)
            }


        }

        fun delehte(){
            val cowId = intent.getStringExtra("userId")

            val dbHelper = DatabaseHelper(this)
            dbHelper.delethe(
                "users",
                cowId?.toIntOrNull() ?: 0,
                "userId",

                )
        }

}