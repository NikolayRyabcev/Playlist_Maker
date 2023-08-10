package com.example.playlistmaker.UI.search.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.App.App
import com.example.playlistmaker.R
import com.example.playlistmaker.UI.player.activity.PlayerActivity
import com.example.playlistmaker.UI.search.view_model.TrackAdapterAndViewHolder.TrackAdapter
import com.example.playlistmaker.UI.search.view_model_for_activity.SearchViewModel
import com.example.playlistmaker.UI.search.view_model_for_activity.screen_states.SearchScreenState
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.domain.search.models.Track

const val SEARCH_SHARED_PREFS_KEY = "123"

class SearchActivity : ComponentActivity() {
    private lateinit var binding: ActivitySearchBinding

    // viewModel:
    private val searchViewModel by viewModels<SearchViewModel> { SearchViewModel.getViewModelFactory() }
    private var isClickAllowed = true

    private lateinit var trackAdapter: TrackAdapter
    private lateinit var historyAdapter: TrackAdapter
    private lateinit var historyRecycler: RecyclerView

    lateinit var recyclerView: RecyclerView

    private val handler = Handler(Looper.getMainLooper())


    private var isEnterPressed: Boolean = false

    private val KEY_TEXT = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        historyRecycler = binding.historyRecycler

        //делаем ViewModel
        searchViewModel.getStateLiveData().observe(this) { stateLiveData ->

            when (val state = stateLiveData) {
                is SearchScreenState.DefaultSearch -> defaultSearch()
                is SearchScreenState.ConnectionError -> connectionError()
                is SearchScreenState.Loading -> loading()
                is SearchScreenState.NothingFound -> nothingFound()
                is SearchScreenState.SearchIsOk -> {
                    searchIsOk(state.data)
                }

                is SearchScreenState.SearchWithHistory -> searchWithHistory()
                else -> {}
            }
        }

        //кнопка назад
        binding.backButtonArrow.setOnClickListener {
            finish()
        }

        // ввод строки поиска и обработка кнопок
        onEditorFocus()
        onSearchTextChange()
        onClearIconClick()
        clearIconVisibilityChanger()
        startSearchByEnterPress()

        //поиск
        trackAdapter = TrackAdapter() {
            if (clickDebounce()) {
                clickAdapting(it)
            }
        }

        recyclerView = binding.trackRecycler
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = trackAdapter

        //история
        historyAdapter = TrackAdapter() {
            if (clickDebounce()) {
                clickAdapting(it)
            }
        }

        historyRecycler.layoutManager = LinearLayoutManager(this)
        historyRecycler.adapter = historyAdapter
        binding.clearHistoryButton.setOnClickListener {
            historyInVisible()
            searchViewModel.clearHistory()
        }
    }

    //сохраняем текст при повороте экрана
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val inputEditText = findViewById<EditText>(R.id.searchUserText)
        outState.putString(KEY_TEXT, inputEditText.text.toString())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        savedInstanceState.getString(KEY_TEXT, "")
    }

    //включаем кликдебаунсер
    override fun onResume() {
        super.onResume()
        isClickAllowed = true
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    private fun clickAdapting(item: Track) {
        searchViewModel.addItem(item)
        val intent = Intent(this, PlayerActivity::class.java)
        intent.putExtra("track", item)
        this.startActivity(intent)
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
        searchViewModel.searchRequesting(searchText)
    }


    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY_MILLIS)
    }

    private val searchRunnable = Runnable {
        search()
       // trackAdapter.notifyDataSetChanged()
    }

    //если фокус на поле ввода поиска
    fun onEditorFocus() {
        binding.searchUserText.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus && binding.searchUserText.text.isEmpty() && App.trackHistoryList.isNotEmpty()) {
                binding.historyTextView.visibility = VISIBLE
                binding.historyRecycler.visibility = VISIBLE
            } else {
                historyInVisible()
            }
        }
    }
