package com.example.cowmanagementsystem.functies

import android.app.Activity
import android.widget.ArrayAdapter
import android.widget.Spinner

class generateList(private val activity: Activity) {

    fun vulKoeienDropdown(koeien: List<String>) {
        val spinner = activity.findViewById<Spinner>(
            com.example.cowmanagementsystem.R.id.spinnerCows
        )

        val adapter = ArrayAdapter(
            activity,
            android.R.layout.simple_spinner_item,
            koeien
        )

        adapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )

        spinner.adapter = adapter
    }

    fun vulTreatmentDropdown(koeien: List<String>) {
        val spinner = activity.findViewById<Spinner>(
            com.example.cowmanagementsystem.R.id.spinnerTreatments
        )

        val adapter = ArrayAdapter(
            activity,
            android.R.layout.simple_spinner_item,
            koeien
        )

        adapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )

        spinner.adapter = adapter
    }
    fun vulrolesDropdown(koeien: List<String>) {
        val spinner = activity.findViewById<Spinner>(
            com.example.cowmanagementsystem.R.id.spinnerRoles
        )

        val adapter = ArrayAdapter(
            activity,
            android.R.layout.simple_spinner_item,
            koeien
        )

        adapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )

        spinner.adapter = adapter
    }
}