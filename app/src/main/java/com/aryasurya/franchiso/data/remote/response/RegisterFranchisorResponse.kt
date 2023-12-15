package com.aryasurya.franchiso.data.remote.response

import com.google.gson.annotations.SerializedName

data class RegisterFranchisorResponse(

	@field:SerializedName("data")
	val data: String,

	@field:SerializedName("message")
	val message: String
)
