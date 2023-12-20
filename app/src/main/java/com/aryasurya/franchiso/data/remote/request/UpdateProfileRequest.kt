package com.aryasurya.franchiso.data.remote.request

data class UpdateProfileRequest (
    val name: String,
    val email: String,
    val password: String,
    val phone: String,
    val gender: String,
    val avatar: String,
    val role: String,
)