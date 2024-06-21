package com.example.hydroheroapp.data.remote.retrofit

import com.example.hydroheroapp.data.remote.response.ErrorResponse
import com.example.hydroheroapp.data.remote.response.LoginResponse
import com.example.hydroheroapp.data.remote.response.ModelResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService {

    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("username") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): ErrorResponse

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    @FormUrlEncoded
    @POST("predict")
    suspend fun  analyze(
        @Field("gender_input") gender: String,
        @Field("age_input") age: String,
        @Field("weight_input") weight: String,
        @Field("height_input")  height: String,
        @Field("ch2o_input") ch2o: String,
        @Field("faf_input") faf: String
    ): ModelResponse
}