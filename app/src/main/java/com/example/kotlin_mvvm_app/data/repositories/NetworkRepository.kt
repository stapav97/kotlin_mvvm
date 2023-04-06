package com.example.kotlin_mvvm_app.data.repositories

import com.example.kotlin_mvvm_app.data.network.ApiService
import javax.inject.Inject

class NetworkRepository  @Inject constructor(
    private val apiService: ApiService
){
}