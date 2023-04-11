package com.example.kotlin_mvvm_app.ui.fourth

import com.example.kotlin_mvvm_app.R
import com.example.kotlin_mvvm_app.databinding.FourthFragmentBinding
import com.example.kotlin_mvvm_app.ui.base.BaseFragment
import com.example.kotlin_mvvm_app.utils.binding.viewBinding

class FourthFragment : BaseFragment(R.layout.fourth_fragment) {

    private val binding: FourthFragmentBinding by viewBinding(FourthFragmentBinding::bind)
    private lateinit var mViewModel: FourthViewModel

    override fun initUI() {
        super.initUI()

        mViewModel = newViewModelWithArgs()

    }
}