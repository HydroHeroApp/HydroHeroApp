package com.example.hydroheroapp.data.remote.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "login_pref")
class LoginPrefsRepo private constructor (private val dataStore: DataStore<Preferences>) {

    fun getLoginStatus(): Flow<Boolean?> {
        return dataStore.data.map { preferences ->
            preferences[STATE_KEY]
        }
    }

    suspend fun login() {
        dataStore.edit { preferences ->
            preferences[STATE_KEY] = true
        }
    }

    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences[EMAIL_KEY] = ""
            preferences[STATE_KEY] = false
        }
    }

    fun getToken(): Flow<String?> {
        return dataStore.data.map { preferences ->
            preferences[EMAIL_KEY]
        }
    }

    suspend fun saveToken(token: String) {
        dataStore.edit { preferences ->
            preferences[EMAIL_KEY] = token
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: LoginPrefsRepo? = null

        private val EMAIL_KEY = stringPreferencesKey("email")
        private val  STATE_KEY = booleanPreferencesKey("state")

        fun getInstance(dataStore: DataStore<Preferences>): LoginPrefsRepo {
            return INSTANCE ?: synchronized(this) {
                val instance = LoginPrefsRepo(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}