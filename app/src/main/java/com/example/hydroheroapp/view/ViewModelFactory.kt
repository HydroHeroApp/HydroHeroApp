package com.example.hydroheroapp.view

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.hydroheroapp.data.remote.repository.AuthRepo
import com.example.hydroheroapp.data.remote.repository.LoginPrefsRepo
import com.example.hydroheroapp.di.Injection
import com.example.hydroheroapp.view.login.LoginViewModel
import com.example.hydroheroapp.view.main.MainViewModel
import com.example.hydroheroapp.view.main.analyze.AnalyzeViewModel
import com.example.hydroheroapp.view.register.RegisterViewModel

class ViewModelFactory(private val authRepo: AuthRepo, private val preferences: LoginPrefsRepo): ViewModelProvider.NewInstanceFactory(){

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            return RegisterViewModel(authRepo) as T
        }
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(authRepo,preferences) as T
        }
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(preferences) as T
        }
        if (modelClass.isAssignableFrom(AnalyzeViewModel::class.java)) {
            return AnalyzeViewModel(authRepo) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null

        fun getInstance(context: Context, preferences: LoginPrefsRepo): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideRepository(context), preferences)
            }.also { instance = it }
    }
}
