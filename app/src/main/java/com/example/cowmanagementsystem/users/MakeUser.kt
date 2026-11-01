package com.example.cowmanagementsystem.users

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateMapOf
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.cowmanagementsystem.functies.BaseActivity
import com.example.cowmanagementsystem.database.DatabaseHelper
import com.example.cowmanagementsystem.R
import com.example.cowmanagementsystem.functies.ValidateInput
import com.example.cowmanagementsystem.functies.GenerateList

class MakeUser : BaseActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_make_user)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        getAllInfo()
        setupBackButton()
        prepareDropdown()
    }
    fun prepareDropdown(){
        val needValues = listOf("roleId", "roleName")

        val dbHelper = DatabaseHelper(this)
        val cowDataItems = dbHelper.getItems("roles", needValues)

        val cowIds = mutableListOf<String>()
        val cowNumbers = mutableListOf<String>()

        for (cowData in cowDataItems) {
            cowData["roleId"]?.let { cowIds.add(it) }
            cowData["roleName"]?.let { cowNumbers.add(it) }
        }

        val def = GenerateList(this)
        def.fullRolesDropdown(cowNumbers)

    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun getAllInfo(){
        val defClass = ValidateInput()
        val makeButton = findViewById<Button>(R.id.button)
        makeButton.setOnClickListener {
            val dbHelper = DatabaseHelper(this)
            // all text field placeholder and groups
            val userNamePlaceholder = findViewById<TextView>(R.id.editTextDate6)
            val passwordPlaceholder = findViewById<TextView>(R.id.editTextDate4)
            val statusGroup = findViewById<RadioGroup>(R.id.genderGroup)

            if (!defClass.validateField(findViewById(R.id.editTextDate6), findViewById(R.id.GebruikersnaamError), "gebruikersnaam mag niet leeg zijn")) return@setOnClickListener
            if (!defClass.validateField(findViewById(R.id.editTextDate4), findViewById(R.id.wachtwoordError), "wachtwoord mag niet leeg zijn")) return@setOnClickListener

            if (!defClass.validateRadioGroup(statusGroup, findViewById(R.id.gebruikerStatusError), "Selecteer een status")) return@setOnClickListener

            val userNameText = userNamePlaceholder.text.toString()
            val passwordText = passwordPlaceholder.text.toString()

            val spinnerPlaceholder = findViewById<Spinner>(R.id.spinnerRoles)

            val selectedCowText = spinnerPlaceholder.selectedItem.toString()
            val cowInfo = dbHelper.getString("roles",selectedCowText,"roleName")
            val cowId = cowInfo[0]["roleId"].toString()

            val status = statusGroup.checkedRadioButtonId
            val valueStaus = findViewById<RadioButton>(status).text.toString()

            // all cowInfo in a mutableStateMapOf and push everything to the database
            val data = mutableStateMapOf(
                "userName" to userNameText,
                "password" to passwordText,
                "roleId" to cowId,
                "active" to valueStaus,
            )
            dbHelper.addItem(data, "users")

            val intent = Intent(this, ShowAllUsers::class.java)
            startActivity(intent)
        }
    }
}