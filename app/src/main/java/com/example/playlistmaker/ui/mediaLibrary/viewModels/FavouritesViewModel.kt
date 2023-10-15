package com.example.playlistmaker.ui.mediaLibrary.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.favourites.FavouritesInteractor
import com.example.playlistmaker.domain.search.history.SearchHistoryInteractor
import com.example.playlistmaker.domain.search.models.Track
import kotlinx.coroutines.launch

class FavouritesViewModel (
    private val favouritesInteractor: FavouritesInteractor,
    private val searchHistoryInteractor :SearchHistoryInteractor
) : ViewModel() {

    var trackResultList: MutableLiveData<List<Track>?> = MutableLiveData<List<Track>?>()

    fun favouritesMaker() {
        viewModelScope.launch {
            favouritesInteractor.favouritesGet()
                .collect { trackList ->
                    if (trackList.isNullOrEmpty()) {
                        trackResultList.postValue(trackList)
                    } else trackResultList.postValue(emptyList())
                }
        }
    }

    fun addItem(item: Track) {
        searchHistoryInteractor.addItem(item)
    }
}