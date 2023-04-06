package com.example.kotlin_mvvm_app.di.module

import androidx.lifecycle.ViewModel
import dagger.MapKey
import kotlin.reflect.KClass

@MapKey
internal annotation class ViewModelKey(val value: KClass<out ViewModel>)
