package com.example.kotlin_mvvm_app.ui.base

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.kotlin_mvvm_app.App
import com.example.kotlin_mvvm_app.utils.Reporter
import com.example.kotlin_mvvm_app.utils.logTag
import javax.inject.Inject

abstract class BaseFragment(@LayoutRes layout: Int) : Fragment(layout) {
    private val logTag = logTag()

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    protected open fun initUI() = Unit
    override fun onAttach(context: Context) {
        super.onAttach(context)

        val app = requireActivity().application as App
        app.component!!.inject(this)

        Reporter.appAction(logTag, "onAttach")
    }

    override fun onDetach() {
        super.onDetach()
        Reporter.appAction(logTag, "onDetach")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Reporter.appAction(logTag, "onCreate")
    }

    override fun onResume() {
        super.onResume()
        Reporter.appAction(logTag, "onResume")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Reporter.appAction(logTag, "onViewCreated")

        initUI()
    }

    override fun onPause() {
        super.onPause()
        Reporter.appAction(logTag, "onPause")
    }

    override fun onDestroy() {
        super.onDestroy()
        Reporter.appAction(logTag, "onDestroy")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Reporter.appAction(logTag, "onDestroyView")
    }

    /**
     * Instantiate ViewModel and set common arguments to it
     */
    inline fun <reified VM : BaseViewModel> BaseFragment.newViewModelWithArgs(): VM {
        return ViewModelProvider(this, viewModelFactory).get(VM::class.java)
    }
}