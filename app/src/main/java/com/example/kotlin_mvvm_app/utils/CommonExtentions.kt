package com.example.kotlin_mvvm_app.utils

fun Any.logTag(): String {
    return this::class.java.simpleName
}

fun String.tokenFormatter() : String{
    return "Bearer $this"
}

