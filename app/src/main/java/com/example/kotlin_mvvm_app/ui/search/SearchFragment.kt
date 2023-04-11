package com.example.kotlin_mvvm_app.ui.search

import com.example.kotlin_mvvm_app.R
import com.example.kotlin_mvvm_app.databinding.SearchFragmentBinding
import com.example.kotlin_mvvm_app.ui.base.BaseFragment
import com.example.kotlin_mvvm_app.utils.binding.viewBinding

class SearchFragment : BaseFragment(R.layout.search_fragment) {

    private val binding: SearchFragmentBinding by viewBinding(SearchFragmentBinding::bind)
    private lateinit var mViewModel: SearchViewModel

    override fun initUI() {
        super.initUI()

        mViewModel = newViewModelWithArgs()

    }
}