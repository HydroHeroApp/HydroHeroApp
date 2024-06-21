package com.example.hydroheroapp.view.main.analyze

import androidx.lifecycle.ViewModel
import com.example.hydroheroapp.data.remote.repository.AuthRepo

class AnalyzeViewModel(private val authRepo: AuthRepo) : ViewModel() {
    fun prediction(heightInput: String, ch20Input: String, weightInput: String, genderInput: String, ageInput: String, fafInput: String) =
        authRepo.prdeiction(heightInput, ch20Input, weightInput, genderInput, ageInput, fafInput)
}