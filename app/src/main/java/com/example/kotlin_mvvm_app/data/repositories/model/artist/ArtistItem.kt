package com.example.kotlin_mvvm_app.data.repositories.model.artist

import com.google.gson.annotations.SerializedName

data class ArtistItem(
    @SerializedName("id")
    val id: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("images")
    val images: List<Image>,

    @SerializedName("preview_url")
    val previewUrl: String,

    @SerializedName("type")
    val type: String,
)
