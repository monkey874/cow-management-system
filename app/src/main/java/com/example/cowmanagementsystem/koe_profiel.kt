
package com.example.cowmanagementsystem

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.cowmanagementsystem.database.DatabaseHelper

class KoeProfiel : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_koe_profiel)

        dbHelper = DatabaseHelper(this)

        findViewById<Button>(R.id.btnBack).setOnClickListener {
            finish()
        }

        val cowId = intent.getStringExtra("cowId")?.toIntOrNull()

        if (cowId == null) {
            Toast.makeText(this, "Geen geldige koe geselecteerd", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        laadKoeProfiel(cowId)
    }

    private fun laadKoeProfiel(cowId: Int) {
        val koeLijst = dbHelper.getId("cows", cowId, "cowID")

        if (koeLijst.isEmpty()) {
            Toast.makeText(this, "Deze koe is niet gevonden", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        val koe = koeLijst[0]

        findViewById<TextView>(R.id.textCowNumber).text =
            "Koeiennummer: ${koe["cowNumber"] ?: "-"}"

        findViewById<TextView>(R.id.textEarTag).text =
            "Oornummer: ${koe["earTagNumber"] ?: "-"}"

        findViewById<TextView>(R.id.textGender).text =
            "Geslacht: ${koe["gender"] ?: "-"}"

        findViewById<TextView>(R.id.textColor).text =
            "Kleur: ${koe["color"] ?: "-"}"

        findViewById<TextView>(R.id.textBirthDate).text =
            "Geboortedatum: ${koe["dayOfBirth"] ?: "-"}"

        laadAantalMeldingen(cowId)
        laadAantalBehandelingen(cowId)
        laadGeschiedenis(cowId)
    }

    private fun laadAantalMeldingen(cowId: Int) {
        val db = dbHelper.readableDatabase

        val cursor = db.rawQuery(
            "SELECT COUNT(*) FROM notifications WHERE cowId = ?",
            arrayOf(cowId.toString())
        )

        cursor.use {
            val aantal = if (it.moveToFirst()) it.getInt(0) else 0
            findViewById<TextView>(R.id.textNotifications).text =
                "Meldingen: $aantal"
        }
    }

    private fun laadAantalBehandelingen(cowId: Int) {
        val db = dbHelper.readableDatabase

        val cursor = db.rawQuery(
            "SELECT COUNT(*) FROM cowTreatments WHERE cowId = ?",
            arrayOf(cowId.toString())
        )

        cursor.use {
            val aantal = if (it.moveToFirst()) it.getInt(0) else 0
            findViewById<TextView>(R.id.textTreatments).text =
                "Behandelingen: $aantal"
        }
    }

    private fun laadGeschiedenis(cowId: Int) {
        val db = dbHelper.readableDatabase

        // Huidige geschiedenisregels die rechtstreeks naar deze koe verwijzen.
        val cursor = db.rawQuery(
            """
            SELECT h.actions, h.dbTable, h.dateTime, h.userId
            FROM history h
            WHERE h.dbTable = ? AND h.recordId = ?
            ORDER BY h.logId DESC
            """.trimIndent(),
            arrayOf("cows", cowId.toString())
        )

        val regels = mutableListOf<String>()

        cursor.use {
            while (it.moveToNext()) {
                val actie = it.getString(0) ?: "Onbekende actie"
                val tabel = it.getString(1) ?: "Onbekend"
                val datum = it.getString(2) ?: "Onbekend"
                val gebruiker = it.getString(3) ?: "Onbekend"

                regels.add(
                    "Actie: $actie\n" +
                            "Onderdeel: $tabel\n" +
                            "Datum: $datum\n" +
                            "Gebruiker-ID: $gebruiker"
                )
            }
        }

        findViewById<TextView>(R.id.textHistory).text =
            if (regels.isEmpty()) {
                "Nog geen geschiedenis beschikbaar voor deze koe."
            } else {
                regels.joinToString("\n\n──────────────\n\n")
            }
    }
}
