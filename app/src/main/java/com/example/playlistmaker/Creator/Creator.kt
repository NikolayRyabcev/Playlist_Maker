package com.example.playlistmaker.Creator

import com.example.playlistmaker.data.player.PlayerRepositoryImpl
import com.example.playlistmaker.domain.player.PlayerInteractor
import com.example.playlistmaker.data.player.PlayerRepository
import com.example.playlistmaker.domain.player.PlayerInteractorImpl

object Creator {
    fun providePlayerInteractor(): PlayerInteractor {
        return PlayerInteractorImpl()
    }
    fun providePlayerRepository(): PlayerRepository {
        return PlayerRepositoryImpl()
    }
}