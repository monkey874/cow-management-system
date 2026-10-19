package com.example.cowmanagementsystem

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, "namen.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE namen (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                naam TEXT NOT NULL
            )
            """.trimIndent()
        )
    }

    override fun onUpgrade(
        db: SQLiteDatabase,
        oldVersion: Int,
        newVersion: Int
    ) {
        // Later gebruiken we dit voor database-updates.
    }

    fun voegNaamToe(naam: String) {
        val values = ContentValues().apply {
            put("naam", naam)
        }

        writableDatabase.insert(
            "namen",
            null,
            values
        )
    }

    fun haalNamenOp(): List<String> {
        val namen = mutableListOf<String>()

        val cursor = readableDatabase.rawQuery(
            "SELECT naam FROM namen",
            null
        )

        cursor.use {
            while (it.moveToNext()) {
                namen.add(it.getString(0))
            }
        }

        return namen
    }
}