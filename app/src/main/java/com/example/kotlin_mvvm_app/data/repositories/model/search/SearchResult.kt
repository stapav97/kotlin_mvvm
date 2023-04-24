package com.example.kotlin_mvvm_app.data.repositories.model.search

import com.example.kotlin_mvvm_app.data.repositories.model.artist.ArtistsResult
import com.google.gson.annotations.SerializedName

data class SearchResult(
    @SerializedName("artists")
    val artists: ArtistsResult
)