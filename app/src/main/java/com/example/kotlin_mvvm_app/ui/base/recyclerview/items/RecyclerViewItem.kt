package com.example.kotlin_mvvm_app.ui.base.recyclerview.items

interface RecyclerViewItem {

    fun getViewType(): Int

    fun isSame(another: RecyclerViewItem): Boolean

    fun isSameContent(another: RecyclerViewItem): Boolean
}