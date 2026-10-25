package com.example.cowmanagementsystem.users

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

class ShowAllUsers : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_show_all_users)
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
        val needValues = listOf("userId", "userName")
        val userInfo = dbHelper.getItems("users", needValues)

        for (user in userInfo) {

            val cowLayout = LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL
                setPadding(16, 16, 16, 16)
                background = getDrawable(R.drawable.bg_option_group)

            }

            cowLayout.setOnClickListener {

                val intent = Intent(this, UserDetails::class.java)

                intent.putExtra(
                    "userId",
                    user["userId"].toString()
                )

                startActivity(intent)
            }

            val textView = TextView(this).apply {

                text = (user["userName"])
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