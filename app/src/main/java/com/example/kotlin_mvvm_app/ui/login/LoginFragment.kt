package com.example.kotlin_mvvm_app.ui.login

import com.example.kotlin_mvvm_app.R
import com.example.kotlin_mvvm_app.databinding.LoginFragmentBinding
import com.example.kotlin_mvvm_app.ui.base.BaseFragment
import com.example.kotlin_mvvm_app.utils.binding.viewBinding

class LoginFragment : BaseFragment(R.layout.login_fragment) {

    private val binding: LoginFragmentBinding by viewBinding(LoginFragmentBinding::bind)
    private lateinit var mViewModel: LoginViewModel

    override fun initUI() {
        super.initUI()

        mViewModel = newViewModelWithArgs()

        binding.loginButton.setOnClickListener {
            mViewModel.onLoginButtonClicked()
        }
    }
}