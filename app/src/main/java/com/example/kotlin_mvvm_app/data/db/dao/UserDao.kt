package com.example.kotlin_mvvm_app.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.kotlin_mvvm_app.data.db.entity.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Query("SELECT * FROM user LIMIT 1")
    fun getUser(): Flow<User?>

    @Query("SELECT token FROM user LIMIT 1")
    fun getUserToken(): Flow<String?>

    @Query("SELECT refresh_token FROM user LIMIT 1")
    fun getRefreshToken(): Flow<String?>

}