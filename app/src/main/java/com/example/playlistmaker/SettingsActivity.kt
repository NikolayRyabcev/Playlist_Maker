package com.example.playlistmaker


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val buttArrow = findViewById<ImageView>(R.id.buttonArrow)
        buttArrow.setOnClickListener {
            val buttArrowIntent = Intent(this, MainActivity::class.java)
            startActivity(buttArrowIntent)
        }
    }
}