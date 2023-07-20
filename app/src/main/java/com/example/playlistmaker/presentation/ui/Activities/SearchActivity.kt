package com.example.playlistmaker.presentation.ui.Activities

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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.App.App
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.SearchHistory
import com.example.playlistmaker.data.dto.TrackResponse
import com.example.playlistmaker.data.network.iTunesSearchAPI
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.presentation.ui.Track.TrackAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Locale

const val SEARCH_SHARED_PREFS_KEY = "123"

class SearchActivity : AppCompatActivity() {

    private var isClickAllowed = true

    lateinit var trackList: ArrayList<Track>

    lateinit var refreshButton: Button
    lateinit var nothingfoundPict: ImageView
    lateinit var loadingproblem: ImageView
    lateinit var nothingfoundText: TextView
    lateinit var loadingproblemText: TextView

    lateinit var trackAdapter: TrackAdapter
    private lateinit var historyAdapter: TrackAdapter
    private lateinit var historyView: TextView
    private lateinit var historyRecycler: RecyclerView

    lateinit var recyclerView: RecyclerView
    lateinit var inputEditText: EditText
    lateinit var clearButton: ImageView
    private lateinit var clearHistoryButton: Button

    val searchHistoryObj = SearchHistory()
    private val handler = Handler(Looper.getMainLooper())
    private val searchRunnable = Runnable { search(inputEditText) }
    lateinit var progressBar: ProgressBar

    private val iTunesBaseURL = "https://itunes.apple.com"
    private val retrofit = Retrofit.Builder()
        .baseUrl(iTunesBaseURL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val iTunesService = retrofit.create(iTunesSearchAPI::class.java)

    var isEnterPressed :Boolean = false

    private val KEY_TEXT = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)


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
        inputEditText = findViewById(R.id.searchUserText)
        clearButton = findViewById(R.id.clearIcon)
        refreshButton = findViewById(R.id.refreshButton)
        nothingfoundPict = findViewById(R.id.nothingfoundPict)
        nothingfoundText = findViewById(R.id.nothingfoundText)
        loadingproblemText = findViewById(R.id.loadingproblemText)
        loadingproblem = findViewById(R.id.loadingproblem)
        historyView = findViewById(R.id.historyTextView)
        historyRecycler = findViewById(R.id.historyRecycler)
        clearHistoryButton = findViewById(R.id.clearHistoryButton)


        applicationContext.getSharedPreferences(SEARCH_SHARED_PREFS_KEY, MODE_PRIVATE)

        historyRecycler.layoutManager = LinearLayoutManager(this)
        historyRecycler.adapter = historyAdapter

        progressBar = findViewById(R.id.progressBar)

        ifSearchOkVisibility()
        historyInVisible()
        inputEditText.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus && inputEditText.text.isEmpty() && App.trackHistoryList.isNotEmpty()) {
                historyVisible()
            } else {
                historyInVisible()
            }

        }
        inputEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (inputEditText.hasFocus() && p0?.isEmpty() == true && App.trackHistoryList.isNotEmpty()) {
                    historyVisible()

                } else {
                    historyInVisible()
                }
                if (!inputEditText.text.isNullOrEmpty()) {
                    recyclerView.visibility = GONE
                    searchDebounce()
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })

        clearButton.setOnClickListener {
            inputEditText.setText("")
            val keyboard = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            keyboard.hideSoftInputFromWindow(inputEditText.windowToken, 0) // скрыть клавиатуру
            inputEditText.clearFocus()
            trackList.clear()
            ifSearchOkVisibility()
        }


        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.visibility = clearButtonVisibility(s)
            }

            override fun afterTextChanged(s: Editable?) {
                // empty
            }
        }
        inputEditText.addTextChangedListener(simpleTextWatcher)

        val arrowButton = findViewById<ImageView>(R.id.searchButtonArrow)
        arrowButton.setOnClickListener {
            finish()
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = trackAdapter

        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (inputEditText.text.isNotEmpty()) {
                    recyclerView.visibility = GONE
                    search(inputEditText)
                    isEnterPressed=true
                    handler.postDelayed({ isEnterPressed=false }, 3000L)
                }
                true
            }
            false
        }

        clearHistoryButton.setOnClickListener {
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
        }
    }

    private fun ifSearchOkVisibility() {
        progressBar.visibility = GONE
        recyclerView.visibility = View.VISIBLE
        nothingfoundPict.visibility = GONE
        nothingfoundText.visibility = GONE
        loadingproblem.visibility = GONE
        loadingproblemText.visibility = GONE
        refreshButton.visibility = GONE
    }

    private fun historyVisible() {
        historyView.visibility = View.VISIBLE
        historyRecycler.visibility = View.VISIBLE
        clearHistoryButton.visibility = View.VISIBLE
        recyclerView.visibility = GONE
        nothingfoundPict.visibility = GONE
        nothingfoundText.visibility = GONE
        loadingproblem.visibility = GONE
        loadingproblemText.visibility = GONE
        refreshButton.visibility = GONE
    }

    private fun historyInVisible() {
        historyView.visibility = GONE
        historyRecycler.visibility = GONE
        clearHistoryButton.visibility = GONE
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