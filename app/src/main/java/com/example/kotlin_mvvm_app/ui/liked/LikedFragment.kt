package com.example.kotlin_mvvm_app.ui.liked

import com.example.kotlin_mvvm_app.R
import com.example.kotlin_mvvm_app.databinding.LikedFragmentBinding
import com.example.kotlin_mvvm_app.ui.base.BaseFragment
import com.example.kotlin_mvvm_app.utils.binding.viewBinding

class LikedFragment : BaseFragment(R.layout.liked_fragment) {

    private val binding: LikedFragmentBinding by viewBinding(LikedFragmentBinding::bind)
    private lateinit var mViewModel: LikedViewModel

    override fun initUI() {
        super.initUI()

        mViewModel = newViewModelWithArgs()

    }
}