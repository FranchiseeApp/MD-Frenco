package com.aryasurya.franchiso.data.pref

data class RegisterRequest(
    val email: String,
    val name: String,
    val password: String,
    val role: String
)

data class LoginRequest(
    val username: String,
    val password: String
)