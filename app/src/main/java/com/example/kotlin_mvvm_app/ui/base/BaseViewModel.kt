package com.example.kotlin_mvvm_app.ui.base

import androidx.lifecycle.ViewModel
import com.example.kotlin_mvvm_app.App
import com.example.kotlin_mvvm_app.utils.ViewModelTracker


open class BaseViewModel : ViewModel() {

    private val mViewModelTracker: ViewModelTracker = App.instance.component!!.provideViewModelTracker()

    override fun onCleared() {
        mViewModelTracker.onVmDestroyed(this)
        super.onCleared()
    }
}