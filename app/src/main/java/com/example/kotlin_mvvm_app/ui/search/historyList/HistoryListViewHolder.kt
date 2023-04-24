package com.example.kotlin_mvvm_app.ui.search.historyList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kotlin_mvvm_app.databinding.HistorylistItemBinding
import com.example.kotlin_mvvm_app.utils.Logger
import com.example.kotlin_mvvm_app.utils.logTag

class HistoryListViewHolder(
    private val mBinding: HistorylistItemBinding,
    private val mContract: Contract?,

    ) : RecyclerView.ViewHolder(mBinding.root) {
    private val logTag = logTag()

    interface Contract {
        fun onClickItem(item: HistoryListItem)
    }

    companion object {
        fun newInstance(inflater: LayoutInflater, parent: ViewGroup, contract: Contract?): HistoryListViewHolder {
            return HistoryListViewHolder(HistorylistItemBinding.inflate(inflater, parent, false), contract)
        }
    }

    //==============================================================================================
    // *** UI ***
    //==============================================================================================

    private var mLastItem: HistoryListItem? = null

    fun bind(item: HistoryListItem) {

        if (item !== mLastItem) {
            Logger.d(logTag, "bind ${item.id}")
            renderItem(item)
        } else {
            Logger.d(logTag, "NOT bind ${item.id}")
        }

        mLastItem = item
    }

    private fun renderItem(item: HistoryListItem) {
        val context = mBinding.root.context
        mBinding.root.setOnClickListener {
            mContract?.onClickItem(item)
        }
        mBinding.name.text = item.name
        mBinding.type.text = item.type
        Glide.with(context).load(item.image).into(mBinding.image);
    }
}