package com.example.kotlin_mvvm_app.data.repositories.model.track

import com.google.gson.annotations.SerializedName
import java.util.Date

data class TrackItemResult(
    @SerializedName("added_at")
    val addedAt: Date,
    @SerializedName("track")
    val track: TrackItem
)