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
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.App.App
import com.example.playlistmaker.R
import com.example.playlistmaker.UI.player.activity.PlayerActivity
import com.example.playlistmaker.UI.search.view_model.TrackAdapterAndViewHolder.TrackAdapter
import com.example.playlistmaker.UI.search.view_model_for_activity.SearchViewModel
import com.example.playlistmaker.UI.search.view_model_for_activity.screen_states.SearchScreenState
import com.example.playlistmaker.UI.settings.view_model.SettingsViewModel
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.domain.search.models.Track

const val SEARCH_SHARED_PREFS_KEY = "123"

class SearchActivity : ComponentActivity() {
    private lateinit var binding: ActivitySearchBinding

    // viewModel:
    private val searchViewModel by viewModels<SearchViewModel> { SearchViewModel.getViewModelFactory() }
    private var isClickAllowed = true

    lateinit var trackAdapter: TrackAdapter
    private lateinit var historyAdapter: TrackAdapter
    private lateinit var historyView: TextView
    private lateinit var historyRecycler: RecyclerView

    lateinit var recyclerView: RecyclerView

    private val handler = Handler(Looper.getMainLooper())

    private val searchRunnable = Runnable { search(binding.searchUserText) }

    var isEnterPressed: Boolean = false

    private val KEY_TEXT = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //делаем ViewModel
        searchViewModel.getStateLiveData().observe(this) { stateLiveData ->
            when (stateLiveData) {
                is SearchScreenState.DefaultSearch -> defaultSearch()
                is SearchScreenState.ConnectionError -> connectionError()
                is SearchScreenState.Loading -> loading()
                is SearchScreenState.NothingFound -> nothingFound()
                is SearchScreenState.SearchIsOk -> searchIsOk()
                is SearchScreenState.SearchWithHistory -> searchWithHistory()
            }
        }

        //кнопка назад
        binding.backButtonArrow.setOnClickListener {
            finish()
        }

        // ввод строки поиска
        onEditorFocus()
        onSearchTextChange()
        onClearIconClick()
        clearIconVisibilityChanger()
        startSearchByEnterPress()

        //поиск

        trackAdapter = TrackAdapter(searchViewModel.searchResults()) {
            if (clickDebounce()) {
                clickAdapting(it)
            }
        }

        recyclerView = findViewById(R.id.trackRecycler)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = trackAdapter

        //история
        historyAdapter = TrackAdapter(searchHistoryObj.trackHistoryList) {
            if (clickDebounce()) {
                clickAdapting(it)
                historyAdapter.notifyDataSetChanged()
            }
        }

        historyRecycler.layoutManager = LinearLayoutManager(this)
        historyRecycler.adapter = historyAdapter
        binding.clearHistoryButton.setOnClickListener {
            App.trackHistoryList.clear()
            historyInVisible()
            trackAdapter.tracks.clear()
            trackAdapter.notifyDataSetChanged()
            historyAdapter.notifyDataSetChanged()
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
        val intent = Intent(this, PlayerActivity::class.java)
        intent.putExtra("Track", item)
        this.startActivity(intent)
        searchHistoryObj.editArray(item)
    }

    //видимость кнопки удаления введенной строки (крестик)
    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            GONE
        } else {
            View.VISIBLE
        }
    }

    private fun search(inputEditText: EditText) {

    }

    private fun defaultSearch() {

    }

    private fun loading() {

    }

    private fun searchIsOk() {
        progressBar.visibility = GONE
        recyclerView.visibility = View.VISIBLE
        binding.nothingfoundPict.visibility = GONE
        binding.nothingfoundText.visibility = GONE
        binding.loadingproblem.visibility = GONE
        binding.loadingproblemText.visibility = GONE
        binding.refreshButton.visibility = GONE
    }

    private fun nothingFound() {
        historyView.visibility = View.VISIBLE
        historyRecycler.visibility = View.VISIBLE
        binding.clearHistoryButton.visibility = View.VISIBLE
        recyclerView.visibility = GONE
        binding.nothingfoundPict.visibility = GONE
        binding.nothingfoundText.visibility = GONE
        binding.loadingproblem.visibility = GONE
        binding.loadingproblemText.visibility = GONE
        binding.refreshButton.visibility = GONE
    }

    private fun connectionError() {
        binding.loadingproblem.visibility = View.VISIBLE
        binding.loadingproblemText.visibility = View.VISIBLE
        binding.refreshButton.visibility = View.VISIBLE
        recyclerView.visibility = GONE
        binding.refreshButton.setOnClickListener { search(inputEditText) }
        progressBar.visibility = GONE
    }

    private fun searchWithHistory() {

    }

    private fun historyInVisible() {
        historyView.visibility = GONE
        historyRecycler.visibility = GONE
        binding.clearHistoryButton.visibility = GONE
    }

    /* private fun searchDebounce() {
         handler.removeCallbacks(searchRunnable)
         handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY_MILLIS)
     }*/

    //если фокус на поле ввода поиска
    fun onEditorFocus() {
        binding.searchUserText.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus && binding.searchUserText.text.isEmpty() && App.trackHistoryList.isNotEmpty()) {
                historyVisible()
            } else {
                historyInVisible()
            }
        }
    }

    // когда меняется текст в поисковой строке
    fun onSearchTextChange() {
        binding.searchUserText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (binding.searchUserText.hasFocus() && p0?.isEmpty() == true && App.trackHistoryList.isNotEmpty()) {
                    historyVisible()

                } else {
                    historyInVisible()
                }
                if (!binding.searchUserText.text.isNullOrEmpty()) {
                    recyclerView.visibility = GONE
                    // searchDebounce()
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
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
            trackList.clear()
            ifSearchOkVisibility()
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

    //поиск по нажатию энтер на клавиатуре
    fun startSearchByEnterPress() {
    binding.searchUserText.setOnEditorActionListener { _, actionId, _ ->
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            if (binding.searchUserText.text.isNotEmpty()) {
                recyclerView.visibility = GONE
                search(binding.searchUserText)
                isEnterPressed = true
                handler.postDelayed({ isEnterPressed = false }, 3000L)
            }
            true
        }
        false
    }}


    companion object {
        private const val SEARCH_DEBOUNCE_DELAY_MILLIS = 2000L
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}