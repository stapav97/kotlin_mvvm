package com.example.kotlin_mvvm_app

import android.app.Application
import com.example.kotlin_mvvm_app.di.AppComponent
import com.example.kotlin_mvvm_app.di.DaggerAppComponent

class App : Application() {

    var component: AppComponent? = null

    companion object {
        const val PACKAGE_NAME = "kotlin_mvvm_app"
        lateinit var instance: App
    }

    override fun onCreate() {
        super.onCreate()

        instance = this
        component = DaggerAppComponent.builder().also {
            it.context(this)
        }.build()

    }
}