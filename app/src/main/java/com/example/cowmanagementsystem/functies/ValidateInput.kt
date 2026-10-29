package com.example.cowmanagementsystem.functies

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.compose.runtime.mutableStateMapOf
import androidx.core.R
import com.example.cowmanagementsystem.cows.AddCow

class ValidateInput {

    fun validateField(
        editText: EditText,
        errorText: TextView,
        message: String
    ): Boolean {

        return if (editText.text.toString().isBlank()) {
            errorText.text = message
            errorText.visibility = View.VISIBLE
            false
        } else {
            errorText.visibility = View.GONE
            true
        }
    }

    fun validateRadioGroup(
        radioGroup: RadioGroup,
        errorText: TextView,
        message: String
    ): Boolean {

        return if (radioGroup.checkedRadioButtonId == -1) {
            errorText.text = message
            errorText.visibility = View.VISIBLE
            false
        } else {
            errorText.visibility = View.GONE
            true
        }
    }
}
