package com.aryasurya.franchiso.data

import android.util.Log
import com.aryasurya.franchiso.data.pref.LoginRequest
import com.aryasurya.franchiso.data.pref.RegisterRequest
import com.aryasurya.franchiso.data.remote.retrofit.ApiService
import com.aryasurya.franchiso.data.pref.UserModel
import com.aryasurya.franchiso.data.pref.UserPreference
import com.aryasurya.franchiso.data.remote.response.ErrorResponse
import com.aryasurya.franchiso.data.remote.response.RegisterResponse
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException

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
            // Panggil metode createUser pada apiService
            val registerRequest = RegisterRequest(name, email, password, "franchisor")
            val response = apiService.createUser(registerRequest)
            emit(Result.Success(response))
        } catch (t: Throwable) {
            when (t) {
                is HttpException -> {
                    try {
                        val errorResponse = Gson().fromJson(
                            t.response()?.errorBody()?.charStream(),
                            ErrorResponse::class.java
                        )
                        Log.e("TAG_LOGIN", "onFailure: ${errorResponse.message}")
                        emit(Result.Error(errorResponse.message))
                    } catch (e: Exception) {
                        Log.e("TAG_LOGIN", "onFailure: ${e.message.toString()}")
                        emit(Result.Error(e.message.toString()))
                    }
                }

                else -> {
                    Log.e("TAG_LOGIN", "onFailure: ${t.message.toString()}")
                    emit(Result.Error(t.message.toString()))
                }
            }
        }
    }

//    suspend fun login(username: String, password: String): Flow<Result<LoginResponse>> = flow {
//        emit(Result.Loading)
//        try {
//            // Panggil metode createUser pada apiService
//            val request = LoginRequest(username, password)
//            val response = apiService.login(request)
//            userPreference.saveSession(UserModel(response.data.name,response.data.appToken))
//            emit(Result.Success(response))
//        } catch (e: Exception) {
//            emit(Result.Error(e.message ?: "An error occurred"))
//        }
//    }

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