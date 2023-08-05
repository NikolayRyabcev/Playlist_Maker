package com.example.playlistmaker.data.sharing

import android.content.Intent
import android.net.Uri
import com.example.playlistmaker.R
import com.example.playlistmaker.App.App


class ExternalNavigatorImpl(private val application: App):ExternalNavigator {

    //val app = App()

    override fun shareLink(shareAppLink: String) {

        val intentSend = Intent(Intent.ACTION_SEND)
        intentSend.type = "text/plain"
        intentSend.putExtra(Intent.EXTRA_TEXT, application.getString(R.string.PractAdr))
        intentSend.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        application.startActivity(intentSend)
    }

    override fun openLink() {
        val intentAgreement3 =
            Intent(Intent.ACTION_VIEW, Uri.parse(application.getString(R.string.AgreementURL)))
        intentAgreement3.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        application.startActivity(intentAgreement3)
    }

    override fun openEmail() {
        val intentSendTo2 = Intent(Intent.ACTION_SENDTO)
        intentSendTo2.data = Uri.parse("mailto:")
        val email = application.getString(R.string.myemail)
        intentSendTo2.putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
        intentSendTo2.putExtra(Intent.EXTRA_TEXT, application.getString(R.string.ShareText))
        intentSendTo2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        application.startActivity(intentSendTo2)
    }
    override fun getShareLink ():String {
        return "https://practicum.yandex.ru/profile/android-developer"
    }

}