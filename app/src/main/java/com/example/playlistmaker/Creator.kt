package com.example.playlistmaker

import com.example.playlistmaker.domain.api.PlayerInteractor
import com.example.playlistmaker.domain.impl.PlayerInteractorImpl

object Creator {
    fun providePlayerInteractor(): PlayerInteractor
    {
        return PlayerInteractorImpl(getPlayerRepository)
    }

}