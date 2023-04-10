package com.example.kotlin_mvvm_app.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.kotlin_mvvm_app.data.db.dao.UserDao
import com.example.kotlin_mvvm_app.data.db.entity.User

@Database(
    entities = [
        User::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}