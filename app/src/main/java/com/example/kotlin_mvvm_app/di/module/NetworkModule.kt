package com.example.kotlin_mvvm_app.di.module

import com.example.kotlin_mvvm_app.BuildConfig
import com.example.kotlin_mvvm_app.data.network.ApiService
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class NetworkModule {

    @Singleton
    @Provides
    fun providesRetrofit(): Retrofit {

        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        val okHttpBuilder = OkHttpClient.Builder()
            .addInterceptor(logging)
            .readTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)

        val gson = GsonBuilder()
            .setLenient()
            .create()

        return Retrofit.Builder()
            .client(okHttpBuilder.build())
            .baseUrl(BuildConfig.API_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Singleton
    @Provides
    fun providesApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

}