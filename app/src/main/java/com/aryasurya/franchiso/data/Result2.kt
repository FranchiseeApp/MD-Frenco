package com.aryasurya.franchiso.data

sealed class Result2<out R> private constructor() {
    data class Success<out T>(val data: T) : Result2<T>()
    data class Error<out T>(val data: T) : Result2<T>()
    object Loading : Result2<Nothing>()
}