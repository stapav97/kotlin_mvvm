package com.example.kotlin_mvvm_app.utils

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator

//==============================================================================================
// *** Visibility ***
//==============================================================================================

fun View.gone(): View {
    if (visibility == View.GONE) return this

    visibility = View.GONE
    return this
}

fun View.show(): View {
    if (visibility == View.VISIBLE) return this

    visibility = View.VISIBLE
    return this
}

//==============================================================================================
// *** Misc ***
//==============================================================================================
fun RecyclerView.disableItemChangeAnimation() {
    (itemAnimator as? SimpleItemAnimator)?.supportsChangeAnimations = false
}