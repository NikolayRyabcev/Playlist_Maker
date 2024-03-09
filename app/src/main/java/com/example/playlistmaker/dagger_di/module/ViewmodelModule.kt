package com.example.playlistmaker.dagger_di.module

import androidx.lifecycle.ViewModel
import com.example.playlistmaker.dagger_di.annotations.ViewModelKey
import com.example.playlistmaker.ui.PlaylistScreen.PlaylistScreenViewModel
import com.example.playlistmaker.ui.mediaLibrary.viewModels.FavouritesViewModel
import com.example.playlistmaker.ui.player.view_model.PlayerViewModel
import com.example.playlistmaker.ui.search.viewModel.SearchViewModel
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

    @IntoMap
    @ViewModelKey(FavouritesViewModel::class)
    @Binds
    fun bindFavoutitesViewModel(favouritesViewModel: FavouritesViewModel): ViewModel

    @IntoMap
    @ViewModelKey(PlayerViewModel::class)
    @Binds
    fun bindPlayerViewModel(playerViewModel: PlayerViewModel): ViewModel

    @IntoMap
    @ViewModelKey(PlaylistScreenViewModel::class)
    @Binds
    fun bindPlaylistViewModel(playlistScreenViewModel: PlaylistScreenViewModel): ViewModel

    @IntoMap
    @ViewModelKey(SearchViewModel::class)
    @Binds
    fun bindSearchViewModel(searchViewModel: SearchViewModel): ViewModel

}