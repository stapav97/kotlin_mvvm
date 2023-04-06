package com.example.kotlin_mvvm_app.ui.splash

import com.example.kotlin_mvvm_app.R
import com.example.kotlin_mvvm_app.ui.base.BaseFragment
import com.example.kotlin_mvvm_app.utils.logTag


class SplashFragment : BaseFragment(R.layout.splash_fragment) {

   
    private val logTag = logTag()

    private lateinit var mViewModel: SplashViewModel


    override fun initUI() {
        super.initUI()
        mViewModel = newViewModelWithArgs()


    }




}