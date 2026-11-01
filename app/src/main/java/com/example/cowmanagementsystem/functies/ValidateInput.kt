package com.example.cowmanagementsystem.functies


import android.view.View
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.TextView


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
