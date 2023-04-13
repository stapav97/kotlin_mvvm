package com.example.kotlin_mvvm_app.ui.liked.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlin_mvvm_app.databinding.TracklistItemBinding
import com.example.kotlin_mvvm_app.utils.Logger
import com.example.kotlin_mvvm_app.utils.logTag

class TrackListViewHolder(
    private val mBinding: TracklistItemBinding,
    private val mContract: Contract?,
) : RecyclerView.ViewHolder(mBinding.root) {

    private val logTag = logTag()

    init {
        Logger.d(logTag, "onCreate  ${hashCode()}")
    }

    interface Contract {
        fun onClickItem(item: TrackListItem)
    }

    companion object {
        fun newInstance(inflater: LayoutInflater, parent: ViewGroup, contract: Contract?): TrackListViewHolder {
            return TrackListViewHolder(TracklistItemBinding.inflate(inflater, parent, false), contract)
        }
    }

    //==============================================================================================
    // *** UI ***
    //==============================================================================================
    private var mLastItem: TrackListItem? = null

    fun bind(item: TrackListItem) {

        if (item !== mLastItem) {
            Logger.d(logTag, "bind ${item.id}")
            renderItem(item)
        } else {
            Logger.d(logTag, "NOT bind ${item.id}")
        }

        mLastItem = item
    }

    private fun renderItem(item: TrackListItem) {
        val context = mBinding.root.context
        mBinding.root.setOnClickListener {
            mContract?.onClickItem(item)
        }
        mBinding.id.text = item.id
        mBinding.name.text = item.name
        mBinding.type.text = item.type

    }
}