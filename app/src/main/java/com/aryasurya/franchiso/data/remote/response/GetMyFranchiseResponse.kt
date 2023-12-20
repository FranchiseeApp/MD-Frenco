package com.aryasurya.franchiso.data.remote.response

import com.google.gson.annotations.SerializedName

data class GetMyFranchiseResponse(

	@field:SerializedName("data")
	val data: List<DataItem3>,

	@field:SerializedName("success")
	val success: Boolean,

	@field:SerializedName("message")
	val message: String
)

data class Franchisor3(

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("id")
	val id: Int
)

data class FranchiseTypeItem3(

	@field:SerializedName("type")
	val type: Type
)

data class DataItem3(

	@field:SerializedName("franchise_name")
	val franchiseName: String,

	@field:SerializedName("address")
	val address: String,

	@field:SerializedName("description")
	val description: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("category")
	val category: String,

	@field:SerializedName("franchisor")
	val franchisor: Franchisor3,

	@field:SerializedName("franchiseType")
	val franchiseType: List<FranchiseTypeItem3>,

	@field:SerializedName("gallery")
	val gallery: List<Any>,

	@field:SerializedName("whatsapp_number")
	val whatsappNumber: String
)

data class Type(

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("franchise_type")
	val franchiseType: String
)
