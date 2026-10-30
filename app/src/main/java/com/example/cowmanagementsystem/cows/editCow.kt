package com.example.cowmanagementsystem.cows

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.mutableStateMapOf
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.cowmanagementsystem.R
import com.example.cowmanagementsystem.database.DatabaseHelper
import com.example.cowmanagementsystem.functies.BaseActivity
import com.example.cowmanagementsystem.functies.ValidateInput

class editCow : BaseActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_edit_cow)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        getAllInfo()
        setupBackButton()
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun getAllInfo() {
        val cowId = intent.getStringExtra("cowId")?.toIntOrNull() ?: return

        val dbHelper = DatabaseHelper(this)
        val cowDataList = dbHelper.getId("cows", cowId, "cowID")

        if (cowDataList.isEmpty()) {
            return
        }

        val cowData = cowDataList[0]

        // Velden uit de XML
        val cowNumberPlaceholder =
            findViewById<EditText>(R.id.editTextDate6)

        val earTagPlaceholder =
            findViewById<EditText>(R.id.editTextDate4)

        val dayOfBirthPlaceholder =
            findViewById<EditText>(R.id.editTextDate3)

        val genderGroup =
            findViewById<RadioGroup>(R.id.genderGroup)

        val colorGroup =
            findViewById<RadioGroup>(R.id.colorGroup)

        // Waarden invullen
        cowNumberPlaceholder.setText(cowData["cowNumber"] ?: "")
        earTagPlaceholder.setText(cowData["earTagNumber"] ?: "")
        dayOfBirthPlaceholder.setText(cowData["dayOfBirth"] ?: "")

        // Geslacht selecteren
        when (cowData["gender"]) {
            "Mannelijk" -> genderGroup.check(R.id.radioButton11)
            "Vrouwelijk" -> genderGroup.check(R.id.radioButton10)
            else -> genderGroup.clearCheck()
        }

        // Kleur selecteren
        val colorRadioId = when (cowData["color"]) {
            "Rood-wit" -> R.id.radioButton2
            "Effen rood" -> R.id.radioButton9
            "Zwart-wit" -> R.id.radioButton5
            "Effen zwart" -> R.id.radioButton7
            "Effen bruin" -> R.id.radioButton8
            "Bruin-wit" -> R.id.radioButton6
            else -> -1
        }

        if (colorRadioId != -1) {
            colorGroup.check(colorRadioId)
        } else {
            colorGroup.clearCheck()
        }

        val defclass = ValidateInput()
        val saveButton = findViewById<Button>(R.id.button)
        saveButton.setOnClickListener {

            // all text field placeholder and groups
            val cowNumberPlaceholder = findViewById<EditText>(R.id.editTextDate6)
            val earTagNumberPlaceholder = findViewById<EditText>(R.id.editTextDate4)
            val datePlaceholder = findViewById<EditText>(R.id.editTextDate3)

            val genderGroup = findViewById<RadioGroup>(R.id.genderGroup)
            val colorGroup = findViewById<RadioGroup>(R.id.colorGroup)

            if (!defclass.validateField(findViewById(R.id.editTextDate6), findViewById(R.id.cowNumberError), "Koenummer mag niet leeg zijn")) return@setOnClickListener
            if (!defclass.validateField(findViewById(R.id.editTextDate4), findViewById(R.id.cowEargtagError), "oormerknummer mag niet leeg zijn")) return@setOnClickListener
            if (!defclass.validateField(findViewById(R.id.editTextDate3),findViewById(R.id.cowDataOfBurteError), "geboortedatum mag niet leeg zijn")) return@setOnClickListener

            if (!defclass.validateRadioGroup(genderGroup, findViewById(R.id.genderError), "Selecteer een geslacht")) return@setOnClickListener
            if (!defclass.validateRadioGroup(colorGroup, findViewById(R.id.colorError), "Selecteer een kleur")) return@setOnClickListener


            // all the values of the placeholders and groups
            val cowNumberText = cowNumberPlaceholder.text.toString()
            val earTagNumberText = earTagNumberPlaceholder.text.toString()
            val datePlaceholderText = datePlaceholder.text.toString()

            val checkedGender = genderGroup.checkedRadioButtonId
            val checkedColor = colorGroup.checkedRadioButtonId

            val valueGenderText = findViewById<RadioButton>(checkedGender).text.toString()
            val valueColorText = findViewById<RadioButton>(checkedColor).text.toString()

            // all cowInfo in a mutableStateMapOf and push everything to the database
            val dbHelper = DatabaseHelper(this)
            val cowInfo = mutableStateMapOf(
                "cowNumber" to cowNumberText,
                "earTagNumber" to earTagNumberText,
                "dayOfBirth" to datePlaceholderText,
                "gender" to valueGenderText,
                "color" to valueColorText,
            )
            dbHelper.UpdateItem(cowInfo, "cows", cowId.toString())

            val intent = Intent(this, ShowAllCows::class.java)
            startActivity(intent)
        }
    }
}