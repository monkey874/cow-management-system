package com.example.cowmanagementsystem

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.cowmanagementsystem.ui.theme.CowManagementSystemTheme
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import android.content.Intent
import com.example.cowmanagementsystem.cows.ActivityCowDetails
import com.example.cowmanagementsystem.cows.AddCow
import com.example.cowmanagementsystem.cows.ShowAllCows
import com.example.cowmanagementsystem.database.DatabaseHelper
import com.example.cowmanagementsystem.login.Login
import com.example.cowmanagementsystem.notifications.AddNotifications
import com.example.cowmanagementsystem.notifications.ShowAllNotifications
import com.example.cowmanagementsystem.treatment.AddTreatment
import com.example.cowmanagementsystem.users.MakeUser
import com.example.cowmanagementsystem.users.ShowAllUsers
import com.example.cowmanagementsystem.users.UserDetails
import kotlin.jvm.java

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CowManagementSystemTheme {
                Scaffold(

                    modifier = Modifier.fillMaxSize(),




                ) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)

                    )
                    GenerateInputs(
                        modifier = Modifier,
                        showFields = arrayOf("Gebruikersnaam","Wachtwoord")
                    )




                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier,



    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CowManagementSystemTheme {
        Greeting("Android")

    }
}

@Composable
fun GenerateInputs(
    modifier: Modifier,
    showFields: Array<String>
) {
    val fields = listOf("userName", "password")

    val resourcesField = remember {
        mutableStateMapOf(
            fields[0] to "",
            fields[1] to ""
        )
    }

    // Context moet buiten de Column staan
    val context = LocalContext.current

    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        for (i in fields.indices) {
            OutlinedTextField(
                value = resourcesField[fields[i]] ?: "",
                onValueChange = {
                    resourcesField[fields[i]] = it
                },
                label = {
                    Text(showFields[i])
                }
            )
        }

        GenerateButton(resourcesField, "Login")

        Button(
            onClick = {
                val intent = Intent(
                    context,
                    AddCow::class.java
                )
                context.startActivity(intent)
            }
        ) {
            Text("Alle koeien bekijken")
        }


        Button(
            onClick = {
                val intent = Intent(
                    context,
                    AddNotifications::class.java
                )
                context.startActivity(intent)
            }
        ) {
            Text("Alle meldingen bekijken")
        }
        Button(
            onClick = {
                val intent = Intent(
                    context,
                    AddTreatment::class.java
                )
                context.startActivity(intent)
            }
        ) {
            Text("behandeling aanmaken")
        }

        Button(
            onClick = {
                val intent = Intent(
                    context,
                    ShowAllCows::class.java
                )
                context.startActivity(intent)
            }
        ) {
            Text("koeien lijst")
        }

        Button(
            onClick = {
                val intent = Intent(
                    context,
                    ShowAllNotifications::class.java
                )
                context.startActivity(intent)
            }
        ) {
            Text("notificatie lijst")
        }

        Button(
            onClick = {
                val intent = Intent(
                    context,
                    ActivityCowDetails::class.java
                )
                context.startActivity(intent)
            }
        ) {
            Text("koe details lijst")
        }
        Button(
            onClick = {
                val intent = Intent(
                    context,
                    Login::class.java
                )
                context.startActivity(intent)
            }
        ) {
            Text("com/example/cowmanagementsystem/loginPage")
        }
        Button(
            onClick = {
                val intent = Intent(
                    context,
                    MakeUser::class.java
                )
                context.startActivity(intent)
            }
        ) {
            Text("maak gebruiker")
        }
        Button(
            onClick = {
                val intent = Intent(
                    context,
                    ShowAllUsers::class.java
                )
                context.startActivity(intent)
            }
        ) {
            Text("toon alle gebruiker")
        }
        Button(
            onClick = {
                val intent = Intent(
                    context,
                    UserDetails::class.java
                )
                context.startActivity(intent)
            }
        ) {
            Text("user gegevens alle gebruiker")
        }
    }
}


@Composable
fun GenerateButton(resourcesField: SnapshotStateMap<String, String>, name: String) {
    val context = LocalContext.current

    val database = remember {
        DatabaseHelper(context)
    }



    Column(

    )
    {
        Button(
            onClick = {
              for (value in resourcesField){
                  if(value == null){
                      Log.d("value null:", "$value: ")

                  }
                  Log.d("value:", "$value: ")

              }
              database.addItem(resourcesField, "com/example/cowmanagementsystem/users" )
            } ) {
            Text(
                name
            )

        }

    }

    }



