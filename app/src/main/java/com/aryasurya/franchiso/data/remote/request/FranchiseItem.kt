package com.aryasurya.franchiso.data.remote.request

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FranchiseItem(
    var type: String,
    var facility: String,
    var price: String
) : Parcelable