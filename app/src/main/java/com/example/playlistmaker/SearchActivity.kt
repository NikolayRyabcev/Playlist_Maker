package com.example.playlistmaker

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.View.GONE
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val SEARCH_SHARED_PREFS_KEY = "123"

class SearchActivity : AppCompatActivity() {
    lateinit var trackList: ArrayList<Track>
    lateinit var refreshButton: Button
    lateinit var nothingfoundPict: ImageView
    lateinit var loadingproblem: ImageView
    lateinit var nothingfoundText: TextView
    lateinit var loadingproblemText: TextView
    lateinit var trackAdapter: TrackAdapter
    lateinit var historyAdapter: TrackAdapter
    lateinit var recyclerView: RecyclerView
    lateinit var inputEditText: EditText
    lateinit var clearButton: ImageView
    lateinit var historyView: TextView
    lateinit var historyRecycler: RecyclerView
    lateinit var clearHistoryButton: Button
    private val searchHistoryObj = SearchHistory()


    private val iTunesBaseURL = "https://itunes.apple.com"
    private val retrofit = Retrofit.Builder()
        .baseUrl(iTunesBaseURL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val iTunesService = retrofit.create(iTunesSearchAPI::class.java)

    private val KEY_TEXT = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        trackList = ArrayList()
        trackAdapter = TrackAdapter(trackList)
        historyAdapter = TrackAdapter(searchHistoryObj.trackHistoryList)
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

        ifSearchOkVisibility()
        historyInVisible()
        searchHistoryObj.toaster(this, "started")
        inputEditText.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus && inputEditText.text.isEmpty()) {
                historyVisible()
                searchHistoryObj.toaster(this, "focusOn")
            } else {
                historyInVisible()
                searchHistoryObj.toaster(this, "focusOff")
            }

        }
        inputEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (inputEditText.hasFocus() && p0?.isEmpty() == true) {
                    historyVisible()
                } else {
                    historyInVisible()
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
                    search(inputEditText)
                }
                true
            }
            false
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

    private fun clearButtonVisibility(s: CharSequence?): Int {

        return if (s.isNullOrEmpty()) {
            GONE
        } else {
            View.VISIBLE
        }
    }

    private fun search(inputEditText: EditText) {
        trackList.clear()

        recyclerView.visibility = View.VISIBLE
        iTunesService.search(inputEditText.text.toString())
            .enqueue(object : Callback<TrackResponse> {
                override fun onResponse(
                    call: Call<TrackResponse>,
                    response: Response<TrackResponse>
                ) {
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
                    } else {
                        loadingproblem.visibility = View.VISIBLE
                        loadingproblemText.visibility = View.VISIBLE
                        nothingfoundPict.visibility = GONE
                        nothingfoundText.visibility = GONE
                        refreshButton.setOnClickListener { search(inputEditText) }
                        refreshButton.visibility = View.VISIBLE
                        recyclerView.visibility = GONE
                        trackAdapter.notifyDataSetChanged()
                    }
                }

                override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                    loadingproblem.visibility = View.VISIBLE
                    loadingproblemText.visibility = View.VISIBLE
                    refreshButton.visibility = View.VISIBLE
                    recyclerView.visibility = GONE
                    refreshButton.setOnClickListener { search(inputEditText) }
                }
            })
    }

    private fun ifSearchOkVisibility() {
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
}