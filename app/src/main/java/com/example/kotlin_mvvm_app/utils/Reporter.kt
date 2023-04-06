package com.example.kotlin_mvvm_app.utils

object Reporter {

    fun userAction(where: String, description: String) {
        Logger.d(where, "[USER_ACTION] $description")
    }

    fun appAction(where: String, description: String) {
        Logger.d(where, "[APP_ACTION] $description")
    }
}