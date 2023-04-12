package com.example.kotlin_mvvm_app.data.repositories.model.track

import com.google.gson.annotations.SerializedName

data class TrackItem(

    @SerializedName("id")
    val id: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("preview_url")
    val previewUrl: String,

    @SerializedName("type")
    val type: String,
)