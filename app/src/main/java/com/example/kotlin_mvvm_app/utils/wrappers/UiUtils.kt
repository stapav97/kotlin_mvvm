package com.example.kotlin_mvvm_app.utils.wrappers

import android.view.View

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