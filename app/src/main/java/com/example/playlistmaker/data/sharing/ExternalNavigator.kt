package com.example.playlistmaker.data.sharing

interface ExternalNavigator {
    fun shareLink(shareAppLink: String)

    fun openLink (termsLink:String)

    fun openEmail (adminEmailData: EmailData)

    fun setShareLink ():String
}