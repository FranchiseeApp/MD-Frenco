package com.aryasurya.franchiso.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.aryasurya.franchiso.data.Result
import com.aryasurya.franchiso.data.UserRepository
import com.aryasurya.franchiso.data.pref.UserModel
import com.aryasurya.franchiso.data.remote.response.LoginResponse
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: UserRepository): ViewModel() {

    private val _loginResult = MutableLiveData<Result<LoginResponse>>()
    val loginResult: LiveData<Result<LoginResponse>> = _loginResult

    fun login(email: String, password: String) {
        viewModelScope.launch {
            repository.login(email, password).collect { result ->
                _loginResult.value = result
            }
        }
    }

    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }
}