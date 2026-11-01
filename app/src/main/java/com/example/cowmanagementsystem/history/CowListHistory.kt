package com.example.cowmanagementsystem.history

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.toColorInt
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.cowmanagementsystem.KoeProfiel
import com.example.cowmanagementsystem.R
import com.example.cowmanagementsystem.database.DatabaseHelper

class CowListHistory : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_cow_list_history)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        showAll()
    }

    @SuppressLint("UseCompatLoadingForDrawables", "SetTextI18n")
    fun showAll(){
        val container = findViewById<LinearLayout>(R.id.linarLayoud)

        val dbHelper = DatabaseHelper(this)
        val needValues = listOf("cowID", "cowNumber")
        val cowInfo = dbHelper.getItems("cows", needValues)

        for (cow in cowInfo) {
            val cowLayout = LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL
                setPadding(16, 16, 16, 16)
                background = getDrawable(R.drawable.bg_option_group)

            }

            cowLayout.setOnClickListener {
                val intent = Intent(this, KoeProfiel::class.java)
                intent.putExtra("cowId", cow["cowID"])
                startActivity(intent)
            }

            val textView = TextView(this).apply {

                text = ("Koe " + cow["cowNumber"])
                textSize = 16f

                setTextColor("#263238".toColorInt())
                gravity = Gravity.CENTER_VERTICAL

                val params = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    56.dpToPx()
                )
                layoutParams = params
            }

            cowLayout.addView(textView)


            val cowParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
            cowParams.bottomMargin = 16

            cowLayout.layoutParams = cowParams
            container.addView(cowLayout)
        }
    }
    fun Int.dpToPx(): Int {
        return (this * resources.displayMetrics.density).toInt()
    }
}