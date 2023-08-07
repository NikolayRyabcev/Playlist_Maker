package com.example.playlistmaker.UI.search.view_model_for_activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.Creator.Creator
import com.example.playlistmaker.domain.search.searching_and_responding.SearchInteractor

class SearchViewModel (private var searchInteractor: SearchInteractor) : ViewModel() {
   // init {
   //     searchInteractor = Creator.provideSearchInteractor()
   // }
    fun setDefault (){}
    fun onFocusSearcher(){}
    fun searchResults(){}
    fun searchHistory(){}


    companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return SearchViewModel(Creator.provideSearchInteractor()
                    ) as T
                }
            }
    }
}