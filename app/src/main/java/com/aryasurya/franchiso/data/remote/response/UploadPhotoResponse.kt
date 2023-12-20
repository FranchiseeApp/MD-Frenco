package com.aryasurya.franchiso.data.remote.response

import com.google.gson.annotations.SerializedName

data class UploadPhotoResponse(

	@field:SerializedName("data")
	val data: Data2,

	@field:SerializedName("success")
	val success: Boolean,

	@field:SerializedName("message")
	val message: String
)

data class Franchisor2(

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("id")
	val id: Int
)

data class Data2(

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
	val franchisor: Franchisor2,

	@field:SerializedName("franchiseType")
	val franchiseType: List<FranchiseTypeItem2>,

	@field:SerializedName("gallery")
	val gallery: List<GalleryItem>,

	@field:SerializedName("whatsapp_number")
	val whatsappNumber: String
)

data class GalleryItem(

	@field:SerializedName("image")
	val image: String,

	@field:SerializedName("franchise_id")
	val franchiseId: Int,

	@field:SerializedName("id")
	val id: Int
)

data class FranchiseTypeItem2(

	@field:SerializedName("franchise_id")
	val franchiseId: Int,

	@field:SerializedName("price")
	val price: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("facility")
	val facility: String,

	@field:SerializedName("franchise_type")
	val franchiseType: String
)
