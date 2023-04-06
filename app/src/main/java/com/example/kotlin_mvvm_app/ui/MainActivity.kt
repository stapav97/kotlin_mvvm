package com.example.kotlin_mvvm_app.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlin_mvvm_app.App
import com.example.kotlin_mvvm_app.utils.Reporter
import com.example.kotlin_mvvm_app.utils.logTag
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    private val logTag = logTag()

    @Inject
    lateinit var mViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        val isProcessRestored = savedInstanceState != null
        Reporter.appAction(logTag, "onCreate isProcessRestored: $isProcessRestored")

        super.onCreate(savedInstanceState)

        (application as App).component!!.inject(this)

    }

}