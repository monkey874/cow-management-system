package com.example.cowmanagementsystem.functies

import android.app.Activity
import android.widget.ArrayAdapter
import android.widget.Spinner

class GenerateList(private val activity: Activity) {

    fun fullCowsDropdown(cows: List<String>) {
        val spinner = activity.findViewById<Spinner>(
            com.example.cowmanagementsystem.R.id.spinnerCows
        )

        val adapter = ArrayAdapter(
            activity,
            android.R.layout.simple_spinner_item,
            cows
        )

        adapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )

        spinner.adapter = adapter
    }

    fun fullTreatmentDropdown(treatment: List<String>) {
        val spinner = activity.findViewById<Spinner>(
            com.example.cowmanagementsystem.R.id.spinnerTreatments
        )

        val adapter = ArrayAdapter(
            activity,
            android.R.layout.simple_spinner_item,
            treatment
        )

        adapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )

        spinner.adapter = adapter
    }
    fun fullRolesDropdown(roles: List<String>) {
        val spinner = activity.findViewById<Spinner>(
            com.example.cowmanagementsystem.R.id.spinnerRoles
        )

        val adapter = ArrayAdapter(
            activity,
            android.R.layout.simple_spinner_item,
            roles
        )

        adapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )

        spinner.adapter = adapter
    }
}