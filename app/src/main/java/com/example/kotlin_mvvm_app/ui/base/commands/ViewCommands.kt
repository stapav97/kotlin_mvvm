package com.example.kotlin_mvvm_app.ui.base.commands

import androidx.annotation.MainThread
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner

interface Command<View> {
    fun execute(view: View)
}

@MainThread
inline fun <reified V> ViewCommandProcessor<V>.enqueue(crossinline command: (view: V) -> Unit) {
    enqueue(object : Command<V> {
        override fun execute(view: V) {
            command.invoke(view)
        }
    })
}

/**
 * Lifecycle-aware action processor. Executes actions when View is [Lifecycle.State.RESUMED]
 * or put them into queue and do it later.
 *
 * For stateless actions (Side Effects) e.g. showToast or hideKeyboard.
 * Replacement for so called "SingleLiveEvent" in MVVM
 */
open class ViewCommandProcessor<View> : DefaultLifecycleObserver {

    private val mCommands = mutableListOf<Command<View>>()
    private var mOwner: LifecycleOwner? = null
    private var mView: View? = null


    @MainThread
    fun enqueue(command: Command<View>) {
        mCommands.add(command)

        if (mOwner?.lifecycle?.currentState?.isAtLeast(Lifecycle.State.RESUMED) == true) {
            executeCommands()
        }
    }

    private fun executeCommands() {
        if (mCommands.isEmpty()) return
        val localList = mCommands
        val iterator = localList.iterator()
        while (iterator.hasNext()) {
            val command = iterator.next()
            if (mView != null) {
                command.execute(mView!!)
            }
        }
        mCommands.clear()
    }

    fun observe(owner: LifecycleOwner, view: View) {
        if (owner.lifecycle.currentState == Lifecycle.State.DESTROYED) {
            return
        }

        if (mOwner != null) {
            mOwner!!.lifecycle.removeObserver(this)
            mOwner = null
            mView = null
        }

        mOwner = owner
        mView = view
        owner.lifecycle.addObserver(this)
    }

    override fun onResume(owner: LifecycleOwner) {
        executeCommands()
    }

    override fun onDestroy(owner: LifecycleOwner) {
        owner.lifecycle.removeObserver(this)
        mOwner = null
        mView = null
    }
}