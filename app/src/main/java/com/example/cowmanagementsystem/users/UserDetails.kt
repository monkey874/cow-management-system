package com.example.cowmanagementsystem.users

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.cowmanagementsystem.database.DatabaseHelper
import com.example.cowmanagementsystem.R
import com.example.cowmanagementsystem.cows.ShowAllCows.Companion.cowId
import com.example.cowmanagementsystem.functies.BaseActivity
import kotlin.text.toIntOrNull

class UserDetails : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_user_details)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets

        }
        getAllInfo()
        setupBackButton()
    }
        @SuppressLint("SetTextI18n")
        fun getAllInfo(){

            // all userInfo in a get from the database
            val userId = intent.getStringExtra("userId")
            val dbHelper = DatabaseHelper(this)
            val userDataList = dbHelper.getId("users", userId?.toIntOrNull() ?: 0, "userId" )

            // all text field placeholder and buttons
            val userNamePlaceholder = findViewById<TextView>(R.id.gebruikersnaam)
            val rolePlaceholder = findViewById<TextView>(R.id.role)
            val statusPlaceholder = findViewById<TextView>(R.id.status)

            val userTitleExtra = findViewById<TextView>(R.id.title)
            val userDescriptionExtra = findViewById<TextView>(R.id.desc)
            val deleteBtn = findViewById<Button>(R.id.button1)
            val update = findViewById<Button>(R.id.button2)


            if (userDataList.isNotEmpty()) {
                val user = userDataList[0]


                userTitleExtra.text = ("Gebruiker: " + user["userName"])
                userDescriptionExtra.text = ("De informatie van gebruiker " + user["userName"] + " word hier weegegeven.")

                userNamePlaceholder.text = ("Gebruikersnaam: " + user["userName"])
                rolePlaceholder.text = ("Rol: " + user["roleId"])
                statusPlaceholder.text = ("Status: " + user["active"])

            }


            deleteBtn.setOnClickListener {
                delete()
                val intent = Intent(this, ShowAllUsers::class.java)
                startActivity(intent)
            }

            update.setOnClickListener {
                val intent = Intent(this, editUser::class.java)
                intent.putExtra(
                    "userId",
                    userId
                )
                startActivity(intent)
            }

        }

        fun delete(){
            val cowId = intent.getStringExtra("userId")

            val dbHelper = DatabaseHelper(this)
            dbHelper.delete(
                "users",
                cowId?.toIntOrNull() ?: 0,
                "userId",

                )
        }

}