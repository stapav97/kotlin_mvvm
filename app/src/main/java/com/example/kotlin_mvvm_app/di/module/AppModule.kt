package com.example.kotlin_mvvm_app.di.module

import android.content.Context
import androidx.room.Room
import com.example.kotlin_mvvm_app.App
import com.example.kotlin_mvvm_app.data.db.AppDatabase
import com.example.kotlin_mvvm_app.data.network.ApiService
import com.example.kotlin_mvvm_app.data.repositories.NetworkRepository
import com.example.kotlin_mvvm_app.utils.AppExecutors
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {

    @Singleton
    @Provides
    fun provideDatabase(context: Context, executors: AppExecutors): AppDatabase = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "app-database"
    ).setQueryExecutor(executors.diskExecutor).build()

    @Singleton
    @Provides
    fun providesRepository(apiService: ApiService): NetworkRepository =
        NetworkRepository(apiService)

    @Singleton
    @Provides
    fun providesContext(app: App): Context {
        return app.applicationContext;
    }
}