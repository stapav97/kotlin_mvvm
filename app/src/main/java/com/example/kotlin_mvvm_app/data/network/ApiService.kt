package com.example.kotlin_mvvm_app.data.network

import com.example.kotlin_mvvm_app.data.repositories.model.search.SearchResult
import com.example.kotlin_mvvm_app.data.repositories.model.track.TrackItemResult
import com.example.kotlin_mvvm_app.data.repositories.model.track.TracksResult
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ApiService {

    @GET("search")
    suspend fun getSearch(
        @Header("Authorization") token: String,
        @Query("q") q : String,
        @Query("type") type : String,
        @Query("limit") limit : Int,
        @Query("offset") offset : Int
    ) : SearchResult

    @GET("me/tracks")
    suspend fun getLikedTracks(
        @Header("Authorization") token: String,
        @Query("limit") limit : Int,
        @Query("offset") offset : Int
    ) : TracksResult

}