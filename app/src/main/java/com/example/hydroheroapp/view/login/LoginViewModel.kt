package com.example.hydroheroapp.view.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hydroheroapp.data.remote.repository.AuthRepo
import com.example.hydroheroapp.data.remote.repository.LoginPrefsRepo
import kotlinx.coroutines.launch

class LoginViewModel(private val authRepo: AuthRepo, private val preferences: LoginPrefsRepo) : ViewModel() {
    fun login(emailInput: String, passwordInput: String) =
        authRepo.login(emailInput, passwordInput)

    fun saveState(token: String) {
        viewModelScope.launch {
            preferences.saveToken(token)
            preferences.login()
        }
    }
}