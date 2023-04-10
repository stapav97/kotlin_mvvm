package com.example.kotlin_mvvm_app.ui.first

import com.example.kotlin_mvvm_app.R
import com.example.kotlin_mvvm_app.databinding.FirstFragmentBinding
import com.example.kotlin_mvvm_app.ui.base.BaseFragment
import com.example.kotlin_mvvm_app.utils.binding.viewBinding

class FirstFragment : BaseFragment(R.layout.first_fragment) {

    private val binding: FirstFragmentBinding by viewBinding(FirstFragmentBinding::bind)
    private lateinit var mViewModel: FirstViewModel

    override fun initUI() {
        super.initUI()

        mViewModel = newViewModelWithArgs()

    }
}