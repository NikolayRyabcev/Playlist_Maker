package com.example.playlistmaker

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {
    private val iTunesBaseURL = "https://itunes.apple.com"
    private val retrofit = Retrofit.Builder()
        .baseUrl(iTunesBaseURL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val iTunesService = retrofit.create(iTunesSearchAPI::class.java)
    private lateinit var placeholderMessage: TextView
    var trackList = ArrayList<Track>()
    lateinit var trackAdapter: TrackAdapter

    val KEY_TEXT = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        val nothingfoundPict: ImageView = findViewById(R.id.nothingfoundPict)
        val loadingproblem: ImageView = findViewById(R.id.loadingproblem)
        val nothingfoundText: TextView = findViewById(R.id.nothingfoundText)
        val loadingproblemText: TextView = findViewById(R.id.loadingproblemText)
        nothingfoundPict.visibility = View.GONE
        nothingfoundText.visibility = View.GONE
        loadingproblem.visibility = View.GONE
        loadingproblemText.visibility = View.GONE
        val inputEditText = findViewById<EditText>(R.id.searchUserText)
        val clearButton = findViewById<ImageView>(R.id.clearIcon)

        clearButton.setOnClickListener {
            inputEditText.setText("")
            val keyboard = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            keyboard.hideSoftInputFromWindow(inputEditText.windowToken, 0) // скрыть клавиатуру
            inputEditText.clearFocus()
            trackList.clear()
            nothingfoundPict.visibility = View.GONE
            nothingfoundText.visibility = View.GONE
            loadingproblem.visibility = View.GONE
            loadingproblemText.visibility = View.GONE
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

        val recyclerView = findViewById<RecyclerView>(R.id.trackRecycler)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val trackAdapter = TrackAdapter(trackList)
        recyclerView.adapter = trackAdapter

        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {

                if (inputEditText.text.isNotEmpty()) {
                    iTunesService.search(inputEditText.text.toString())
                        .enqueue(object : Callback<TrackResponse> {

                            override fun onResponse(
                                call: Call<TrackResponse>,
                                response: Response<TrackResponse>
                            ) {
                                if (response.code() == 200) {
                                    trackList.clear()
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
                                    trackAdapter.notifyDataSetChanged()
                                }
                            }

                            override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                                loadingproblem.visibility = View.VISIBLE
                                loadingproblemText.visibility = View.VISIBLE
                            }


                        })
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
            View.GONE
        } else {
            View.VISIBLE
        }
    }
}