package com.example.hydroheroapp.data.remote.response

import com.google.gson.annotations.SerializedName

data class User(
    @field:SerializedName("profile")
    val profile: Any? = null,

    @field:SerializedName("createdAt")
    val createdAt: String? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("email")
    val email: String? = null,

    @field:SerializedName("username")
    val username: String? = null,

    @field:SerializedName("updatedAt")
    val updatedAt: String? = null
)