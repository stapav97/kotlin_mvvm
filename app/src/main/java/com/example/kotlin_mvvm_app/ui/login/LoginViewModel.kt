package com.example.kotlin_mvvm_app.ui.login

import com.example.kotlin_mvvm_app.ui.MainViewCommandProcessor
import com.example.kotlin_mvvm_app.ui.base.BaseViewModel
import com.example.kotlin_mvvm_app.ui.base.commands.enqueue
import com.example.kotlin_mvvm_app.utils.logTag
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val mMainCommands: MainViewCommandProcessor
) : BaseViewModel() {

    private val logTag = logTag()

    fun onLoginButtonClicked(){
        mMainCommands.enqueue { it.navigateToSpotifyLogin() }
    }
}