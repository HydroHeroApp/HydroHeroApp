package com.example.hydroheroapp.data.remote.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.hydroheroapp.data.remote.response.ErrorResponse
import com.example.hydroheroapp.data.remote.retrofit.ApiService
import com.google.gson.Gson
import retrofit2.HttpException
import com.example.hydroheroapp.data.Result
import com.example.hydroheroapp.data.remote.response.Analyze
import com.example.hydroheroapp.data.remote.response.ModelResponse
import com.example.hydroheroapp.data.remote.response.User

class AuthRepo(
    private var apiService: ApiService,
    private val pref: LoginPrefsRepo,
) {
    fun register(
        username: String,
        email: String,
        password: String
    ): LiveData<Result<ErrorResponse>> =
        liveData {
            emit(Result.Loading)
            try {
                val response = apiService.register(username, email, password)
                emit(Result.Success(response))
            } catch (e: HttpException) {
                val jsonInString = e.response()?.errorBody()?.string()
                val error = Gson().fromJson(jsonInString, ErrorResponse::class.java)
                emit(Result.Error(error.message.toString()))
            } catch (e: Exception) {
                emit(Result.Error(e.message.toString()))
            }
        }

    fun login(email: String, password: String): LiveData<Result<User>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.login(email, password)
            val UserResponse = response.user
            if (UserResponse != null) {
                emit(Result.Success(UserResponse))
            } else {
                emit(Result.Error("There was an error"))
            }
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            emit(Result.Error(errorBody.message.toString()))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun prdeiction(
        height: String,
        ch20: String,
        weight: String,
        gender: String,
        age: String,
        faf: String
    ): LiveData<Result<ModelResponse>> =
        liveData {
            emit(Result.Loading)
            try {
                val response = apiService.analyze(height, ch20, weight, gender, age, faf)
                emit(Result.Success(response))
            } catch (e: HttpException) {
                val jsonInString = e.response()?.errorBody()?.string()
                val error = Gson().fromJson(jsonInString, ModelResponse::class.java)
                emit(Result.Error(error.predict.toString()))
            } catch (e: Exception) {
                emit(Result.Error(e.message.toString()))
            }

        }

    companion object {
        @Volatile
        private var instance: AuthRepo? = null

        fun getInstance(apiService: ApiService, preferences: LoginPrefsRepo): AuthRepo =
            instance ?: synchronized(this) {
                instance ?: AuthRepo(apiService, preferences).also { instance = it }
            }
    }
}