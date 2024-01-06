package com.example.playlistmaker.ui.root

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

class RootActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Text("Hello world!")
        }
    }
}

@Composable
fun MyComposable() {
    MaterialTheme {
        Surface {
            Text(text = "Hello, Compose!")
        }
    }
}

@Preview
@Composable
fun PreviewMyComposable() {
    MyComposable()
}