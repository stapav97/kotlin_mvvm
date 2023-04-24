package com.example.kotlin_mvvm_app.data.db.converters

import androidx.room.TypeConverter
import com.example.kotlin_mvvm_app.data.repositories.model.artist.Image
import com.google.gson.Gson

class SearchHistoryConverter {

    @TypeConverter
    fun fromListToString(list: List<String>): String = list.toString()

    @TypeConverter
    fun toListFromString(stringList: String) = stringList.split(",").toList()

    @TypeConverter
    fun fromImagesToJson(images: List<Image>): String = Gson().toJson(images)

    @TypeConverter
    fun toImagesFromJson(json: String): List<Image> = Gson().fromJson(json, Array<Image>::class.java).toList()

}