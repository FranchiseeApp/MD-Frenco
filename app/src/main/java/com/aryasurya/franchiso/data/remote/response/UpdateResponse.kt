package com.aryasurya.franchiso.data.remote.response

import com.google.gson.annotations.SerializedName

data class UpdateResponse(

	@field:SerializedName("data")
	val data: Data,

	@field:SerializedName("success")
	val success: Boolean,

	@field:SerializedName("message")
	val message: String
)

