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
fun GenerateInputs(modifier: Modifier, showFields: Array<String>){
//    val numberOfFields = fields.size
    val fields = listOf("userName", "password")
    val resourcesField = remember { mutableStateMapOf(
            fields[0] to "",
            fields[1] to ""
        )}

//    var numberOfLoops = 0
//    while (numberOfLoops < numberOfFields - 1){
//        resourcesField.add("")
//        numberOfLoops++
//    }

    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,


    ) {
        for ( i in fields.indices) {
            OutlinedTextField(
                value = resourcesField[fields[i]] ?: "" ,
                onValueChange = { resourcesField[fields[i]] = it },
                label = { Text(showFields[i]) },


            )
        }
        GenerateButton(resourcesField, "Login")
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
              database.voegNaamToe(resourcesField, "users" )
            } ) {
            Text(
                name
            )

        }

    }

    }



