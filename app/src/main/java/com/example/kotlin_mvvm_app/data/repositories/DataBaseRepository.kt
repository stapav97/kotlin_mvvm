package com.example.kotlin_mvvm_app.data.repositories

import com.example.kotlin_mvvm_app.data.db.AppDatabase
import javax.inject.Inject

class DatabaseRepository @Inject constructor(
    private val mDatabase: AppDatabase,
){
//    suspend fun getUserToken(): String?{
//        return mDatabase.userDao().getUserToken()
//    }
}