package com.aryasurya.franchiso.data.remote.request

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FranchiseTypeRequest(
    val franchise_type: String,
    val facility: String,
    val price: String
) : Parcelable

data class FranchiseRequest(
    val franchise_name: String,
    val address: String,
    val description: String,
    val category: String,
    val whatsapp_number: String,
    val franchiseType: List<FranchiseTypeRequest>
)