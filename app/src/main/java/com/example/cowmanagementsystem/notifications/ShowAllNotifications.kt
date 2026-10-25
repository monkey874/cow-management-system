package com.example.cowmanagementsystem.notifications

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.core.graphics.toColorInt
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.cowmanagementsystem.functies.BaseActivity
import com.example.cowmanagementsystem.database.DatabaseHelper
import com.example.cowmanagementsystem.R

class ShowAllNotifications : BaseActivity() {
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
        setupBackButton()
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    fun showAll(){
        val container = findViewById<LinearLayout>(R.id.linarLayoud)

        val dbHelper = DatabaseHelper(this)
        val needValues = listOf("notificationId", "title")
        val notificationsInfo = dbHelper.getItems("notifications", needValues)

        for (notification in notificationsInfo) {

            val cowLayout = LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL
                setPadding(16, 16, 16, 16)
                background = getDrawable(R.drawable.bg_option_group)

            }

            cowLayout.setOnClickListener {

                val intent = Intent(this, NotificationDetails::class.java)

                intent.putExtra(
                    "notificationId",
                    notification["notificationId"].toString()
                )

                startActivity(intent)
            }

            val textView = TextView(this).apply {

                text = (notification["title"])
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