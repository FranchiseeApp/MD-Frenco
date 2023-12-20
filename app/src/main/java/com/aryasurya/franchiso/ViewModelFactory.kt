package com.aryasurya.franchiso

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.aryasurya.franchiso.data.FranchiseRepository
import com.aryasurya.franchiso.data.UserRepository
import com.aryasurya.franchiso.di.Injection
import com.aryasurya.franchiso.ui.account.AccountViewModel
import com.aryasurya.franchiso.ui.addfranchise.AddFranchiseViewModel
import com.aryasurya.franchiso.ui.editprofile.EditProfileViewModel
import com.aryasurya.franchiso.ui.login.LoginViewModel
import com.aryasurya.franchiso.ui.register.RegisterViewModel

class ViewModelFactory(
    private val repository: UserRepository,
    private val repositoryFranchise: FranchiseRepository,
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(repository) as T
            }
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(repository) as T
            }
            modelClass.isAssignableFrom(AccountViewModel::class.java) -> {
                AccountViewModel(repository) as T
            }
            modelClass.isAssignableFrom(EditProfileViewModel::class.java) -> {
                EditProfileViewModel(repository) as T
            }
            modelClass.isAssignableFrom(AddFranchiseViewModel::class.java) -> {
                AddFranchiseViewModel(repositoryFranchise) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel Class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        @JvmStatic
        fun getInstance(context: Context) =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: ViewModelFactory(
                    Injection.provideRepository(context) ,
                    Injection.provideRepositoryFranchise(context) ,
                )
            }.also { INSTANCE = it }
    }
}