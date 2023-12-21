package com.aryasurya.franchiso.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.aryasurya.franchiso.data.pref.UserPreference
import com.aryasurya.franchiso.data.remote.request.FranchiseRequest
import com.aryasurya.franchiso.data.remote.request.RegisterRequest
import com.aryasurya.franchiso.data.remote.response.GetMyFranchiseResponse
import com.aryasurya.franchiso.data.remote.response.RegisterResponse
import com.aryasurya.franchiso.data.remote.response.UploadFranchiseResponse
import com.aryasurya.franchiso.data.remote.response.UploadPhotoResponse
import com.aryasurya.franchiso.data.remote.retrofit.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody

class FranchiseRepository constructor(
    private val apiService: ApiService,
    private val userPreference: UserPreference
) {

//    suspend fun createFranchise(franchiseRequest: FranchiseRequest): Flow<Result<UploadFranchiseResponse>> = flow {
//        emit(Result.Loading)
//        try {
//            val response = apiService.createFranchise(franchiseRequest)
//            emit(Result.Success(response))
//        } catch (e: Exception) {
//            emit(Result.Error(e.message ?: "An error occurred"))
//        }
//    }

//    suspend fun createFranchise(franchiseRequest: FranchiseRequest): Result<UploadFranchiseResponse> {
//        return try {
//            val response = apiService.createFranchise(franchiseRequest)
//            Result.Success(response)
//        } catch (e: Exception) {
//            Result.Error(e.message ?: "An error occurred")
//        }
//    }

    suspend fun createFranchise(franchiseRequest: FranchiseRequest): UploadFranchiseResponse {
        val token = userPreference.getSession().first().token
        return try {
            apiService.createFranchise(token, franchiseRequest)
//            apiService.createFranchise(franchiseRequest)
        } catch (e: Exception) {
            throw Exception("Failed to create franchise: ${e.message}")
        }
    }

    suspend fun uploadImages(id: String, imageParts: List<MultipartBody.Part>): UploadPhotoResponse {
        val token = userPreference.getSession().first().token
        return try {
//            apiService.uploadImages( id, imageParts)
            apiService.uploadImages(token, id, imageParts)
        } catch (e: Exception) {
            throw Exception(e.message ?: "An error occurred")
        }
    }

    fun getMyFranchise(): LiveData<Result<GetMyFranchiseResponse>> = liveData {
        emit(Result.Loading)
        val token = userPreference.getSession().first().token
        try {
            val response = apiService.getMyFranchises(token)
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "An error occurred"))
        }
    }



//    suspend fun uploadImages(id: String, imageParts: List<MultipartBody.Part>): Flow<Result<UploadPhotoResponse>> = flow {
//        emit(Result.Loading)
//        try {
//            val response = apiService.uploadImages(id, imageParts)
//            Result.Success(response)
//        } catch (e: Exception) {
//            Result.Error(e.message ?: "An error occurred")
//        }
//    }

    companion object {
        @Volatile
        private var instance: FranchiseRepository? = null
        fun getInstance(
            apiService: ApiService,
            userPreference: UserPreference
        ): FranchiseRepository =
            instance ?: synchronized(this) {
                instance ?: FranchiseRepository(apiService, userPreference)
            }.also { instance = it }
    }

}