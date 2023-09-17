package com.example.playlistmaker.ui.search.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentSearchBinding
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.ui.player.activity.PlayerActivity
import com.example.playlistmaker.ui.search.adapter.TrackAdapter
import com.example.playlistmaker.ui.search.viewModelForActivity.SearchViewModel
import com.example.playlistmaker.ui.search.viewModelForActivity.screen_states.SearchScreenState
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.IOException

class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding

    // viewModel:
    private val searchViewModel by viewModel<SearchViewModel>()
    private var isClickAllowed = true

    private lateinit var trackAdapter: TrackAdapter
    private lateinit var historyAdapter: TrackAdapter
    private lateinit var historyRecycler: RecyclerView
    private lateinit var historyList: List<Track>
    private lateinit var recyclerView: RecyclerView

    private val handler = Handler(Looper.getMainLooper())

    private var isEnterPressed: Boolean = false

    private lateinit var activity: Activity
    private lateinit var bottomNavigator: BottomNavigationView
    private val KEY_TEXT = ""

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
        activity = requireActivity()
        bottomNavigator = activity.findViewById(R.id.bottomNavigationView)
        historyRecycler = binding.historyRecycler

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
        trackAdapter = TrackAdapter() {
            if (clickDebounce()) {
                clickAdapting(it)
            }
        }

        recyclerView = binding.trackRecycler
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = trackAdapter

        //история

        historyAdapter = TrackAdapter() {
            if (clickDebounce()) {
                clickAdapting(it)
            }
        }

        historyRecycler.layoutManager = LinearLayoutManager(requireContext())
        historyRecycler.adapter = historyAdapter
        binding.clearHistoryButton.setOnClickListener {
            historyInVisible()
            searchViewModel.clearHistory()
        }
        historyList = try {
            val historyValue = searchViewModel.provideHistory().value
            historyValue ?: emptyList()

        } catch (e: IOException) {
            emptyList()
        }
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
        val intent = Intent(requireContext(), PlayerActivity::class.java)
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
        if (!isEnterPressed) searchViewModel.searchRequesting(binding.searchUserText.text.toString())
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
    var searchText = ""

    // когда меняется текст в поисковой строке
    private fun onSearchTextChange() {
        binding.searchUserText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (binding.searchUserText.hasFocus() && p0?.isEmpty() == true && historyList.isNotEmpty()) {
                    searchViewModel.clearTrackList()
                } else {
                    historyInVisible()
                }
                if (!binding.searchUserText.text.isNullOrEmpty()) {
                    searchText = binding.searchUserText.text.toString()
                    if (!isEnterPressed) {
                        searchDebounce()
                    }

                }
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
                    searchText = binding.searchUserText.text.toString()
                    bottomNavigator.visibility = VISIBLE
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
        recyclerView.visibility = GONE
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
        recyclerView.visibility = GONE
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
        recyclerView.visibility = VISIBLE
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
        historyRecycler.visibility = GONE
        binding.clearHistoryButton.visibility = VISIBLE
        recyclerView.visibility = GONE
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
        recyclerView.visibility = GONE
        binding.refreshButton.setOnClickListener { search() }
        binding.progressBar.visibility = GONE
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
        recyclerView.visibility = GONE
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
        historyRecycler.visibility = GONE
        binding.clearHistoryButton.visibility = GONE
    }

    override fun onStop() {
        super.onStop()

    }


    companion object {
        private const val SEARCH_DEBOUNCE_DELAY_MILLIS = 2000L
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

}