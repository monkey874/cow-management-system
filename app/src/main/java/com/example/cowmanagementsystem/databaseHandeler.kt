package com.example.cowmanagementsystem

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import org.mindrot.jbcrypt.BCrypt
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.core.content.ContextCompat.startActivity

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, "namen.db", null, 11) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE namen (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                naam TEXT NOT NULL
            )
            """.trimIndent()
        )
        db.execSQL(
            """
            CREATE TABLE users (
                userId INTEGER PRIMARY KEY AUTOINCREMENT,
                userName TEXT NOT NULL,
                password TEXT NOT NULL,
                roleId TEXT NOT NULL,
                active TEXT NOT NULL
                
            )
            """.trimIndent()
        )
        db.execSQL(
            """
            CREATE TABLE cows (
                cowID INTEGER PRIMARY KEY AUTOINCREMENT,
                cowNumber TEXT NOT NULL,
                earTagNumber TEXT NOT NULL,
                gender TEXT NOT NULL,
                color TEXT NOT NULL,
                dayOfBirth TEXT NOT NULL
                
            )
            """.trimIndent()
        )
        db.execSQL(
            """
            CREATE TABLE notifications (
                notificationId INTEGER PRIMARY KEY AUTOINCREMENT,
                title TEXT NOT NULL,
                description TEXT NOT NULL,
                cowId TEXT NOT NULL,
                userId TEXT NOT NULL,
                createdOn TEXT NOT NULL,
                processed TEXT NOT NULL,
                FOREIGN KEY (userId) REFERENCES users(userId)
                
                
            )
            """.trimIndent()
        )
        db.execSQL(
            """
            CREATE TABLE treatments (
                treatmentId INTEGER PRIMARY KEY AUTOINCREMENT,
                treatmentName TEXT NOT NULL,
                treatmentDescription TEXT NOT NULL
            )
            """.trimIndent()
        )
        db.execSQL(
            """
            CREATE TABLE cowTreatments (
                cowTreatmentId INTEGER PRIMARY KEY AUTOINCREMENT,
                treatmentId TEXT NOT NULL,
                extratreatmentDescription TEXT NOT NULL,
                cowId TEXT NOT NULL,
                userID TEXT NOT NULL,
                doneOn TEXT NOT NULL,
                FOREIGN KEY (userID) REFERENCES users(userId),
                FOREIGN KEY (cowId) REFERENCES cows(cowId),
                FOREIGN KEY (treatmentId) REFERENCES treatments(treatmentId)
                
                
            )
            """.trimIndent()
        )

    }

    override fun onUpgrade(
        db: SQLiteDatabase,
        oldVersion: Int,
        newVersion: Int
    ) {
        db.execSQL("DROP TABLE IF EXISTS users")
        db.execSQL("DROP TABLE IF EXISTS namen")
        db.execSQL("DROP TABLE IF EXISTS cows")
        db.execSQL("DROP TABLE IF EXISTS notifications")
        db.execSQL("DROP TABLE IF EXISTS treatments")
        db.execSQL("DROP TABLE IF EXISTS cowTreatments")

        onCreate(db)
    }

    fun voegNaamToe(naam: SnapshotStateMap<String, String>, tableName: String) {
        val tables = arrayOf(
            "cows",
            "users",
            "treatments",
            "cowTreatments",
            "notifications",
            "roles",
            "history"
        )
        val colum = mapOf(
            "cows" to listOf("cowID", "cowNumber", "earTagNumber", "gender", "color", "dayOfBirth"),
            "users" to listOf("userId", "userName", "password", "roleId", "active"),
            "treatments" to listOf("treatmentId", "treatmentName", "treatmentDescription"),
            "cowTreatments" to listOf(
                "cowTreatmentId",
                "treatmentId",
                "extratreatmentDescription",
                "cowId",
                "userID",
                "doneOn"
            ),
            "notifications" to listOf(
                "notificationId",
                "title",
                "description",
                "cowId",
                "userId",
                "createdOn",
                "processed"
            ),
            "roles" to listOf("roleId", "roleName"),
            "history" to listOf("logId", "userId", "action", "table", "recordId", "dateTime")

        )

        val hasTable = tables.contains(tableName)
        if (!hasTable) {
            Log.d("DB", "The database table doesn't exist")
        }

        Log.d("test1", naam.toString())
        val columns = colum[tableName]
        val values = ContentValues().apply {
            if (columns != null) {
                for (x in columns.drop(1)) {
                    if (x == "password") {
                        Log.d("123", "ik ben in de crypte")
                        Log.d("123", naam[x].toString())
                        var sriptie = BCrypt.hashpw(naam[x], BCrypt.gensalt())
                        Log.d("123", "ik ben in de crypte dit is de $sriptie")
                        put(x, sriptie ?: "De value is null")
                    } else {
                        put(x, naam[x] ?: "De value is null")
                    }
                }
            }
        }
        Log.d("test", values.toString())


        writableDatabase.insert(
            tableName, null, values
        )
    }

    fun getId(tableName: String, recordId: Int?, cowId: String): MutableList<Map<String, String>> {
        val tables = arrayOf(
            "cows",
            "users",
            "treatments",
            "cowTreatments",
            "notifications",
            "roles",
            "history"
        )

        val hasTable = tables.contains(tableName)
        if (!hasTable) {
            Log.d("DB", "The database table doesn't exist")
        }


        val cursor = readableDatabase.rawQuery(
            "SELECT * FROM $tableName WHERE $cowId = $recordId",
            null
        )

        val info = mutableListOf<Map<String, String>>()

        cursor.use {
            while (it.moveToNext()) {

                val row = mutableMapOf<String, String>()

                for (i in 0 until it.columnCount) {
                    val columnName = it.getColumnName(i)
                    val value = it.getString(i) ?: ""

                    row[columnName] = value
                }

                info.add(row)
            }
        }


        return info

    }

    fun haalNamenOp(tableName: String, fields: List<String>): MutableList<Map<String, String>> {

        val tables = arrayOf(
            "cows",
            "users",
            "treatments",
            "cowTreatments",
            "notifications",
            "roles",
            "history"
        )

        val hasTable = tables.contains(tableName)
        if (!hasTable) {
            Log.d("DB", "The database table doesn't exist")
        }

        val columns = fields.joinToString(", ")
        val cursor = readableDatabase.rawQuery(
            "SELECT $columns FROM $tableName",
            null
        )

        val info = mutableListOf<Map<String, String>>()

        cursor.use {
            while (it.moveToNext()) {

                val row = mutableMapOf<String, String>()

                for (i in 0 until it.columnCount) {
                    val columnName = it.getColumnName(i)
                    val value = it.getString(i) ?: ""

                    row[columnName] = value
                }

                info.add(row)
            }
        }


        return info
    }

    fun delethe(tableName: String, recordId: Int?, cowID: String) {
        val tables = arrayOf(
            "cows",
            "users",
            "treatments",
            "cowTreatments",
            "notifications",
            "roles",
            "history"
        )

        val hasTable = tables.contains(tableName)
        if (!hasTable) {
            Log.d("DB", "The database table doesn't exist")
        }

        Log.d("db", "${tableName.toString()}")
        Log.d("db", "${recordId.toString()}")
        Log.d("db", "${cowID.toString()}")

        val cursor = readableDatabase.execSQL(
            "DELETE FROM $tableName WHERE $cowID = $recordId",

            )

    }

    fun getIdLog(tableName: String, recordId: String, cowId: String): MutableList<Map<String, String>> {
        val tables = arrayOf(
            "cows",
            "users",
            "treatments",
            "cowTreatments",
            "notifications",
            "roles",
            "history"
        )

        val hasTable = tables.contains(tableName)
        if (!hasTable) {
            Log.d("DB", "The database table doesn't exist")
        }


        val cursor = readableDatabase.rawQuery(
            "SELECT * FROM $tableName WHERE $cowId = '$recordId'",
            null
        )

        val info = mutableListOf<Map<String, String>>()

        cursor.use {
            while (it.moveToNext()) {

                val row = mutableMapOf<String, String>()

                for (i in 0 until it.columnCount) {
                    val columnName = it.getColumnName(i)
                    val value = it.getString(i) ?: ""

                    row[columnName] = value
                }

                info.add(row)
            }
        }
        return info
    }
}