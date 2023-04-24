package com.example.kotlin_mvvm_app.ui.search.historyList

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlin_mvvm_app.ui.base.recyclerview.BaseRecyclerViewAdapter
import com.example.kotlin_mvvm_app.ui.base.recyclerview.items.RecyclerViewItem

class HistoryAdapter(
    var contractHistory: HistoryListViewHolder.Contract? = null
) : BaseRecyclerViewAdapter<RecyclerViewItem>() {

    object ViewType {
        const val ARTIST = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ViewType.ARTIST -> HistoryListViewHolder.newInstance(mInflater!!, parent, contractHistory)
            else -> super.onCreateViewHolder(parent, viewType)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            ViewType.ARTIST -> (holder as HistoryListViewHolder).bind(getItem(position) as HistoryListItem)
        }
    }
}