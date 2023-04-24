package com.example.kotlin_mvvm_app.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.kotlin_mvvm_app.data.db.converters.SearchHistoryConverter

@Entity(tableName = "search_histories")
@TypeConverters(SearchHistoryConverter::class)
data class SearchHistory(
    @PrimaryKey
    @ColumnInfo(name = "id") val id: String,

    @ColumnInfo(name = "name") val name: String,

    @ColumnInfo(name = "type") val type: String,

    @ColumnInfo(name = "image") val image: String?,
)
