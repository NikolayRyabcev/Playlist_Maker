package com.example.playlistmaker.domain.sharing


import com.example.playlistmaker.Creator.Creator
import com.example.playlistmaker.data.sharing.ExternalNavigator

class SharingInteractorImpl(
    private var externalNavigator: ExternalNavigator,
) :SharingInteractor {

    override fun shareApp() {
        externalNavigator.shareLink(getShareAppLink())
    }

    override fun openTerms() {
        externalNavigator.openLink(getTermsLink())
    }

    override fun openSupport() {
        /*externalNavigator.openEmail(getSupportEmailData())*/
    }

    private fun getShareAppLink(): String {
        externalNavigator = Creator.provideExternalNavigator()
        return externalNavigator.setShareLink ()
    }

    private fun getSupportEmailData() {
        // Нужно реализовать

    }

    private fun getTermsLink(): String {
        // Нужно реализовать
        return ""
    }
}