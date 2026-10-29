package com.example.cowmanagementsystem.treatment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.mutableStateMapOf
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.cowmanagementsystem.database.DatabaseHelper
import com.example.cowmanagementsystem.R
import com.example.cowmanagementsystem.cows.ActivityCowDetails
import com.example.cowmanagementsystem.cows.ShowAllCows
import com.example.cowmanagementsystem.functies.ValidateInput
import com.example.cowmanagementsystem.functies.generateList
import com.example.cowmanagementsystem.login.Login
import kotlin.text.toIntOrNull

class AddOwnTreatment : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_own_treatment)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        getAllInfo()
        prepareDropdown()
    }

    fun prepareDropdown(){
        val needValues = listOf("treatmentId", "treatmentName")

        val dbHelper = DatabaseHelper(this)
        val cowDataItems = dbHelper.getItems("treatments", needValues)

        val cowIds = mutableListOf<String>()
        val cowNumbers = mutableListOf<String>()

        for (cowData in cowDataItems) {
            cowData["treatmentId"]?.let { cowIds.add(it) }
            cowData["treatmentName"]?.let { cowNumbers.add(it) }
        }

        val def = generateList(this)
        def.vulTreatmentDropdown(cowNumbers)

    }

    fun getAllInfo(){
        val dbHelper = DatabaseHelper(this)
        val defclass = ValidateInput()
        // all text field placeholder and groups and buttons
        val subBtn = findViewById<Button>(R.id.button)

        // all treatmentInfo in a mutableStateMapOf and push everything to the database
        subBtn.setOnClickListener {

            val spinnerPlaceholder = findViewById<Spinner>(R.id.spinnerTreatments)
            val dateOfTreatment = findViewById<EditText>(R.id.editTextDate3)
            val notes = findViewById<EditText>(R.id.editTextDate6)


            if (!defclass.validateField(findViewById(R.id.editTextDate3), findViewById(R.id.errorUitgevoerdOp), "Dit veld mag niet leeg zijn")) return@setOnClickListener
            if (!defclass.validateField(findViewById(R.id.editTextDate6), findViewById(R.id.errorNotitie), "Dit veld mag niet leeg zijn")) return@setOnClickListener

            val selectedCowText = spinnerPlaceholder.selectedItem.toString()
            val cowInfo = dbHelper.getString("treatments",selectedCowText,"treatmentName")
            val cowId = cowInfo[0]["treatmentId"].toString()
            val treatmentInfo = mutableStateMapOf(
                "treatmentId" to cowId,
                "extraTreatmentDescription" to notes.toString(),
                "cowId" to ShowAllCows.cowId.toString(),
                "userID" to Login.UserID.toString(),
                "doneOn" to dateOfTreatment.toString()

            )
            dbHelper.addItem(treatmentInfo, "cowTreatments")
            val intent = Intent(this, ActivityCowDetails::class.java)
            intent.putExtra(
                "cowId",
                ShowAllCows.cowId.toString()
            )
            startActivity(intent)
        }
    }
}