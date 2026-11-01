package com.example.cowmanagementsystem.users

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.cowmanagementsystem.database.DatabaseHelper
import com.example.cowmanagementsystem.R
import com.example.cowmanagementsystem.functies.BaseActivity
import kotlin.text.toIntOrNull

class UserDetails : BaseActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
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
    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    fun getAllInfo() {

        // Haal de userId op
        val userId = intent.getStringExtra("userId")?.toIntOrNull()

        if (userId == null) {
            Toast.makeText(this, "Geen gebruiker gevonden", Toast.LENGTH_SHORT).show()
            return
        }

        val dbHelper = DatabaseHelper(this)
        val userDataList = dbHelper.join(userId)
        // Koppel de TextViews en buttons
        val userNamePlaceholder = findViewById<TextView>(R.id.gebruikersnaam)
        val rolePlaceholder = findViewById<TextView>(R.id.role)
        val statusPlaceholder = findViewById<TextView>(R.id.status)

        val userTitleExtra = findViewById<TextView>(R.id.title)
        val userDescriptionExtra = findViewById<TextView>(R.id.desc)

        val deleteBtn = findViewById<Button>(R.id.button1)
        val update = findViewById<Button>(R.id.button2)

        if (userDataList.isNotEmpty()) {

            val user = userDataList[0]

            val userName = user["userName"] ?: ""
            val roleName = user["roleName"] ?: "Onbekende rol"
            val status = user["active"] ?: ""

            userTitleExtra.text = "Gebruiker: $userName"
            userDescriptionExtra.text =
                "De informatie van gebruiker $userName wordt hier weergegeven."

            userNamePlaceholder.text = "Gebruikersnaam: $userName"
            rolePlaceholder.text = "Rol: $roleName"
            statusPlaceholder.text = "Status: $status"

        } else {
            Toast.makeText(this, "Gebruiker niet gevonden", Toast.LENGTH_SHORT).show()
        }

        deleteBtn.setOnClickListener {

            if (userId == null) {
                Toast.makeText(this, "Geen gebruiker gevonden", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            delete()

            val intent = Intent(this, ShowAllUsers::class.java)
            startActivity(intent)
            finish()
        }

        update.setOnClickListener {

            if (userId == null) {
                Toast.makeText(this, "Geen gebruiker gevonden", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val intent = Intent(this, EditUser::class.java)

            // Als String doorgeven, omdat editUser getStringExtra gebruikt.
            intent.putExtra("userId", userId.toString())

            startActivity(intent)
            finish()
        }
    }



        @RequiresApi(Build.VERSION_CODES.O)
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