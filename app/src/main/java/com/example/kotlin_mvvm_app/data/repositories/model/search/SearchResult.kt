package com.example.kotlin_mvvm_app.data.repositories.model.search

import com.example.kotlin_mvvm_app.data.repositories.model.track.TracksResult
import com.google.gson.annotations.SerializedName

data class SearchResult(

    @SerializedName("tracks")
    val tracks: TracksResult
)