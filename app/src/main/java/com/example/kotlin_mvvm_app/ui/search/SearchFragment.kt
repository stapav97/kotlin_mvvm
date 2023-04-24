package com.example.kotlin_mvvm_app.ui.search

import android.content.Context
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlin_mvvm_app.R
import com.example.kotlin_mvvm_app.databinding.SearchFragmentBinding
import com.example.kotlin_mvvm_app.ui.base.BaseFragment
import com.example.kotlin_mvvm_app.ui.base.newViewModelWithArgs
import com.example.kotlin_mvvm_app.ui.base.recyclerview.CustomLinearLayoutManager
import com.example.kotlin_mvvm_app.ui.base.recyclerview.applyDiffUtil
import com.example.kotlin_mvvm_app.ui.liked.list.TracksAdapter
import com.example.kotlin_mvvm_app.ui.search.searchList.SearchAdapter
import com.example.kotlin_mvvm_app.ui.search.searchList.SearchListItem
import com.example.kotlin_mvvm_app.ui.search.searchList.SearchListViewHolder
import com.example.kotlin_mvvm_app.utils.DebouncingQueryTextListener
import com.example.kotlin_mvvm_app.utils.binding.viewBinding
import com.example.kotlin_mvvm_app.utils.disableItemChangeAnimation
import com.example.kotlin_mvvm_app.utils.gone
import com.example.kotlin_mvvm_app.utils.show

class SearchFragment : BaseFragment(R.layout.search_fragment) {

    private val binding: SearchFragmentBinding by viewBinding(SearchFragmentBinding::bind)
    private lateinit var mViewModel: SearchViewModel
    private lateinit var searchAdapter: SearchAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mViewModel = newViewModelWithArgs()
    }

    override fun initUI() {
        super.initUI()

        searchAdapter = SearchAdapter(
            contractSearch = object : SearchListViewHolder.Contract {
                override fun onClickItem(item: SearchListItem) {
                    mViewModel.onSearchListItemClick(item)
                }
            }
        )

        val layoutManager = CustomLinearLayoutManager(requireContext())
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = searchAdapter
        binding.recyclerView.disableItemChangeAnimation()
        binding.recyclerView.setRecycledViewPool(RecyclerView.RecycledViewPool().apply {
            setMaxRecycledViews(TracksAdapter.ViewType.TRACK, 20)
        })

        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val pastVisibleItems = layoutManager.findFirstVisibleItemPosition()
                if (visibleItemCount + pastVisibleItems >= totalItemCount) {
                    mViewModel.onScrolledToEnd()
                }
            }
        })

        binding.searchView.setOnQueryTextListener(
            DebouncingQueryTextListener(
                lifecycle
            ) { newText ->
                newText?.let {
                    mViewModel.onSearchTextChanged(it)
                }
            }
        )

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

            searchAdapter.applyDiffUtil(state.searchList)

            if (state.hasSearchItems()) {
                binding.emptyList.gone()
            } else {
                binding.emptyList.show()
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