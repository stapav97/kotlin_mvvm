package com.example.kotlin_mvvm_app.ui.third

import com.example.kotlin_mvvm_app.R
import com.example.kotlin_mvvm_app.databinding.ThirdFragmentBinding
import com.example.kotlin_mvvm_app.ui.base.BaseFragment
import com.example.kotlin_mvvm_app.utils.binding.viewBinding

class ThirdFragment : BaseFragment(R.layout.third_fragment) {

    private val binding: ThirdFragmentBinding by viewBinding(ThirdFragmentBinding::bind)
    private lateinit var mViewModel: ThirdViewModel

    override fun initUI() {
        super.initUI()

        mViewModel = newViewModelWithArgs()

    }
}