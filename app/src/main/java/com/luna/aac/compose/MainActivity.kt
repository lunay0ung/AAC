package com.luna.aac.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.luna.aac.R
import com.luna.aac.ui.theme.AACTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AACTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    SetHomeButton()
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "ddddd $name!")
}

@Preview
@Composable
fun SetHomeButton() {
    var value by remember { mutableStateOf("Hello") }

    LazyRow(
        modifier = Modifier.fillMaxSize(),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        items(1) {
            IconButton(modifier = Modifier.then(
                Modifier
                    .size(50.dp)
            ),
                onClick = { }) {
                Image(
                    painter = painterResource(id = R.drawable.woman),
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )
            }
            IconButton(modifier = Modifier.then(
                Modifier
                    .size(50.dp)
            ),
                onClick = { }) {
                Image(
                    painter = painterResource(id = R.drawable.teacher_),
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )
            }
            IconButton(modifier = Modifier.then(
                Modifier
                    .size(50.dp)
            ),
                onClick = { }) {
                Image(
                    painter = painterResource(id = R.drawable.help),
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )
            }
            TextField(
                value = value,
                onValueChange = { value = it },
                label = { Text("Enter text") },
                maxLines = 1,
                textStyle = TextStyle(color = Color.Black, fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(20.dp)
            )

            IconButton(
                modifier = Modifier.then(
                    Modifier
                        .size(50.dp)
                ),
                onClick = { }) {
                Image(
                    painter = painterResource(id = R.drawable.home),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    AACTheme {
        Greeting("Android")
    }
}