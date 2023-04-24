package com.example.kotlin_mvvm_app.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.kotlin_mvvm_app.data.db.dao.SearchHistoryDao
import com.example.kotlin_mvvm_app.data.db.dao.UserDao
import com.example.kotlin_mvvm_app.data.db.entity.SearchHistory
import com.example.kotlin_mvvm_app.data.db.entity.User

@Database(
    entities = [
        User::class,
        SearchHistory::class
    ],
    version = 1,
    exportSchema = true,
    autoMigrations = [
//        AutoMigration(from = 1, to = 2)
    ]
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun searchHistoryDao(): SearchHistoryDao
}