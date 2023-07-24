package com.example.playlistmaker

import com.example.playlistmaker.data.dto.PlayerRepositoryImpl
import com.example.playlistmaker.domain.api.PlayerInteractor
import com.example.playlistmaker.domain.api.PlayerRepository
import com.example.playlistmaker.domain.api.TimeInteractor
import com.example.playlistmaker.domain.impl.PlayerInteractorImpl
import com.example.playlistmaker.domain.impl.TimeInteractorImpl

object Creator {
    fun providePlayerInteractor(): PlayerInteractor {
        return PlayerInteractorImpl()
    }
    fun providePlayerRepository():PlayerRepository{
        return PlayerRepositoryImpl(providePlayerInteractor())
    }
   fun provideTimeInteractor(): TimeInteractor {
       return TimeInteractorImpl(providePlayerRepository())
   }
}