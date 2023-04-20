package com.example.kotlin_mvvm_app.ui.first

import androidx.lifecycle.LifecycleOwner
import com.example.kotlin_mvvm_app.ui.MainViewCommandProcessor
import com.example.kotlin_mvvm_app.ui.base.BaseViewModel
import com.example.kotlin_mvvm_app.ui.base.commands.ViewCommandProcessor
import com.example.kotlin_mvvm_app.utils.logTag
import javax.inject.Inject

class FirstViewModel @Inject constructor(
    private val mMainCommands: MainViewCommandProcessor
) : BaseViewModel() {

    private val logTag = logTag()

    private val mCommands: ViewCommandProcessor<FirstFragment> = ViewCommandProcessor()
    fun observeCommands(owner: LifecycleOwner, view: FirstFragment) {
        mCommands.observe(owner, view)
    }
}