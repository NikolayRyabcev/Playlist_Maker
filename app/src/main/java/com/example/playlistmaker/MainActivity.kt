package com.example.playlistmaker

import android.annotation.SuppressLint
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
        val butt3 = findViewById<Button>(R.id.button3)

        val imageClickListener: View.OnClickListener = object : View.OnClickListener {
            override fun onClick(v: View?) {
                Toast.makeText(this@MainActivity, "Нажали на кнопку!", Toast.LENGTH_SHORT).show()
            }
        }
        val butt2 = findViewById<Button>(R.id.button2)

        butt2.setOnClickListener {
            Toast.makeText(this@MainActivity, "Нажали на кнопку!", Toast.LENGTH_SHORT).show()
        }
        butt1.setOnClickListener(imageClickListener)
        butt2.setOnClickListener(imageClickListener)
        butt3.setOnClickListener(imageClickListener)
    }
}