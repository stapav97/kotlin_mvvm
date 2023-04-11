package com.example.kotlin_mvvm_app.data.repositories.model

import com.google.gson.annotations.SerializedName

data class AccessToken(
    @SerializedName("access_token")
    val accessToken: String = "",
    @SerializedName("token_type")
    val tokenType: String = "",
    @SerializedName("refresh_token")
    val refreshToken: String = "",
)