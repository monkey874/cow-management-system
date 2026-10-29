package com.example.cowmanagementsystem.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateMapOf
import org.mindrot.jbcrypt.BCrypt
import androidx.compose.runtime.snapshots.SnapshotStateMap
import com.example.cowmanagementsystem.login.Login
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, "cowManagementSystem.db", null, 20) {

    override fun onCreate(db: SQLiteDatabase) {


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
        val password = "1"
        val hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt())

        db.execSQL(
            """
    INSERT OR IGNORE INTO users (
        userName,
        password,
        roleId,
        active
    ) VALUES (?, ?, ?, ?)
    """.trimIndent(),
            arrayOf("Admin", hashedPassword, 1, 1)
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
                extraTreatmentDescription TEXT NOT NULL,
                cowId TEXT NOT NULL,
                userID TEXT NOT NULL,
                doneOn TEXT NOT NULL,
                FOREIGN KEY (userID) REFERENCES users(userId),
                FOREIGN KEY (cowId) REFERENCES cows(cowId),
                FOREIGN KEY (treatmentId) REFERENCES treatments(treatmentId)
                
                
            )
            """.trimIndent()
        )
        db.execSQL(
            """
            CREATE TABLE roles (
               roleId INTEGER PRIMARY KEY AUTOINCREMENT,
               roleName TEXT NOT NULL
            )
            """.trimIndent()
        )
        db.execSQL("""
        INSERT OR IGNORE INTO roles (
            roleName
           
        ) VALUES (
            'Admin'
        )
        """.trimIndent())

        db.execSQL("""
        INSERT OR IGNORE INTO roles (
            roleName
           
        ) VALUES (
            'Medewerker'
        )
        """.trimIndent())

        db.execSQL(
            """
            CREATE TABLE history (
                logId INTEGER PRIMARY KEY AUTOINCREMENT,
                userId TEXT NOT NULL,
                actions TEXT NOT NULL,
                dbTable TEXT NOT NULL,
                recordId TEXT NOT NULL,
                dateTime TEXT NOT NULL,
                FOREIGN KEY (userId) REFERENCES users(userId)

            
                
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
        db.execSQL("DROP TABLE IF EXISTS cows")
        db.execSQL("DROP TABLE IF EXISTS notifications")
        db.execSQL("DROP TABLE IF EXISTS treatments")
        db.execSQL("DROP TABLE IF EXISTS cowTreatments")
        db.execSQL("DROP TABLE IF EXISTS roles")
        db.execSQL("DROP TABLE IF EXISTS history")

        onCreate(db)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun History(
        dbTable: String,
        action: String,
        recordId: String
    ) {
        val datum = LocalDate.now()
            .format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))

        val historyInfo = mutableStateMapOf(
            "userId" to Login.UserID.toString(),
            "actions" to action,
            "dbTable" to dbTable,
            "recordId" to recordId,
            "dateTime" to datum
        )

        addItemHistory(historyInfo, "history")
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun UpdateItem(
        naam: SnapshotStateMap<String, String>,
        tableName: String,
        recordId: String
    ): Boolean {

        // Toegestane tabellen met hun primaire sleutel
        val primaryKeys = mapOf(
            "cows" to "cowID",
            "users" to "userId",
            "treatments" to "treatmentId",
            "cowTreatments" to "cowTreatmentId",
            "notifications" to "notificationId",
            "roles" to "roleId",
            "history" to "logId"
        )

        val primaryKey = primaryKeys[tableName]

        // Controleer of de tabel bestaat
        if (primaryKey == null) {
            Log.e("DB", "Deze tabel bestaat niet: $tableName")
            return false
        }

        // Kolommen per tabel
        val columns = mapOf(
            "cows" to listOf(
                "cowNumber", "earTagNumber", "gender",
                "color", "dayOfBirth"
            ),
            "users" to listOf(
                "userName", "password", "roleId", "active"
            ),
            "treatments" to listOf(
                "treatmentName", "treatmentDescription"
            ),
            "cowTreatments" to listOf(
                "treatmentId", "extraTreatmentDescription",
                "cowId", "userID", "doneOn"
            ),
            "notifications" to listOf(
                "title", "description", "cowId",
                "userId", "createdOn", "processed"
            ),
            "roles" to listOf("roleName"),
            "history" to listOf(
                "userId", "actions", "dbTable",
                "recordId", "dateTime"
            )
        )

        val tableColumns = columns[tableName] ?: return false

        // Maak de waarden klaar voor de update
        val values = ContentValues()

        for (column in tableColumns) {
            val value = naam[column] ?: continue

            if (column == "password") {
                // Hash het wachtwoord alleen als het gewijzigd wordt
                if (value.isNotBlank()) {
                    values.put(column, BCrypt.hashpw(value, BCrypt.gensalt()))
                }
            } else {
                values.put(column, value)
            }
        }

        if (values.size() == 0) {
            Log.d("DB", "Geen waarden om bij te werken")
            return false
        }

        val db = writableDatabase

        return try {
            val updatedRows = db.update(
                tableName,
                values,
                "$primaryKey = ?",
                arrayOf(recordId)
            )

            if (updatedRows > 0 && tableName != "history") {
                History(
                    dbTable = tableName,
                    action = "bijgewerkt",
                    recordId = recordId
                )
            }

            updatedRows > 0
        } catch (e: Exception) {
            Log.e("DB", "Fout bij bijwerken van $tableName", e)
            false
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun addItem(naam: SnapshotStateMap<String, String>, tableName: String) {
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
                "extraTreatmentDescription",
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
            "history" to listOf("logId", "userId", "actions", "dbTable", "recordId", "dateTime")

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


        val db = writableDatabase

        val insertedId = db.insert(tableName, null, values)

        if (insertedId != -1L && tableName != "history") {
            History(
                dbTable = tableName,
                action = "toegevoegd",
                recordId = insertedId.toString()
            )
        }
    }

    fun addItemHistory(naam: SnapshotStateMap<String, String>, tableName: String) {
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
                "extraTreatmentDescription",
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
            "history" to listOf("logId", "userId", "actions", "dbTable", "recordId", "dateTime")

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

    fun getString(tableName: String, recordId: String, cowId: String): MutableList<Map<String, String>> {
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
            "SELECT * FROM $tableName WHERE $cowId = ?",
            arrayOf(recordId),
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

    fun getItems(tableName: String, fields: List<String>): MutableList<Map<String, String>> {

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

    @RequiresApi(Build.VERSION_CODES.O)
    fun delete(tableName: String, recordId: Int?, cowID: String) {
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

        val db = writableDatabase

        val deletedRows = db.delete(
            tableName,
            "$cowID = ?",
            arrayOf(recordId.toString())
        )

        if (deletedRows > 0) {
            History(
                dbTable = tableName,
                action = "verwijderd",
                recordId = recordId.toString()
            )
        }
    }

    fun getIdLog(tableName: String, recordId: String, cowId: String): MutableList<Map<String, String>> {
        val tables = arrayOf(
            "cows",
            "/users",
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