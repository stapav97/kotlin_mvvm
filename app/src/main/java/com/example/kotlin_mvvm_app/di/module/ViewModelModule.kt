package com.example.kotlin_mvvm_app.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap



@Module
abstract class ViewModelModule {

//    @Binds
//    @IntoMap
//    @ViewModelKey(LoginViewModel::class)
//    abstract fun bindsLoginViewModel(vm: LoginViewModel): ViewModel
//
//    @Binds
//    @IntoMap
//    @ViewModelKey(SplashViewModel::class)
//    abstract fun bindsSplashViewModel(vm: SplashViewModel): ViewModel

    @Binds
    abstract fun bindDefaultViewModelFactory(factory: DefaultViewModelFactory): ViewModelProvider.Factory

}