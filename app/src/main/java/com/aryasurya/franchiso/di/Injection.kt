package com.aryasurya.franchiso.di

import android.content.Context
import com.aryasurya.franchiso.data.FranchiseRepository
import com.aryasurya.franchiso.data.remote.retrofit.ApiConfig
import com.aryasurya.franchiso.data.UserRepository
import com.aryasurya.franchiso.data.pref.UserPreference
import com.aryasurya.franchiso.data.pref.dataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getSession().first() }
        val apiService = ApiConfig.getApiService(user.token)
        val userPreference = UserPreference.getInstance(context.dataStore)
        return UserRepository.getInstance(apiService, userPreference)
    }
    fun provideRepositoryFranchise(context: Context): FranchiseRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getSession().first() }
        val apiService = ApiConfig.getApiService(user.token)
        val userPreference = UserPreference.getInstance(context.dataStore)
        return FranchiseRepository.getInstance(apiService, userPreference)
    }

}