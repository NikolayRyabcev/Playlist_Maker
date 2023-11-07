package com.example.playlistmaker.ui.search.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentSearchBinding
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.ui.player.fargment.PlayerFragment
import com.example.playlistmaker.ui.search.adapter.TrackAdapter
import com.example.playlistmaker.ui.search.viewModel.SearchViewModel
import com.example.playlistmaker.ui.search.viewModel.screen_states.SearchScreenState
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding

    // viewModel:
    private val searchViewModel by viewModel<SearchViewModel>()
    private var isClickAllowed = true

    private lateinit var trackAdapter: TrackAdapter
    private lateinit var historyAdapter: TrackAdapter

    private lateinit var bottomNavigator: BottomNavigationView
    private val KEY_TEXT = ""

    //переменные для введения корутин:
    private var latestSearchText: String? = null

    private var searchJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bottomNavigator = requireActivity().findViewById(R.id.bottomNavigationView)

        //делаем ViewModel
        searchViewModel.getStateLiveData().observe(viewLifecycleOwner) { stateLiveData ->
            when (stateLiveData) {
                is SearchScreenState.DefaultSearch -> defaultSearch()
                is SearchScreenState.ConnectionError -> connectionError()
                is SearchScreenState.Loading -> loading()
                is SearchScreenState.NothingFound -> nothingFound()
                is SearchScreenState.SearchIsOk -> searchIsOk(stateLiveData.data)
                is SearchScreenState.SearchWithHistory -> searchWithHistory(stateLiveData.historyData)
                else -> {}
            }
        }

        // ввод строки поиска и обработка кнопок
        onEditorFocus()
        onSearchTextChange()
        onClearIconClick()
        clearIconVisibilityChanger()
        startSearchByEnterPress()

        //поиск
        trackAdapter = TrackAdapter {
            if (isClickAllowed) {
                clickAdapting(it)
            }
        }

        binding.trackRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.trackRecycler.adapter = trackAdapter

        //история
        historyAdapter = TrackAdapter {
            if (isClickAllowed) {
                clickAdapting(it)
            }
        }

        binding.historyRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.historyRecycler.adapter = historyAdapter
        binding.clearHistoryButton.setOnClickListener {
            historyInVisible()
            searchViewModel.clearHistory()
        }
        clickDebounceManager()
    }

    //сохраняем текст при повороте экрана
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val inputEditText = binding.searchUserText
        outState.putString(KEY_TEXT, inputEditText.text.toString())
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if (savedInstanceState != null) {
            val savedText = savedInstanceState.getString(KEY_TEXT, "")
            binding.searchUserText.setText(savedText)
        }
    }

    //восстанавливаем кликдебаунсер
    override fun onResume() {
        super.onResume()
        isClickAllowed = true
    }

    //включаем кликдебаунсер
    private fun clickDebounceManager() {
        GlobalScope.launch { clickDebouncer() }
    }

    private suspend fun clickDebouncer() {
        isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            delay(CLICK_DEBOUNCE_DELAY)
            isClickAllowed = true
        }
    }

    private fun clickAdapting(item: Track) {
        searchViewModel.addItem(item)
        val bundle = Bundle()
        bundle.putParcelable("track", item)
        val navController = findNavController()
        navController.navigate(R.id.action_searchFragment_to_playerFragment, bundle)
    }

    //видимость кнопки удаления введенной строки (крестик)
    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            GONE
        } else {
            VISIBLE
        }
    }

    //поиск
    private fun search() {
        searchViewModel.searchRequesting(binding.searchUserText.text.toString())
    }

    // с корутиной
    private fun searchDebounce() {
        val changedText = binding.searchUserText.text.toString()
        if (latestSearchText == changedText) return
        latestSearchText = changedText
        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
            delay(SEARCH_DEBOUNCE_DELAY_MILLIS)
            search()
        }
    }

    //если фокус на поле ввода поиска
    private fun onEditorFocus() {
        binding.searchUserText.setOnFocusChangeListener { view, hasFocus ->
            run {
                if (hasFocus && binding.searchUserText.text.isEmpty() && searchViewModel.provideHistory().value?.isNotEmpty() ?: false) {
                    searchViewModel.clearTrackList()

                } else {

                }
                //if (hasFocus) bottomNavigator.visibility = GONE else bottomNavigator.visibility =
                //    VISIBLE
            }
        }
    }

    //поиски


    // когда меняется текст в поисковой строке
    private fun onSearchTextChange() {
        binding.searchUserText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (binding.searchUserText.hasFocus() && p0?.isEmpty() == true && searchViewModel.provideHistory().value?.isNotEmpty() ?: false) {
                    searchViewModel.clearTrackList()
                } else {
                    historyInVisible()
                }
                if (!binding.searchUserText.text.isNullOrEmpty()) searchDebounce()
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
    }

    //поиск по нажатию энтер на клавиатуре
    @SuppressLint("NotifyDataSetChanged")
    private fun startSearchByEnterPress() {
        binding.searchUserText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (binding.searchUserText.text.isNotEmpty()) {
                    bottomNavigator.visibility = VISIBLE
                    searchDebounce()
                    trackAdapter.notifyDataSetChanged()
                }
                true
            }
            false
        }
    }

    //очистить строку ввода
    private fun onClearIconClick() {
        binding.clearIcon.setOnClickListener {
            binding.searchUserText.setText("")
            val keyboard =
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            keyboard.hideSoftInputFromWindow(
                binding.searchUserText.windowToken,
                0
            ) // скрыть клавиатуру
            binding.searchUserText.clearFocus()
            searchViewModel.clearTrackList()
        }
    }

    //включает видимость кнопки очистки строки ввода, когда есть какой-либо текст
    private fun clearIconVisibilityChanger() {
        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.clearIcon.visibility = clearButtonVisibility(s)
            }

            override fun afterTextChanged(s: Editable?) {
                // empty
            }
        }
        binding.searchUserText.addTextChangedListener(simpleTextWatcher)
    }

    //состояния экранов
    private fun defaultSearch() {
        historyInVisible()
        binding.trackRecycler.visibility = GONE
        binding.nothingfoundPict.visibility = GONE
        binding.nothingfoundText.visibility = GONE
        binding.loadingproblem.visibility = GONE
        binding.loadingproblemText.visibility = GONE
        binding.refreshButton.visibility = GONE
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun loading() {
        binding.progressBar.visibility = VISIBLE
        historyInVisible()
        binding.trackRecycler.visibility = GONE
        binding.nothingfoundPict.visibility = GONE
        binding.nothingfoundText.visibility = GONE
        binding.loadingproblem.visibility = GONE
        binding.loadingproblemText.visibility = GONE
        binding.refreshButton.visibility = GONE
        trackAdapter.notifyDataSetChanged()
        binding.searchBlock.visibility = GONE
    }

    private fun searchIsOk(data: List<Track>) {
        binding.progressBar.visibility = GONE
        binding.trackRecycler.visibility = VISIBLE
        binding.nothingfoundPict.visibility = GONE
        binding.nothingfoundText.visibility = GONE
        binding.loadingproblem.visibility = GONE
        binding.loadingproblemText.visibility = GONE
        binding.refreshButton.visibility = GONE
        trackAdapter.setItems(data)
        binding.clearHistoryButton.visibility - GONE
        historyInVisible()
        binding.searchBlock.visibility = VISIBLE
    }

    private fun nothingFound() {
        binding.historyTextView.visibility = GONE
        binding.historyRecycler.visibility = GONE
        binding.clearHistoryButton.visibility = VISIBLE
        binding.trackRecycler.visibility = GONE
        binding.nothingfoundPict.visibility = VISIBLE
        binding.nothingfoundText.visibility = VISIBLE
        binding.loadingproblem.visibility = GONE
        binding.loadingproblemText.visibility = GONE
        binding.refreshButton.visibility = GONE
        historyInVisible()
        binding.searchBlock.visibility = VISIBLE
    }

    private fun connectionError() {
        binding.loadingproblem.visibility = VISIBLE
        binding.loadingproblemText.visibility = VISIBLE
        binding.refreshButton.visibility = VISIBLE
        binding.trackRecycler.visibility = GONE
        binding.refreshButton.setOnClickListener { search() }
        binding.progressBar.visibility = GONE
        binding.nothingfoundPict.visibility = GONE
        binding.nothingfoundText.visibility = GONE
        historyInVisible()
        binding.searchBlock.visibility = VISIBLE
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun searchWithHistory(historyData: List<Track>) {
        historyAdapter.setItems(historyData)
        historyAdapter.notifyDataSetChanged()
        binding.trackRecycler.visibility = GONE
        if (!historyData.isNullOrEmpty()) {
            binding.historyTextView.visibility = VISIBLE
            binding.historyRecycler.visibility = VISIBLE
            binding.clearHistoryButton.visibility = VISIBLE
        }
        binding.trackRecycler.visibility = GONE
        binding.nothingfoundPict.visibility = GONE
        binding.nothingfoundText.visibility = GONE
        binding.refreshButton.visibility = GONE
        binding.loadingproblem.visibility = GONE
        binding.loadingproblemText.visibility = GONE
        binding.progressBar.visibility = GONE
        binding.searchBlock.visibility = GONE
    }

    private fun historyInVisible() {
        binding.historyTextView.visibility = GONE
        binding.historyRecycler.visibility = GONE
        binding.clearHistoryButton.visibility = GONE
    }


    companion object {
        private const val SEARCH_DEBOUNCE_DELAY_MILLIS = 2000L
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

}