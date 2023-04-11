package com.example.kotlin_mvvm_app.data.repositories

import com.example.kotlin_mvvm_app.data.db.AppDatabase
import com.example.kotlin_mvvm_app.data.db.entity.User
import javax.inject.Inject

class DatabaseRepository @Inject constructor(
    private val mDatabase: AppDatabase,
){
    suspend fun insertUser(user: User) {
        mDatabase.userDao().insertUser(user)
    }

    suspend fun getUser(): User? {
        return mDatabase.userDao().getUser()
    }

    suspend fun getUserToken(): String?{
        return mDatabase.userDao().getUserToken()
    }

    suspend fun getRefreshToken(): String?{
        return mDatabase.userDao().getRefreshToken()
    }
}