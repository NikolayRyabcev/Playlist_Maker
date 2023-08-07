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
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.App.App
import com.example.playlistmaker.R
import com.example.playlistmaker.UI.player.activity.PlayerActivity
import com.example.playlistmaker.domain.search.SearchHistory
import com.example.playlistmaker.data.search.TrackResponse
import com.example.playlistmaker.data.search.iTunesSearchAPI
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.UI.search.view_model.TrackAdapterAndViewHolder.TrackAdapter
import com.example.playlistmaker.UI.search.view_model_for_activity.SearchViewModel
import com.example.playlistmaker.UI.settings.view_model.SettingsViewModel
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.databinding.PlayerActivityBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Locale

const val SEARCH_SHARED_PREFS_KEY = "123"

class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding
// viewModel:
    private lateinit var searchViewModel:ViewModel
    private var isClickAllowed = true

    lateinit var trackList: ArrayList<Track>

    lateinit var trackAdapter: TrackAdapter
    private lateinit var historyAdapter: TrackAdapter
    private lateinit var historyView: TextView
    private lateinit var historyRecycler: RecyclerView

    lateinit var recyclerView: RecyclerView

    val searchHistoryObj = SearchHistory()
    private val handler = Handler(Looper.getMainLooper())
    //private val searchRunnable = Runnable { search(inputEditText) }
    lateinit var progressBar: ProgressBar

    var isEnterPressed :Boolean = false

    private val KEY_TEXT = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //делаем ViewModel
        searchViewModel = ViewModelProvider(
            this,
            SearchViewModel.getViewModelFactory()
        )[SettingsViewModel::class.java]

        trackList = ArrayList()

        trackAdapter = TrackAdapter(trackList) {
            if (clickDebounce()) {
                clickAdapting(it)
            }
        }
        historyAdapter = TrackAdapter(searchHistoryObj.trackHistoryList) {
            if (clickDebounce()) {
                clickAdapting(it)
                historyAdapter.notifyDataSetChanged()
            }
        }
        recyclerView = findViewById(R.id.trackRecycler)

        applicationContext.getSharedPreferences(SEARCH_SHARED_PREFS_KEY, MODE_PRIVATE)

        historyRecycler.layoutManager = LinearLayoutManager(this)
        historyRecycler.adapter = historyAdapter

        progressBar = findViewById(R.id.progressBar)

        ifSearchOkVisibility()
        historyInVisible()
        binding.searchUserText.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus && binding.searchUserText.text.isEmpty() && App.trackHistoryList.isNotEmpty()) {
                historyVisible()
            } else {
                historyInVisible()
            }
        }
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
                    searchDebounce()
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })

        binding.clearIcon.setOnClickListener {
            binding.searchUserText.setText("")
            val keyboard = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            keyboard.hideSoftInputFromWindow(binding.searchUserText.windowToken, 0) // скрыть клавиатуру
            binding.searchUserText.clearFocus()
            trackList.clear()
            ifSearchOkVisibility()
        }


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

        val arrowButton = findViewById<ImageView>(R.id.searchButtonArrow)
        arrowButton.setOnClickListener {
            finish()
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = trackAdapter

        binding.searchUserText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (binding.searchUserText.text.isNotEmpty()) {
                    recyclerView.visibility = GONE
                    search(binding.searchUserText)
                    isEnterPressed=true
                    handler.postDelayed({ isEnterPressed=false }, 3000L)
                }
                true
            }
            false
        }

        binding.clearHistoryButton.setOnClickListener {
            App.trackHistoryList.clear()
            historyInVisible()
            trackAdapter.tracks.clear()
            trackAdapter.notifyDataSetChanged()
            historyAdapter.notifyDataSetChanged()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val inputEditText = findViewById<EditText>(R.id.searchUserText)
        outState.putString(KEY_TEXT, inputEditText.text.toString())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        savedInstanceState.getString(KEY_TEXT, "")
    }
    override fun onResume() {
        super.onResume()
        isClickAllowed = true
    }
    private fun clearButtonVisibility(s: CharSequence?): Int {

        return if (s.isNullOrEmpty()) {
            GONE
        } else {
            View.VISIBLE
        }
    }

    private fun search(inputEditText: EditText) {
/*
        trackList.clear()
        if (!inputEditText.text.isNullOrEmpty()) {
            if (!isEnterPressed) progressBar.visibility = View.VISIBLE
            iTunesService.search(inputEditText.text.toString())
                .enqueue(object : Callback<TrackResponse> {
                    override fun onResponse(
                        call: Call<TrackResponse>,
                        response: Response<TrackResponse>
                    ) {
                        progressBar.visibility = GONE

                        if (response.code() == 200) {
                            trackList.clear()
                            ifSearchOkVisibility()
                            if (response.body()?.results?.isNotEmpty() == true) {
                                trackList.addAll(response.body()?.results!!)
                                trackAdapter.notifyDataSetChanged()
                            }
                            if (trackAdapter.tracks.isEmpty()) {
                                nothingfoundPict.visibility = View.VISIBLE
                                nothingfoundText.visibility = View.VISIBLE
                                loadingproblem.visibility = GONE
                                loadingproblemText.visibility = GONE
                                trackAdapter.notifyDataSetChanged()
                            }
                            progressBar.visibility = GONE
                        } else {
                            loadingproblem.visibility = View.VISIBLE
                            loadingproblemText.visibility = View.VISIBLE
                            nothingfoundPict.visibility = GONE
                            nothingfoundText.visibility = GONE
                            refreshButton.setOnClickListener { search(inputEditText) }
                            refreshButton.visibility = View.VISIBLE
                            recyclerView.visibility = GONE
                            trackAdapter.notifyDataSetChanged()
                            progressBar.visibility = GONE
                        }
                    }

                    override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                        loadingproblem.visibility = View.VISIBLE
                        loadingproblemText.visibility = View.VISIBLE
                        refreshButton.visibility = View.VISIBLE
                        recyclerView.visibility = GONE
                        refreshButton.setOnClickListener { search(inputEditText) }
                        progressBar.visibility = GONE
                    }
                })
            return
        }*/
    }

    private fun ifSearchOkVisibility() {
        progressBar.visibility = GONE
        recyclerView.visibility = View.VISIBLE
        binding.nothingfoundPict.visibility = GONE
        binding.nothingfoundText.visibility = GONE
        binding.loadingproblem.visibility = GONE
        binding.loadingproblemText.visibility = GONE
        binding.refreshButton.visibility = GONE
    }

    private fun historyVisible() {
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

    private fun historyInVisible() {
        historyView.visibility = GONE
        historyRecycler.visibility = GONE
        binding.clearHistoryButton.visibility = GONE
    }

    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY_MILLIS)
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
        intent.putExtra("Track Name", item.trackName)
        intent.putExtra("Artist Name", item.artistName)
        val trackTime = SimpleDateFormat(
            "mm:ss",
            Locale.getDefault()
        ).format(item.trackTimeMillis)
        intent.putExtra("Track Time", trackTime)
        intent.putExtra("Album", item.collectionName)
        intent.putExtra("Year", item.releaseDate)
        intent.putExtra("Genre", item.primaryGenreName)
        intent.putExtra("Country", item.country)
        intent.putExtra("Cover", item.artworkUrl100)
        intent.putExtra("URL", item.previewUrl)
        this.startActivity(intent)
        searchHistoryObj.editArray(item)
    }


    companion object {
        private const val SEARCH_DEBOUNCE_DELAY_MILLIS = 2000L
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}