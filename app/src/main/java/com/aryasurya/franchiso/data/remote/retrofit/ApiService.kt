package com.aryasurya.franchiso.data.remote.retrofit

import com.aryasurya.franchiso.data.remote.request.FranchiseRequest
import com.aryasurya.franchiso.data.remote.request.LoginRequest
import com.aryasurya.franchiso.data.remote.request.RegisterRequest
import com.aryasurya.franchiso.data.remote.request.UpdateProfileRequest
import com.aryasurya.franchiso.data.remote.response.GetMyFranchiseResponse
import com.aryasurya.franchiso.data.remote.response.LoginResponse
import com.aryasurya.franchiso.data.remote.response.RegisterResponse
import com.aryasurya.franchiso.data.remote.response.UpdateResponse
import com.aryasurya.franchiso.data.remote.response.UploadFranchiseResponse
import com.aryasurya.franchiso.data.remote.response.UploadPhotoResponse
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {


    @POST("users")
    suspend fun createUser(
        @Body registerRequest: RegisterRequest
    ): RegisterResponse

    @POST("users/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse


    @PATCH("users/current")
    suspend fun updateUser(
        @Header("Authorization") authorization: String,
        @Body updateRequest: UpdateProfileRequest
    ): UpdateResponse

    @POST("franchises")
    suspend fun createFranchise(
        @Header("Authorization") authorization: String,
        @Body request: FranchiseRequest
    ): UploadFranchiseResponse

    @Multipart
    @POST("franchises/{id}/upload")
    suspend fun uploadImages(
        @Header("Authorization") authorization: String,
        @Path("id") id: String,
        @Part gallery: List<MultipartBody.Part>
    ): UploadPhotoResponse

    @GET("my_franchises")
    suspend fun getMyFranchises(
        @Header("Authorization") authorization: String,
    ): GetMyFranchiseResponse

}