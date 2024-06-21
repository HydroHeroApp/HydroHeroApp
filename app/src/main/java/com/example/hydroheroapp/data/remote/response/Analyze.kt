package com.example.hydroheroapp.data.remote.response

import com.google.gson.annotations.SerializedName

data class Analyze(

    @field:SerializedName("height_input")
    val heightInput: String? = null,

    @field:SerializedName("ch2o_input")
    val ch2oInput: String? = null,

    @field:SerializedName("weight_input")
    val weightInput: String? = null,

    @field:SerializedName("gender_input")
    val genderInput: String? = null,

    @field:SerializedName("age_input")
    val ageInput: String? = null,

    @field:SerializedName("faf_input")
    val fafInput: String? = null
)