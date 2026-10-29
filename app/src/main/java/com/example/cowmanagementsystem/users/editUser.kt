package com.example.cowmanagementsystem.users

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.mutableStateMapOf
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.cowmanagementsystem.R
import com.example.cowmanagementsystem.database.DatabaseHelper
import com.example.cowmanagementsystem.functies.ValidateInput

class editUser : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_edit_user)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        getAllData()
    }

    fun getAllData() {
        val userId = intent.getStringExtra("userId")?.toIntOrNull()

        if (userId == null) {
            Toast.makeText(this, "Geen gebruiker gevonden", Toast.LENGTH_SHORT).show()
            return
        }

        val dbHelper = DatabaseHelper(this)

        // Haal de gebruiker op
        val userDataList = dbHelper.getId("users", userId, "userId")

        if (userDataList.isEmpty()) {
            Toast.makeText(this, "Gebruiker niet gevonden", Toast.LENGTH_SHORT).show()
            return
        }

        val userData = userDataList[0]

        // Koppel de velden aan de XML
        val userNameField = findViewById<EditText>(R.id.editTextDate6)
        val statusGroup = findViewById<RadioGroup>(R.id.genderGroup)
        val rolesSpinner = findViewById<Spinner>(R.id.spinnerRoles)

        // Vul de gebruikersnaam in
        userNameField.setText(userData["userName"] ?: "")

        // Het wachtwoord is gehasht in de database.
        // Laat het veld daarom leeg; vul de hash niet in als wachtwoord.


        // Selecteer de status
        when (userData["active"]) {
            "Actief" -> statusGroup.check(R.id.radioButton11)
            "Niet actief" -> statusGroup.check(R.id.radioButton10)
            else -> statusGroup.clearCheck()
        }

        // Haal alle rollen op en zet de gekoppelde rol bovenaan
        val roleId = userData["roleId"]?.toIntOrNull()

        val db = dbHelper.readableDatabase
        val rolesList = mutableListOf<Pair<Int, String>>()

        val cursor = db.rawQuery(
            "SELECT roleId, roleName FROM roles",
            null
        )

        cursor.use {
            while (it.moveToNext()) {
                val id = it.getInt(it.getColumnIndexOrThrow("roleId"))
                val name = it.getString(it.getColumnIndexOrThrow("roleName"))

                rolesList.add(Pair(id, name))
            }
        }

// Zet de rol van de gebruiker als eerste
        if (roleId != null) {
            rolesList.sortWith(
                compareBy<Pair<Int, String>> { if (it.first == roleId) 0 else 1 }
                    .thenBy { it.second }
            )
        } else {
            rolesList.sortBy { it.second }
        }

// Vul de Spinner met de rolnamen
        val roleNames = rolesList.map { it.second }

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            roleNames
        )

        adapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )

        rolesSpinner.adapter = adapter

// Selecteer de rol die bij de gebruiker hoort
        val selectedIndex = rolesList.indexOfFirst { it.first == roleId }

        if (selectedIndex >= 0) {
            rolesSpinner.setSelection(selectedIndex)
        }
        val defclass = ValidateInput()
        val makeButton = findViewById<Button>(R.id.button)
        makeButton.setOnClickListener {
            val dbHelper = DatabaseHelper(this)
            // all text field placeholder and groups
            val userNamePlaceholder = findViewById<TextView>(R.id.editTextDate6)
            val statusGroup = findViewById<RadioGroup>(R.id.genderGroup)

            val userNameText = userNamePlaceholder.text.toString()
            if (!defclass.validateField(findViewById(R.id.editTextDate6), findViewById(R.id.GebruikersnaamError), "gebruikersnaam mag niet leeg zijn")) return@setOnClickListener
            if (!defclass.validateRadioGroup(statusGroup, findViewById(R.id.gebruikerStatusError), "Selecteer een geslacht")) return@setOnClickListener

            val spinnerPlaceholder = findViewById<Spinner>(R.id.spinnerRoles)

            val selectedCowText = spinnerPlaceholder.selectedItem.toString()
            val cowInfo = dbHelper.getString("roles",selectedCowText,"roleName")
            val cowId = cowInfo[0]["roleId"].toString()

            val status = statusGroup.checkedRadioButtonId
            val valueStaus = findViewById<RadioButton>(status).text.toString()

            // all cowInfo in a mutableStateMapOf and push everything to the database
            val data = mutableStateMapOf(
                "userName" to userNameText,
                "roleId" to cowId,
                "active" to valueStaus,
            )
            dbHelper.UpdateItem(data, "users", cowId)

            val intent = Intent(this, ShowAllUsers::class.java)
            startActivity(intent)
        }
    }
}