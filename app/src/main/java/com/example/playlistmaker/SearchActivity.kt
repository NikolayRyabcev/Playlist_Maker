package com.example.playlistmaker

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.View.GONE
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {
    var trackList = ArrayList<Track>()
    val refreshButton: Button = findViewById(R.id.refreshButton)
    val nothingfoundPict: ImageView = findViewById(R.id.nothingfoundPict)
    val loadingproblem: ImageView = findViewById(R.id.loadingproblem)
    val nothingfoundText: TextView = findViewById(R.id.nothingfoundText)
    val loadingproblemText: TextView = findViewById(R.id.loadingproblemText)
    var trackAdapter = TrackAdapter(trackList)
    var recyclerView: RecyclerView = findViewById(R.id.trackRecycler)
    private val inputEditText: EditText = findViewById(R.id.searchUserText)
    val clearButton: ImageView = findViewById(R.id.clearIcon)

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

        ifSearchOkVisibility()

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
                            trackAdapter.notifyDataSetChanged()
                        }
                    } else {
                        loadingproblem.visibility = View.VISIBLE
                        loadingproblemText.visibility = View.VISIBLE
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
}