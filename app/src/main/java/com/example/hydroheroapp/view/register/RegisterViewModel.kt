package com.example.hydroheroapp.view.register

import androidx.lifecycle.ViewModel
import com.example.hydroheroapp.data.remote.repository.AuthRepo

class RegisterViewModel(private val authRepo: AuthRepo) : ViewModel() {
    fun register(usernameInput: String, emailInput: String, passwordInput: String) =
        authRepo.register(usernameInput, emailInput, passwordInput)
}