package com.example.cowmanagementsystem.functies

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.cowmanagementsystem.R

open class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    protected fun setupBackButton() {
        val backButton = findViewById<Button>(R.id.btnBack)

        backButton?.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }
}