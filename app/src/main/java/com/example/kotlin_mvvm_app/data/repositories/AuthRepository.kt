package com.example.kotlin_mvvm_app.data.repositories

import com.example.kotlin_mvvm_app.data.network.AuthApiService
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val authApiService: AuthApiService
){
}