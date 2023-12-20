package com.aryasurya.franchiso.data.remote.retrofit

import com.aryasurya.franchiso.data.pref.LoginRequest
import com.aryasurya.franchiso.data.pref.RegisterRequest
import com.aryasurya.franchiso.data.remote.response.DetailStoriesResponse
import com.aryasurya.franchiso.data.remote.response.FileUploadResponse
import com.aryasurya.franchiso.data.remote.response.RegisterResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {


    @POST("users")
    suspend fun createUser(
        @Body registerRequest: RegisterRequest
    ): RegisterResponse

//    @POST("user/login")
//    suspend fun login(@Body request: LoginRequest): LoginResponse


    @Multipart
    @POST("stories")
    suspend fun postStory(
        @Part file: MultipartBody.Part ,
        @Part("description") description: RequestBody ,
    ): FileUploadResponse

    @Multipart
    @POST("stories")
    suspend fun postStory(
        @Part file: MultipartBody.Part ,
        @Part("description") description: RequestBody ,
        @Part("lat") lat: Double ,
        @Part("lon") lon: Double ,
    ): FileUploadResponse

    @GET("stories/{id}")
    suspend fun getDetailStory(
        @Path("id") id: String
    ): DetailStoriesResponse

}