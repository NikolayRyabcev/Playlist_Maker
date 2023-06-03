package com.example.playlistmaker

class SearchHistory {
    private val savedHistory = App.getSharedPreferences()


    fun editArray(historyTrackId: Long): ArrayList<Long> {
        val trackHistoryList = ArrayList<Long>()
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

    private fun saveHistory(trackHistoryList: ArrayList<Long>) {
        savedHistory.edit()
            .clear()
            .apply()
        for (i in 0 until trackHistoryList.size) {
            savedHistory.edit()
                .putLong(SEARCH_SHARED_PREFS_KEY, trackHistoryList[i])
                .apply()
        }
    }
    private fun openHistory() :ArrayList<Long> {
        val historyArray =ArrayList<Long>()

        return historyArray
    }

}