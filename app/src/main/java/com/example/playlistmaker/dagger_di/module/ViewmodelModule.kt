package com.example.playlistmaker.dagger_di.module

import androidx.lifecycle.ViewModel
import com.example.playlistmaker.dagger_di.annotations.ViewModelKey
import com.example.playlistmaker.ui.settings.view_model.SettingsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewmodelModule {
    @IntoMap
    @ViewModelKey(SettingsViewModel::class)
    @Binds
    fun bindSettingsViewModel(settingsViewModel: SettingsViewModel): ViewModel


}