//поиски
    var searchText=""
    // когда меняется текст в поисковой строке
    fun onSearchTextChange() {
        binding.searchUserText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (binding.searchUserText.hasFocus() && p0?.isEmpty() == true && App.trackHistoryList.isNotEmpty()) {
                    binding.historyTextView.visibility = VISIBLE
                    binding.historyRecycler.visibility = VISIBLE
                } else {
                    historyInVisible()
                }
                if (!binding.searchUserText.text.isNullOrEmpty()) {
                    searchText=binding.searchUserText.text.toString()
                    searchDebounce()
                }
            }
            override fun afterTextChanged(p0: Editable?) {
            }
        })
    }

    //поиск по нажатию энтер на клавиатуре
    private fun startSearchByEnterPress() {
        binding.searchUserText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (binding.searchUserText.text.isNotEmpty()) {
                    searchText=binding.searchUserText.text.toString()
                    search()
                    trackAdapter.notifyDataSetChanged()
                    isEnterPressed = true
                    handler.postDelayed({ isEnterPressed = false }, 3000L)
                }
                true
            }
            false
        }
    }

    //очистить строку ввода
    fun onClearIconClick() {
        binding.clearIcon.setOnClickListener {
            binding.searchUserText.setText("")
            val keyboard = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            keyboard.hideSoftInputFromWindow(
                binding.searchUserText.windowToken,
                0
            ) // скрыть клавиатуру
            binding.searchUserText.clearFocus()
            searchViewModel.clearTrackList()
            binding.progressBar.visibility = GONE
            recyclerView.visibility = GONE
            binding.nothingfoundPict.visibility = GONE
            binding.nothingfoundText.visibility = GONE
            binding.loadingproblem.visibility = GONE
            binding.loadingproblemText.visibility = GONE
            binding.refreshButton.visibility = GONE
        }
    }

    //включает видимость кнопки очистки строки ввода, когда есть какой-либо текст
    fun clearIconVisibilityChanger() {
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
        recyclerView.visibility = GONE
        binding.nothingfoundPict.visibility = GONE
        binding.nothingfoundText.visibility = GONE
        binding.loadingproblem.visibility = GONE
        binding.loadingproblemText.visibility = GONE
        binding.refreshButton.visibility = GONE
    }

    private fun loading() {
        binding.progressBar.visibility = VISIBLE
        historyInVisible()
        recyclerView.visibility = GONE
        binding.nothingfoundPict.visibility = GONE
        binding.nothingfoundText.visibility = GONE
        binding.loadingproblem.visibility = GONE
        binding.loadingproblemText.visibility = GONE
        binding.refreshButton.visibility = GONE

    }

    private fun searchIsOk(data: List<Track>) {
        binding.progressBar.visibility = GONE
        recyclerView.visibility = VISIBLE
        binding.nothingfoundPict.visibility = GONE
        binding.nothingfoundText.visibility = GONE
        binding.loadingproblem.visibility = GONE
        binding.loadingproblemText.visibility = GONE
        binding.refreshButton.visibility = GONE
        trackAdapter.setItems(data)
        binding.clearHistoryButton.visibility - GONE
        historyInVisible()
    }

    private fun nothingFound() {
        binding.historyTextView.visibility = View.GONE
        historyRecycler.visibility = View.GONE
        binding.clearHistoryButton.visibility = VISIBLE
        recyclerView.visibility = GONE
        binding.nothingfoundPict.visibility = VISIBLE
        binding.nothingfoundText.visibility = VISIBLE
        binding.loadingproblem.visibility = GONE
        binding.loadingproblemText.visibility = GONE
        binding.refreshButton.visibility = GONE
        historyInVisible()
    }

    private fun connectionError() {
        binding.loadingproblem.visibility = VISIBLE
        binding.loadingproblemText.visibility = VISIBLE
        binding.refreshButton.visibility = VISIBLE
        recyclerView.visibility = GONE
        binding.refreshButton.setOnClickListener { search() }
        binding.progressBar.visibility = GONE
        historyInVisible()
    }

    private fun searchWithHistory() {
        binding.historyTextView.visibility = VISIBLE
        binding.historyRecycler.visibility = VISIBLE
        recyclerView.visibility = GONE
        binding.nothingfoundPict.visibility = GONE
        binding.nothingfoundText.visibility = GONE
        binding.refreshButton.visibility = GONE
        binding.loadingproblem.visibility = GONE
        binding.loadingproblemText.visibility = GONE
    }

    private fun historyInVisible() {
        binding.historyTextView.visibility = GONE
        historyRecycler.visibility = GONE
        binding.clearHistoryButton.visibility = GONE
    }


    companion object {
        private const val SEARCH_DEBOUNCE_DELAY_MILLIS = 2000L
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}