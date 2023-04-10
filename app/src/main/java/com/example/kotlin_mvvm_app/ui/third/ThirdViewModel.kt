package com.example.kotlin_mvvm_app.ui.third

import com.example.kotlin_mvvm_app.ui.MainViewCommandProcessor
import com.example.kotlin_mvvm_app.ui.base.BaseViewModel
import com.example.kotlin_mvvm_app.utils.logTag
import javax.inject.Inject

class ThirdViewModel @Inject constructor(
    private val mMainCommands: MainViewCommandProcessor
) : BaseViewModel() {

    private val logTag = logTag()

}