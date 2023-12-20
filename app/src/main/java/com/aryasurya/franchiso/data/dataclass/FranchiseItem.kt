package com.aryasurya.franchiso.data.dataclass

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FranchiseItem(
    var type: String,
    var facility: String,
    var price: String
) : Parcelable