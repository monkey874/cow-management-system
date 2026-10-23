package com.example.cowmanagementsystem

import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.mutableStateMapOf
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.Date

class addOwnTreatment : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_own_treatment)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        test()
    }
    fun test(){
        val subBtn = findViewById<Button>(R.id.button)

        subBtn.setOnClickListener {
            val data = mutableStateMapOf(
                "treatmentId" to "1",
                "extratreatmentDescription" to "e",
                "cowId" to ShowAllCows.cowId.toString(),
                "userID" to login.UserID.toString(),
                "doneOn" to ""

            )
            val dbHelper = DatabaseHelper(this)
            dbHelper.voegNaamToe(data, "cowTreatments")
        }
    }
}