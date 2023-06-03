package com.example.playlistmaker

class SearchHistory {
    val savedHistory = App.getSharedPreferences()
    var trackHistoryList = ArrayList<String>()

    fun editArray(historyTrackId: String): ArrayList<String> {

        if (trackHistoryList.contains(historyTrackId)) {
            trackHistoryList.remove(historyTrackId)
            trackHistoryList.add(0, historyTrackId)
        } else {
            if (trackHistoryList.size < 10) trackHistoryList.add(0, historyTrackId)
            else {
                trackHistoryList.removeAt(9)
                trackHistoryList.add(0, historyTrackId)
            }
        }
        saveHistory(trackHistoryList)
        return trackHistoryList
    }

    private fun saveHistory(trackHistoryList: ArrayList<String>) {
        savedHistory.edit()
            .clear()
            .apply()
        for (i in 0 until trackHistoryList.size) {
            savedHistory.edit()
                .putString(SEARCH_SHARED_PREFS_KEY, trackHistoryList[i])
                .apply()
        }
    }

}