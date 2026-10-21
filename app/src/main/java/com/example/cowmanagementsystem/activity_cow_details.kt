package com.example.cowmanagementsystem

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class activity_cow_details() : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_cow_details)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        test()
    }
    fun  test(){
        val cowId = intent.getStringExtra("cowId")

        val dbHelper = DatabaseHelper(this)

        val info = dbHelper.getId("cows", cowId?.toIntOrNull() ?: 0 )

        val cowDesc = findViewById<TextView>(R.id.desc)
        val cowTitle = findViewById<TextView>(R.id.title)

        val cowNumberPlaceholder = findViewById<TextView>(R.id.textCowNumber)
        val earTagPlaceholder = findViewById<TextView>(R.id.textEarTag)

        val color = findViewById<TextView>(R.id.textCowColor)
        val gender = findViewById<TextView>(R.id.textCowGender)
        val dayOfBurte = findViewById<TextView>(R.id.textCowBirthDate)

        Log.d("123", "$info")

        if (info.isNotEmpty()) {

            val cow = info[0]

            cowTitle.text = "Koe ${cow["cowNumber"] ?: ""}"
            cowDesc.text = "De informatie van koe ${cow["cowNumber"]} word hier weegegeven."

            cowNumberPlaceholder.text = "Koe " + cow["cowNumber"]?.toString() ?: ""
            earTagPlaceholder.text = "Oormerknummer: " + cow["earTagNumber"]?.toString() ?: ""

            color.text = ("Kleur: " + cow["color"]?.toString()) ?: ""
            gender.text = ("Geslacht: " + cow["gender"]?.toString()) ?: ""
            dayOfBurte.text = ("GeboorteDatum: " + cow["dayOfBirth"]?.toString()) ?: ""
        }


    }
}