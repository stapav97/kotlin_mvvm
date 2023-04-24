package com.example.kotlin_mvvm_app.ui.search.searchList

import com.example.kotlin_mvvm_app.ui.base.recyclerview.items.RecyclerViewItem

data class SearchListItem(
    val id: String,
    val name: String,
    val type: String,
    val image: String?
) : RecyclerViewItem {

    override fun getViewType() = SearchAdapter.ViewType.ARTIST

    override fun isSame(another: RecyclerViewItem): Boolean {
        if (another !is SearchListItem) return false

        if (id != another.id) return false
        if (name != another.name) return false
        if (type != another.type) return false
        if (image != another.image) return false
        return true
    }

    override fun isSameContent(another: RecyclerViewItem): Boolean {
        if (another !is SearchListItem) return false

        return true
    }
}
