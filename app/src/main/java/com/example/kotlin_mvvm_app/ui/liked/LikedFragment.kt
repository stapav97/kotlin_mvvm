package com.example.kotlin_mvvm_app.ui.liked

import android.widget.Toast
import com.example.kotlin_mvvm_app.R
import com.example.kotlin_mvvm_app.databinding.LikedFragmentBinding
import com.example.kotlin_mvvm_app.ui.base.BaseFragment
import com.example.kotlin_mvvm_app.utils.binding.viewBinding
import com.example.kotlin_mvvm_app.utils.wrappers.gone
import com.example.kotlin_mvvm_app.utils.wrappers.show

class LikedFragment : BaseFragment(R.layout.liked_fragment) {

    private val binding: LikedFragmentBinding by viewBinding(LikedFragmentBinding::bind)
    private lateinit var mViewModel: LikedViewModel

    override fun initUI() {
        super.initUI()

        mViewModel = newViewModelWithArgs()

        observeState()
    }

    private var mLastConsumedState: LikedViewModel.State? = null
    private fun observeState() {
        mViewModel.state().observe(viewLifecycleOwner) { state ->

            if (state.token.isNotEmpty() && mLastConsumedState?.token != state.token) {
                mViewModel.getLikedTracks()
            }

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

    private fun shouldUpdateProgress(state: LikedViewModel.State): Boolean {
        return state.isProgress != mLastConsumedState?.isProgress
    }

    private fun shouldShowError(state: LikedViewModel.State): Boolean {
        if (state.errorMessage.isBlank()) return false
        if (mLastConsumedState == null) return true

        return mLastConsumedState?.errorMessage != state.errorMessage
    }

    private fun showErrorMessage(errorMessage: String) {
        Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show()
        mViewModel.errorMessageShown()
    }
}