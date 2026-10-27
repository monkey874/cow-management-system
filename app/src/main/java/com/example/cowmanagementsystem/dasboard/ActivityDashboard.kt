package com.example.cowmanagementsystem.dasboard

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.cowmanagementsystem.R
import com.example.cowmanagementsystem.cows.AddCow
import com.example.cowmanagementsystem.cows.ShowAllCows
import com.example.cowmanagementsystem.history.History
import com.example.cowmanagementsystem.notifications.AddNotifications
import com.example.cowmanagementsystem.notifications.ShowAllNotifications
import com.example.cowmanagementsystem.treatment.AddTreatment
import com.example.cowmanagementsystem.users.MakeUser
import com.example.cowmanagementsystem.users.ShowAllUsers
import com.example.cowmanagementsystem.users.UserDetails

class ActivityDashboard : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_dashboard)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setupButtonClickListeners()
    }
    fun setupButtonClickListeners() {

        val buttonIds = listOf(
            R.id.cowButton,
            R.id.noticionButton,
            R.id.userButton,
            R.id.historyButton,
            R.id.btnAddCow,
            R.id.btnNotifications,
            R.id.btnAddUser,
            R.id.btnAddTreatment
        )

        for (id in buttonIds) {
            val button = findViewById<android.view.View>(id)

            button.setOnClickListener {
                when (button.id) {

                    R.id.cowButton -> {
                        val intent = Intent(this, ShowAllCows::class.java)
                        startActivity(intent)
                    }

                    R.id.noticionButton -> {
                        val intent = Intent(this, ShowAllNotifications::class.java)
                        startActivity(intent)
                    }

                    R.id.userButton -> {
                        val intent = Intent(this, ShowAllUsers::class.java)
                        startActivity(intent)
                    }

                    R.id.historyButton -> {
                        val intent = Intent(this, History::class.java)
                        startActivity(intent)
                    }

                    R.id.btnAddCow -> {
                        val intent = Intent(this, AddCow::class.java)
                        startActivity(intent)
                    }

                    R.id.btnNotifications -> {
                        val intent = Intent(this, AddNotifications::class.java)
                        startActivity(intent)
                    }

                    R.id.btnAddUser -> {
                        val intent = Intent(this, MakeUser::class.java)
                        startActivity(intent)
                    }

                    R.id.btnAddTreatment -> {
                        val intent = Intent(this, AddTreatment::class.java)
                        startActivity(intent)
                    }
                }
            }
        }
    }
}