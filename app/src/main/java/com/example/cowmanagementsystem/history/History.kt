package com.example.cowmanagementsystem.history

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.cowmanagementsystem.R

class History : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_history)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        getAllInfo()
    }
    fun getAllInfo(){
        findViewById<Button>(R.id.btnBack).setOnClickListener {
            finish()
        }

        findViewById<Button>(R.id.btnKoeien).setOnClickListener {
            val intent = Intent(this, CowListHistory::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.btnUsers).setOnClickListener {
            val intent = Intent(this, UserListHistory::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.btnMeldingen).setOnClickListener {
            val intent = Intent(this, NotificationListHistory::class.java)
            startActivity(intent)
        }
    }
}