package com.example.kotlin_mvvm_app.ui.second

import com.example.kotlin_mvvm_app.R
import com.example.kotlin_mvvm_app.databinding.SecondFragmentBinding
import com.example.kotlin_mvvm_app.ui.base.BaseFragment
import com.example.kotlin_mvvm_app.utils.binding.viewBinding

class SecondFragment : BaseFragment(R.layout.second__fragment) {

    private val binding: SecondFragmentBinding by viewBinding(SecondFragmentBinding::bind)
    private lateinit var mViewModel: SecondViewModel

    override fun initUI() {
        super.initUI()

        mViewModel = newViewModelWithArgs()

    }
}