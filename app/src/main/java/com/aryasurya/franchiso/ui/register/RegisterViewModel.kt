package com.aryasurya.franchiso.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aryasurya.franchiso.data.Result
import com.aryasurya.franchiso.data.UserRepository
import com.aryasurya.franchiso.data.remote.response.CreateUserResponse
import com.aryasurya.franchiso.data.remote.response.RegisterFranchisorResponse
import kotlinx.coroutines.launch

class RegisterViewModel(private val repository: UserRepository): ViewModel() {
    private val _createUserResult = MutableLiveData<Result<RegisterFranchisorResponse>>()
    val createUserResult: LiveData<Result<RegisterFranchisorResponse>> = _createUserResult

    fun createUser(email: String, name: String, username: String , password: String) {
        viewModelScope.launch {
            repository.createUser(email, name, username , password).collect{ result ->
                _createUserResult.value = result
            }
        }
    }
}