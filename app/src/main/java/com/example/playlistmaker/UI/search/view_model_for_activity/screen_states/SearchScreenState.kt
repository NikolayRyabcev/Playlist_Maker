package com.example.playlistmaker.UI.search.view_model_for_activity.screen_states

sealed class SearchScreenState {
    class DefaultSearch() : SearchScreenState()
    class Loading(): SearchScreenState()
    class SearchIsOk(): SearchScreenState()
    class NothingFound(): SearchScreenState()
    class ConnectionError(): SearchScreenState()
}