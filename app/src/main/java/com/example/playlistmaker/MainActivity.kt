package com.example.playlistmaker

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val butt1 = findViewById<Button>(R.id.button1)
        val butt2 = findViewById<Button>(R.id.button2)
        val butt3 = findViewById<Button>(R.id.button3)

        butt1.setOnClickListener {
            val butt1Intent = Intent(this, Search::class.java)
            startActivity(butt1Intent)
        }
        butt2.setOnClickListener {
            val butt2Intent = Intent(this, MediaLibrary::class.java)
            startActivity(butt2Intent)
        }
        butt3.setOnClickListener {
            val butt3Intent = Intent(this, SettingsActivity::class.java)
            startActivity(butt3Intent)
        }

    }
}