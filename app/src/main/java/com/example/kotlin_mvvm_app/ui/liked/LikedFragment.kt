package com.example.kotlin_mvvm_app.ui.liked

import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlin_mvvm_app.R
import com.example.kotlin_mvvm_app.databinding.LikedFragmentBinding
import com.example.kotlin_mvvm_app.ui.base.BaseFragment
import com.example.kotlin_mvvm_app.ui.base.newViewModelWithArgs
import com.example.kotlin_mvvm_app.ui.base.recyclerview.CustomLinearLayoutManager
import com.example.kotlin_mvvm_app.ui.base.recyclerview.applyDiffUtil
import com.example.kotlin_mvvm_app.ui.liked.list.TrackListItem
import com.example.kotlin_mvvm_app.ui.liked.list.TrackListViewHolder
import com.example.kotlin_mvvm_app.ui.liked.list.TracksAdapter
import com.example.kotlin_mvvm_app.utils.binding.viewBinding
import com.example.kotlin_mvvm_app.utils.disableItemChangeAnimation
import com.example.kotlin_mvvm_app.utils.gone
import com.example.kotlin_mvvm_app.utils.show

class LikedFragment : BaseFragment(R.layout.liked_fragment) {

    private val binding: LikedFragmentBinding by viewBinding(LikedFragmentBinding::bind)
    private lateinit var mViewModel: LikedViewModel
    private lateinit var mTracksAdapter: TracksAdapter

    override fun initUI() {
        super.initUI()
        mViewModel = newViewModelWithArgs()

        mTracksAdapter = TracksAdapter(
            contractTrack = object : TrackListViewHolder.Contract{
                override fun onClickItem(item: TrackListItem) {
                    if (mLastConsumedState != null && !mLastConsumedState!!.isProgress)
                        mViewModel.onListItemClick(item)
                }
            }
        )

        val layoutManager = CustomLinearLayoutManager(requireContext())
        binding.likedTrackRecyclerView.layoutManager = layoutManager
        binding.likedTrackRecyclerView.adapter = mTracksAdapter
        binding.likedTrackRecyclerView.disableItemChangeAnimation()
        binding.likedTrackRecyclerView.setRecycledViewPool(RecyclerView.RecycledViewPool().apply {
            setMaxRecycledViews(TracksAdapter.ViewType.TRACK, 20)
        })

        binding.likedTrackRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val pastVisibleItems = layoutManager.findFirstVisibleItemPosition()
                if (visibleItemCount + pastVisibleItems >= totalItemCount) {
                    mViewModel.onScrolledToEnd()
                }
            }
        })

        mViewModel.observeCommands(viewLifecycleOwner, this)
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

            mTracksAdapter.applyDiffUtil(state.trackList)

            if (state.hasTrackItems()) {
                binding.emptyList.gone()
            } else {
                binding.emptyList.show()
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