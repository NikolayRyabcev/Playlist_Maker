package com.example.playlistmaker.domain.sharing


class SharingInteractorImpl(
    private var externalNavigator: ExternalNavigator,
) :SharingInteractor {

    override fun shareApp() {
        externalNavigator.shareLink(getShareAppLink())
    }

    override fun openTerms() {
        externalNavigator.openLink()
    }

    override fun openSupport() {
        externalNavigator.openEmail()
    }

    private fun getShareAppLink(): String {

        return externalNavigator.getShareLink ()
    }
}