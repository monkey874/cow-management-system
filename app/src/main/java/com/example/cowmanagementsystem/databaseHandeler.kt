package com.example.cowmanagementsystem

import android.R
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import org.mindrot.jbcrypt.BCrypt
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.snapshots.SnapshotStateMap

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, "namen.db", null, 5) {

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
                processed TEXT NOT NULL
                
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
            onCreate(db)
    }

    fun voegNaamToe(naam: SnapshotStateMap<String, String>, tableName: String) {
        val tables = arrayOf("cows","users","treatments","cowTreatments","notifications","roles","history")
        val colum = mapOf(
            "cows" to listOf("cowID","cowNumber","earTagNumber","gender", "color", "dayOfBirth"),
            "users" to listOf("userId", "userName", "password", "roleId", "active"),
            "treatments" to listOf("treatmentId", "treatmentName", "treatmentDescription"),
            "cowTreatments" to listOf("cowTreatmentId", "treatmentId", "cowId", "userId", "startDate", "endDate"),
            "notifications" to listOf("notificationId", "title", "description", "cowId","userId", "createdOn", "processed"),
            "roles" to listOf("roleId", "roleName"),
            "history" to listOf("logId", "userId", "action", "table", "recordId", "dateTime")

        )

        val hasTable = tables.contains(tableName)
        if (!hasTable){
            Log.d("DB", "The database table doesn't exist")
        }

        Log.d("test1", naam.toString())
        val columns = colum[tableName]
        val values = ContentValues().apply {
            if (columns != null) {
                for (x in columns.drop(1)){
                    if (x == "password"){
                        Log.d("123", "ik ben in de crypte")
                        Log.d("123", naam[x].toString())
                        var sriptie = BCrypt.hashpw(naam[x], BCrypt.gensalt())
                        Log.d("123", "ik ben in de crypte dit is de $sriptie")
                        put(x, sriptie ?: "De value is null")
                    }else{
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