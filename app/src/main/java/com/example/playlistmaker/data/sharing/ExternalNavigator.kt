package com.example.playlistmaker.data.sharing

interface ExternalNavigator {
    fun shareLink(shareAppLink: String)

    fun openLink ()

    fun openEmail ()

    fun getShareLink ():String

}