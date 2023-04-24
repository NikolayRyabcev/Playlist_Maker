package com.example.playlistmaker


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        val arrowButton = findViewById<ImageView>(R.id.buttonArrow)
        arrowButton.setOnClickListener {
            finish()
        }
        val textView = findViewById<FrameLayout>(R.id.ShareAppText)
        textView.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.PractAdr))
            startActivity(intent)
        }
        val textView2 = findViewById<FrameLayout>(R.id.WriteSupportText)
        textView2.setOnClickListener {
            val intent2 = Intent(Intent.ACTION_SENDTO)
            intent2.data = Uri.parse("mailto:")
            val email = getString(R.string.myemail)
            intent2.putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
            intent2.putExtra(Intent.EXTRA_TEXT, getString(R.string.ShareText))
            startActivity(intent2)
        }
        val textView3 = findViewById<FrameLayout>(R.id.AgreementText)
        textView3.setOnClickListener {
            val intent3 = Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.AgreementURL)))
            startActivity(intent3)
        }
    }
}