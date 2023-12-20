package com.aryasurya.franchiso.data

import com.aryasurya.franchiso.data.remote.request.LoginRequest
import com.aryasurya.franchiso.data.remote.request.RegisterRequest
import com.aryasurya.franchiso.data.remote.retrofit.ApiService
import com.aryasurya.franchiso.data.pref.UserModel
import com.aryasurya.franchiso.data.pref.UserPreference
import com.aryasurya.franchiso.data.remote.request.UpdateProfileRequest
import com.aryasurya.franchiso.data.remote.response.LoginResponse
import com.aryasurya.franchiso.data.remote.response.RegisterResponse
import com.aryasurya.franchiso.data.remote.response.UpdateResponse
import com.aryasurya.franchiso.data.remote.retrofit.ApiConfig
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

class UserRepository private constructor(
    private val apiService: ApiService,
    private val userPreference: UserPreference
) {

    suspend fun logout() {
        userPreference.logout()
    }

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }


    suspend fun createUser(name: String, email: String, password: String): Flow<Result<RegisterResponse>> = flow {
        emit(Result.Loading)
        try {
            val registerRequest = RegisterRequest(name, email, password, "franchisor")
            val response = apiService.createUser(registerRequest)
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "An error occurred"))
        }
    }

    suspend fun login(email: String, password: String): Flow<Result<LoginResponse>> = flow {
        emit(Result.Loading)
        try {
            // Panggil metode createUser pada apiService
            val request = LoginRequest(email, password)
            val response = apiService.login(request)
//            userPreference.saveSession(UserModel(response.data.name, response.data.token))
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "An error occurred"))
        }
    }

    suspend fun updateProfile(name: String, email: String, password: String): Flow<Result<UpdateResponse>> = flow {
        emit(Result.Loading)
        val token = userPreference.getSession().first().token
        try {
            val updateProfileRequest = UpdateProfileRequest(name, email, password, "franchisor")
            val response = apiService.updateUser("Bearer $token", updateProfileRequest)
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "An error occurred"))
        }
    }


    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            apiService: ApiService,
            userPreference: UserPreference
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(apiService, userPreference)
            }.also { instance = it }
    }
}