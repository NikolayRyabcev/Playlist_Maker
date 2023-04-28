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
        val textSendView = findViewById<FrameLayout>(R.id.ShareAppText)
        textSendView.setOnClickListener {
            val intentSend = Intent(Intent.ACTION_SEND)
            intentSend.type = "text/plain"
            intentSend.putExtra(Intent.EXTRA_TEXT, getString(R.string.PractAdr))
            startActivity(intentSend)
        }
        val textSendToView2 = findViewById<FrameLayout>(R.id.WriteSupportText)
        textSendToView2.setOnClickListener {
            val intentSendTo2 = Intent(Intent.ACTION_SENDTO)
            intentSendTo2.data = Uri.parse("mailto:")
            val email = getString(R.string.myemail)
            intentSendTo2.putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
            intentSendTo2.putExtra(Intent.EXTRA_TEXT, getString(R.string.ShareText))
            startActivity(intentSendTo2)
        }
        val textAgreementView3 = findViewById<FrameLayout>(R.id.AgreementText)
        textAgreementView3.setOnClickListener {
            val intentAgreement3 = Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.AgreementURL)))
            startActivity(intentAgreement3)
        }
    }
}