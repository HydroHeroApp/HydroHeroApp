package com.example.hydroheroapp.data.remote.response

import com.google.gson.annotations.SerializedName

data class ModelResponse(

	@field:SerializedName("analyze")
	val analyze: Analyze? = null,

	@field:SerializedName("predict")
	val predict: String? = null
)


