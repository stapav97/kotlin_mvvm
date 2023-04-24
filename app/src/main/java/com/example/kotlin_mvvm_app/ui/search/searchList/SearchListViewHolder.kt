package com.example.kotlin_mvvm_app.ui.search.searchList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kotlin_mvvm_app.databinding.SearchlistItemBinding
import com.example.kotlin_mvvm_app.utils.Logger
import com.example.kotlin_mvvm_app.utils.logTag

class SearchListViewHolder(
    private val mBinding: SearchlistItemBinding,
    private val mContract: Contract?,
) : RecyclerView.ViewHolder(mBinding.root) {

    private val logTag = logTag()

    interface Contract {
        fun onClickItem(item: SearchListItem)
    }

    companion object {
        fun newInstance(inflater: LayoutInflater, parent: ViewGroup, contract: Contract?): SearchListViewHolder {
            return SearchListViewHolder(SearchlistItemBinding.inflate(inflater, parent, false), contract)
        }
    }

    //==============================================================================================
    // *** UI ***
    //==============================================================================================
    private var mLastItem: SearchListItem? = null

    fun bind(item: SearchListItem) {

        if (item !== mLastItem) {
            Logger.d(logTag, "bind ${item.id}")
            renderItem(item)
        } else {
            Logger.d(logTag, "NOT bind ${item.id}")
        }

        mLastItem = item
    }

    private fun renderItem(item: SearchListItem) {
        val context = mBinding.root.context
        mBinding.root.setOnClickListener {
            mContract?.onClickItem(item)
        }
        mBinding.name.text = item.name
        mBinding.type.text = item.type
        Glide.with(context).load(item.image).circleCrop().into(mBinding.image);
    }
}
