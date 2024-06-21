package com.example.hydroheroapp.data.local

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class HistoryRepository(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("history_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    fun saveHistory(history: List<AnalysisHistory>) {
        val json = gson.toJson(history)
        prefs.edit().putString("history_key", json).apply()
    }

    fun getHistory(): List<AnalysisHistory> {
        val json = prefs.getString("history_key", null) ?: return emptyList()
        val type = object : TypeToken<List<AnalysisHistory>>() {}.type
        return gson.fromJson(json, type)
    }
}
