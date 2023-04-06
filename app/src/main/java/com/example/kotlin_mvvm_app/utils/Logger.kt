package com.example.kotlin_mvvm_app.utils

import android.util.Log
import com.example.kotlin_mvvm_app.BuildConfig

object Logger {
    private const val TAG = "kotlin_mvvm_app.mylog"

    fun d(tag: String, message: String) {
        if (!BuildConfig.DEBUG) return

        Log.d(TAG, "$tag $message")
    }

}