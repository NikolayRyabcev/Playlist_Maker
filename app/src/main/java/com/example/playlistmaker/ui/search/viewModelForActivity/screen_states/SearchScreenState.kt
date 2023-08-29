package com.example.playlistmaker.ui.search.viewModelForActivity.screen_states

import com.example.playlistmaker.domain.search.models.Track

sealed class SearchScreenState {
    object DefaultSearch : SearchScreenState()
    object Loading : SearchScreenState()
    object NothingFound : SearchScreenState()
    object ConnectionError : SearchScreenState()
    data class SearchWithHistory(var historyData: List<Track>) : SearchScreenState()
    data class SearchIsOk(val data: List<Track>) : SearchScreenState()
}