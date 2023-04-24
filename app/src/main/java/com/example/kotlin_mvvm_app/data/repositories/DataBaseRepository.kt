package com.example.kotlin_mvvm_app.data.repositories

import com.example.kotlin_mvvm_app.data.db.AppDatabase
import com.example.kotlin_mvvm_app.data.db.entity.SearchHistory
import com.example.kotlin_mvvm_app.data.db.entity.User
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DatabaseRepository @Inject constructor(
    private val mDatabase: AppDatabase,
) {
    suspend fun insertUser(user: User) {
        mDatabase.userDao().insertUser(user)
    }

    fun getUser(): Flow<User?> {
        return mDatabase.userDao().getUser()
    }

    fun getUserToken(): Flow<String?> {
        return mDatabase.userDao().getUserToken()
    }

    fun getRefreshToken(): Flow<String?> {
        return mDatabase.userDao().getRefreshToken()
    }

    suspend fun insertSearchHistory(searchHistory: SearchHistory) {
        mDatabase.searchHistoryDao().insertSearchHistory(searchHistory)
    }
}