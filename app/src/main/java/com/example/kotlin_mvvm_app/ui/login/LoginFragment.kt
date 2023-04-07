package com.example.kotlin_mvvm_app.ui.login

import com.example.kotlin_mvvm_app.R
import com.example.kotlin_mvvm_app.ui.base.BaseFragment
import com.example.kotlin_mvvm_app.utils.logTag

class LoginFragment : BaseFragment(R.layout.login_fragment) {

    private val logTag = logTag()

    private lateinit var mViewModel: LoginViewModel

    override fun initUI() {
        super.initUI()
        mViewModel = newViewModelWithArgs()


    }
}