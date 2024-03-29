package com.example.kotlin_mvvm_app.ui.liked.list

import com.example.kotlin_mvvm_app.ui.base.recyclerview.items.RecyclerViewItem

data class TrackListItem (
    val id: String,
    val name: String,
    val type: String
) : RecyclerViewItem {

    override fun getViewType() = TracksAdapter.ViewType.TRACK

    override fun isSame(another: RecyclerViewItem): Boolean {
        if (another !is TrackListItem) return false

        if (id != another.id) return false
        if (name != another.name) return false
        if (type != another.type) return false

        return true
    }

    override fun isSameContent(another: RecyclerViewItem): Boolean {
        if (another !is TrackListItem) return false

        return true
    }
}