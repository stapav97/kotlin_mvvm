package com.example.kotlin_mvvm_app.ui.search

import android.widget.SearchView
import android.widget.Toast
import com.example.kotlin_mvvm_app.R
import com.example.kotlin_mvvm_app.databinding.SearchFragmentBinding
import com.example.kotlin_mvvm_app.ui.base.BaseFragment
import com.example.kotlin_mvvm_app.utils.binding.viewBinding
import com.example.kotlin_mvvm_app.utils.wrappers.gone
import com.example.kotlin_mvvm_app.utils.wrappers.show

class SearchFragment : BaseFragment(R.layout.search_fragment) {

    private val binding: SearchFragmentBinding by viewBinding(SearchFragmentBinding::bind)
    private lateinit var mViewModel: SearchViewModel

    override fun initUI() {
        super.initUI()

        mViewModel = newViewModelWithArgs()

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextChange(query: String): Boolean {
                mViewModel.onSearchTextChanged(query)
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                TODO("Not yet implemented")
            }
        })

        mViewModel.observeCommands(viewLifecycleOwner, this)
        observeState()
    }

    private var mLastConsumedState: SearchViewModel.State? = null

    private fun observeState() {
        mViewModel.state().observe(viewLifecycleOwner) { state ->

            if (shouldUpdateProgress(state)) {
                if (state.isProgress) {
                    binding.progressBar.show()
                } else {
                    binding.progressBar.gone()
                }

            }
            if (shouldShowError(state))
                showErrorMessage(state.errorMessage)
            mLastConsumedState = state
        }
    }

    private fun shouldUpdateProgress(state: SearchViewModel.State): Boolean {
        return state.isProgress != mLastConsumedState?.isProgress
    }

    private fun shouldShowError(state: SearchViewModel.State): Boolean {
        if (state.errorMessage.isBlank()) return false
        if (mLastConsumedState == null) return true

        return mLastConsumedState?.errorMessage != state.errorMessage
    }

    private fun showErrorMessage(errorMessage: String) {
        Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show()
        mViewModel.errorMessageShown()
    }
}