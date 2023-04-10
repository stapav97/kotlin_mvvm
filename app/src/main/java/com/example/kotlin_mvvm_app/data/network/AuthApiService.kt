package com.example.kotlin_mvvm_app.data.network

import com.example.kotlin_mvvm_app.data.repositories.model.AccessToken
import retrofit2.http.*
import java.util.*


interface AuthApiService {

    @FormUrlEncoded
    @POST("token")
    suspend fun getAccessToken(
        @Header("Authorization") authHeader: String,
        @Field("redirect_uri") redirectUri: String,
        @Field("grant_type") grantType: String,
        @Field("code") code: String
    ): AccessToken
}