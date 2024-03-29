package com.example.kotlin_mvvm_app.data.repositories.model.track

import com.google.gson.annotations.SerializedName

data class TracksResult(

    @SerializedName("href")
    val href: String,

    @SerializedName("limit")
    val limit: Int,

    @SerializedName("next")
    val next: String,

    @SerializedName("offset")
    val offset: Int,

    @SerializedName("previous")
    val previous: String,

    @SerializedName("total")
    val total: Int,

    @SerializedName("items")
    val items: List<TrackItemResult>
)