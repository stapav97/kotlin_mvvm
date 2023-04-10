package com.example.kotlin_mvvm_app.di

import android.content.Context
import com.example.kotlin_mvvm_app.di.module.AppModule
import com.example.kotlin_mvvm_app.di.module.NetworkModule
import com.example.kotlin_mvvm_app.di.module.ViewModelModule
import com.example.kotlin_mvvm_app.ui.MainActivity
import com.example.kotlin_mvvm_app.ui.MainViewModel
import com.example.kotlin_mvvm_app.ui.base.BaseFragment
import com.example.kotlin_mvvm_app.utils.ViewModelTracker
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        NetworkModule::class,
        ViewModelModule::class
    ]
)
interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun context(context: Context)
        fun build(): AppComponent
    }

    fun provideMainViewModel(): MainViewModel
    fun provideViewModelTracker(): ViewModelTracker

    fun inject(entity: MainActivity)
    fun inject(entity: BaseFragment)
}