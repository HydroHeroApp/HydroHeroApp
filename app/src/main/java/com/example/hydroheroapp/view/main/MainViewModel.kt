package com.example.hydroheroapp.view.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hydroheroapp.data.remote.repository.LoginPrefsRepo
import kotlinx.coroutines.launch

class MainViewModel(private val preferences: LoginPrefsRepo) : ViewModel() {
    fun logout() {
        viewModelScope.launch {
            preferences.logout()
        }
    }
}