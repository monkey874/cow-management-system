package com.example.cowmanagementsystem.cows

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.cowmanagementsystem.database.DatabaseHelper
import com.example.cowmanagementsystem.R
import com.example.cowmanagementsystem.treatment.AddOwnTreatment
import kotlin.text.toIntOrNull

class ActivityCowDetails : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_cow_details)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        getAllInfo()
    }
    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    fun getAllInfo(){

        // all cowInfo in a get from the database
        val cowId = intent.getStringExtra("cowId")
        val dbHelper = DatabaseHelper(this)
        val cowDataList = dbHelper.getId("cows", cowId?.toIntOrNull() ?: 0, "cowId" )

        // all text field placeholder and buttons
        val cowDescriptionPlaceholder = findViewById<TextView>(R.id.desc)
        val cowTitlePlaceholder = findViewById<TextView>(R.id.title)

        val cowNumberPlaceholder = findViewById<TextView>(R.id.textCowNumber)
        val earTagPlaceholder = findViewById<TextView>(R.id.textEarTag)

        val colorPlaceholder = findViewById<TextView>(R.id.textCowColor)
        val genderPlaceholder = findViewById<TextView>(R.id.textCowGender)
        val dayOfBurtPlaceholder = findViewById<TextView>(R.id.textCowBirthDate)

        val deleteButton = findViewById<Button>(R.id.button1)
        val treatmentButton = findViewById<Button>(R.id.button)
        val editButton = findViewById<TextView>(R.id.buttonEditCow)


        // set all the values on the cowDetails page
        if (cowDataList.isNotEmpty()) {
            val cow = cowDataList[0]

            cowTitlePlaceholder.text = ("Koe "+ cow["cowNumber"])
            cowDescriptionPlaceholder.text = ("De informatie van koe " + cow["cowNumber"] + "word hier weegegeven.")

            cowNumberPlaceholder.text = ("Koe " + cow["cowNumber"])
            earTagPlaceholder.text = ("Oormerknummer: " + cow["earTagNumber"])

            colorPlaceholder.text = ("Kleur: " + cow["color"])
            genderPlaceholder.text = ("Geslacht: " + cow["gender"])
            dayOfBurtPlaceholder.text = ("GeboorteDatum: " + cow["dayOfBirth"])

        }

        // clickable buttons for different pages
        deleteButton.setOnClickListener{
            deleteNow()
            val intent = Intent(this, ShowAllCows::class.java)
            startActivity(intent)
        }

        treatmentButton.setOnClickListener{

            val intent = Intent(this, AddOwnTreatment::class.java)
            startActivity(intent)
        }

        editButton.setOnClickListener{

            val intent = Intent(this, editCow::class.java)
            intent.putExtra(
                "cowId",
                cowId
            )
            startActivity(intent)
        }

    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun deleteNow() {
        val cowId = intent.getStringExtra("cowId")

        val dbHelper = DatabaseHelper(this)
        dbHelper.delete("cows", cowId?.toIntOrNull() ?: 0, "cowID")
    }
}