package com.example.kotlin_mvvm_app.ui.splash

import androidx.navigation.findNavController
import com.example.kotlin_mvvm_app.R
import com.example.kotlin_mvvm_app.databinding.SplashFragmentBinding
import com.example.kotlin_mvvm_app.ui.base.BaseFragment
import com.example.kotlin_mvvm_app.utils.Reporter
import com.example.kotlin_mvvm_app.utils.binding.viewBinding
import com.example.kotlin_mvvm_app.utils.logTag


class SplashFragment : BaseFragment(R.layout.splash_fragment) {

    private val binding: SplashFragmentBinding by viewBinding(SplashFragmentBinding::bind)
    private val logTag = logTag()

    private lateinit var mViewModel: SplashViewModel

    override fun initUI() {
        super.initUI()
        mViewModel = newViewModelWithArgs()

        observeState()
        navigateToLogin()
    }

    private fun observeState() {

    }

    private fun navigateToLogin() {
        Reporter.appAction(logTag, "navigateToLogin")

        val action = SplashFragmentDirections.actionSplashFragmentToLoginFragment()
        binding.root.findNavController().navigate(action)
    }
}