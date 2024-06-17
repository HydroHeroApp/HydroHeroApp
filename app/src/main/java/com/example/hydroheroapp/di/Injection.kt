package com.example.hydroheroapp.di

import android.content.Context
import com.example.hydroheroapp.data.remote.retrofit.ApiConfig
import com.example.hydroheroapp.data.remote.repository.AuthRepo
import com.example.hydroheroapp.data.remote.repository.LoginPrefsRepo
import com.example.hydroheroapp.data.remote.repository.dataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): AuthRepo {
        val pref = LoginPrefsRepo.getInstance(context.dataStore)
        val email = runBlocking {
            pref.getToken().first()
        }
        val apiService = ApiConfig.getApiService(email.toString())
        return AuthRepo.getInstance(apiService, pref,)
    }
}