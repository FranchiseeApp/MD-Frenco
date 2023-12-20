package com.aryasurya.franchiso.data

import com.aryasurya.franchiso.data.pref.UserPreference
import com.aryasurya.franchiso.data.remote.request.FranchiseRequest
import com.aryasurya.franchiso.data.remote.response.UploadFranchiseResponse
import com.aryasurya.franchiso.data.remote.retrofit.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

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
        return try {
            apiService.createFranchise(franchiseRequest)
        } catch (e: Exception) {
            throw Exception("Failed to create franchise: ${e.message}")
        }
    }


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