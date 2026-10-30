package com.example.cowmanagementsystem.functies

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.cowmanagementsystem.R
import com.example.cowmanagementsystem.dasboard.ActivityDashboard

open class BaseActivity : AppCompatActivity() {

    protected fun setupBackButton() {
        val backButton = findViewById<Button>(R.id.btnBack)

        backButton?.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    protected fun setupBackToDashboard() {
        val backButton = findViewById<Button>(R.id.btnBack)

        backButton?.setOnClickListener {
            val intent = Intent(this, ActivityDashboard::class.java)

            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or
                    Intent.FLAG_ACTIVITY_SINGLE_TOP

            startActivity(intent)
            finish()
        }
    }
}