package com.example.kotlin_mvvm_app.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.kotlin_mvvm_app.ui.first.FirstViewModel
import com.example.kotlin_mvvm_app.ui.fourth.FourthViewModel
import com.example.kotlin_mvvm_app.ui.login.LoginViewModel
import com.example.kotlin_mvvm_app.ui.search.SearchViewModel
import com.example.kotlin_mvvm_app.ui.splash.SplashViewModel
import com.example.kotlin_mvvm_app.ui.liked.LikedViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap



@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(FourthViewModel::class)
    abstract fun bindsFourthViewModel(vm: FourthViewModel): ViewModel
    @Binds
    @IntoMap
    @ViewModelKey(LikedViewModel::class)
    abstract fun bindsLikedViewModel(vm: LikedViewModel): ViewModel
    @Binds
    @IntoMap
    @ViewModelKey(SearchViewModel::class)
    abstract fun bindsSearchViewModel(vm: SearchViewModel): ViewModel
    @Binds
    @IntoMap
    @ViewModelKey(FirstViewModel::class)
    abstract fun bindsFirstViewModel(vm: FirstViewModel): ViewModel
    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    abstract fun bindsLoginViewModel(vm: LoginViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SplashViewModel::class)
    abstract fun bindsSplashViewModel(vm: SplashViewModel): ViewModel

    @Binds
    abstract fun bindDefaultViewModelFactory(factory: DefaultViewModelFactory): ViewModelProvider.Factory

}