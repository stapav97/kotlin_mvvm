package com.example.kotlin_mvvm_app.ui.liked.list

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlin_mvvm_app.ui.base.recyclerview.BaseRecyclerViewAdapter
import com.example.kotlin_mvvm_app.ui.base.recyclerview.items.RecyclerViewItem

class TracksAdapter(
    var contractTrack: TrackListViewHolder.Contract? = null,
) : BaseRecyclerViewAdapter<RecyclerViewItem>() {

    object ViewType {
        const val TRACK = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ViewType.TRACK -> TrackListViewHolder.newInstance(mInflater!!, parent, contractTrack)

            else -> super.onCreateViewHolder(parent, viewType)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            ViewType.TRACK -> (holder as TrackListViewHolder).bind(getItem(position) as TrackListItem)
            else -> super.onBindViewHolder(holder, position)
        }
    }
}