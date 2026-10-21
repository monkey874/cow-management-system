package com.example.cowmanagementsystem

import android.os.Bundle
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class showAllNotifications : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_show_all_notifications)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        showAll()
    }

    fun showAll(){
        val container = findViewById<LinearLayout>(R.id.linarLayoud)

        val dbHelper = DatabaseHelper(this)
        val needvalues = listOf("notificationId", "title")
        val info = dbHelper.haalNamenOp("notifications", needvalues)

        for (cow in info) {

            val cowLayout = LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL
                setPadding(16, 16, 16, 16)
                background = getDrawable(R.drawable.bg_option_group)

            }

            val textView = TextView(this).apply {

                text = "${cow["title"] ?: ""}"
                textSize = 16f

                setTextColor(android.graphics.Color.parseColor("#263238"))
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