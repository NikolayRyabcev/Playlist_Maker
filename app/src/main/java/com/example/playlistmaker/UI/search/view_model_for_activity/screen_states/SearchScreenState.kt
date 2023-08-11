package com.example.playlistmaker.UI.search.view_model_for_activity.screen_states

import com.example.playlistmaker.domain.search.models.Track

sealed class SearchScreenState {
    object DefaultSearch                                 : SearchScreenState()
    object Loading                                       : SearchScreenState()
    object NothingFound                                  : SearchScreenState()
    object ConnectionError                               : SearchScreenState()
    data class SearchWithHistory(val historyData: List<Track>)  : SearchScreenState()
    data class SearchIsOk(val data: List<Track>)         : SearchScreenState()
}