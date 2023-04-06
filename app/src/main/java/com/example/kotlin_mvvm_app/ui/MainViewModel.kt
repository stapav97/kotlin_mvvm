package com.example.kotlin_mvvm_app.ui

import com.example.kotlin_mvvm_app.ui.base.BaseViewModel
import com.example.kotlin_mvvm_app.utils.logTag
import com.example.kotlin_mvvm_app.ui.base.commands.ViewCommandProcessor
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainViewCommandProcessor @Inject constructor() : ViewCommandProcessor<MainActivity>()

@Singleton
class MainViewModel @Inject constructor(
    private val mCommands: MainViewCommandProcessor
) : BaseViewModel() {

    private val logTag = logTag()

}