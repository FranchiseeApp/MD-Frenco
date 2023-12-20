package com.aryasurya.franchiso.ui.addfranchise

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.aryasurya.franchiso.data.FranchiseRepository
import com.aryasurya.franchiso.data.Result
import com.aryasurya.franchiso.data.UserRepository
import com.aryasurya.franchiso.data.pref.UserModel
import com.aryasurya.franchiso.data.remote.request.FranchiseRequest
import com.aryasurya.franchiso.data.remote.response.LoginResponse
import com.aryasurya.franchiso.data.remote.response.UploadFranchiseResponse
import com.aryasurya.franchiso.data.remote.response.UploadPhotoResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class AddFranchiseViewModel(private val repository: FranchiseRepository): ViewModel() {

//    private val _franchiseResult = MutableLiveData<Result<UploadFranchiseResponse>>()
//    val franchiseResult: LiveData<Result<UploadFranchiseResponse>> = _franchiseResult
//
//    fun createFranchise(franchiseRequest: FranchiseRequest) {
//        viewModelScope.launch {
//            repository.createFranchise(franchiseRequest).collect {
//                _franchiseResult.value = it
//            }
//        }
//    }

    private val _franchiseResult = MutableLiveData<Result<UploadFranchiseResponse>>()
    val franchiseResult: LiveData<Result<UploadFranchiseResponse>> = _franchiseResult

    fun createFranchise(franchiseRequest: FranchiseRequest) {
        viewModelScope.launch {
            try {
                val response = repository.createFranchise(franchiseRequest)
                _franchiseResult.value = Result.Success(response)
            } catch (e: Exception) {
                _franchiseResult.value = Result.Error(e.message ?: "An error occurred")
            }
        }
    }

    private val _uploadResult = MutableLiveData<Result<UploadPhotoResponse>>()
    val uploadResult: LiveData<Result<UploadPhotoResponse>> = _uploadResult

    fun uploadImages(id: String, imageParts: List<MultipartBody.Part>) {
        viewModelScope.launch {
            try {
                val response = repository.uploadImages(id, imageParts)
                _uploadResult.value = Result.Success(response)
            } catch (e: Exception) {
                _uploadResult.value = Result.Error(e.message ?: "An error occurred")
            }

        }
    }

//    fun uploadImages(id: String, imageParts: List<MultipartBody.Part>) {
//        viewModelScope.launch {
//            repository.uploadImages(id, imageParts).collect { result ->
//                _uploadResult.value = result
//            }
//
//        }
//    }

}