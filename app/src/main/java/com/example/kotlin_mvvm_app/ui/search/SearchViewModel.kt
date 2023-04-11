package com.example.kotlin_mvvm_app.ui.search

import com.example.kotlin_mvvm_app.ui.MainViewCommandProcessor
import com.example.kotlin_mvvm_app.ui.base.BaseViewModel
import com.example.kotlin_mvvm_app.utils.logTag
import javax.inject.Inject

class SearchViewModel @Inject constructor(
    private val mMainCommands: MainViewCommandProcessor
) : BaseViewModel() {

    private val logTag = logTag()

}