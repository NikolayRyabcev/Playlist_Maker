package com.example.playlistmaker.data.sharing

import android.content.Intent
import com.example.playlistmaker.R


class ExternalNavigatorImpl:ExternalNavigator {
    override fun shareLink(shareAppLink: String) {
        val intentSend = Intent(Intent.ACTION_SEND)
        intentSend.type = "text/plain"
        intentSend.putExtra(Intent.EXTRA_TEXT, getString(R.string.PractAdr))
        startActivity(intentSend)
    }

    override fun openLink(termsLink: String) {
        TODO("Not yet implemented")
    }

    override fun openEmail(adminEmailData: EmailData) {
        TODO("Not yet implemented")
    }
}