package com.example.cowmanagementsystem

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class notificationDetails : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_notification_details)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        test()
    }
    fun  test(){
        val cowId = intent.getStringExtra("notificationId")

        val dbHelper = DatabaseHelper(this)

        val info = dbHelper.getId("notifications", cowId?.toIntOrNull() ?: 0, "notificationId" )

        val Desc = findViewById<TextView>(R.id.desc)
        val title = findViewById<TextView>(R.id.title)

        val cowDesc = findViewById<TextView>(R.id.textOmschrijving)
        val cowTitle = findViewById<TextView>(R.id.titleOmschrijving)

        Log.d("123", "$info")

        if (info.isNotEmpty()) {


            val cow = info[0]
            title.text = "Melding: ${cow["title"]}"
            Desc.text = "De informatie van melding word hier weegegeven."

            cowTitle.text = "${cow["title"] ?: ""}"
            cowDesc.text = "${cow["description"]}"

        }


    }
}