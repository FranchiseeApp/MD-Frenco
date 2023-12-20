package com.aryasurya.franchiso.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.aryasurya.franchiso.data.FranchiseRepository
import com.aryasurya.franchiso.data.Result
import com.aryasurya.franchiso.data.UserRepository
import com.aryasurya.franchiso.data.pref.UserModel
import com.aryasurya.franchiso.data.remote.request.UpdateProfileRequest
import com.aryasurya.franchiso.data.remote.response.GetMyFranchiseResponse
import com.aryasurya.franchiso.data.remote.response.LoginResponse
import com.aryasurya.franchiso.data.remote.response.RegisterResponse
import com.aryasurya.franchiso.data.remote.response.UpdateResponse
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: FranchiseRepository): ViewModel() {

    fun getMyFranchise(): LiveData<Result<List<GetMyFranchiseResponse>>> {
        return repository.getMyFranchise()
    }
}