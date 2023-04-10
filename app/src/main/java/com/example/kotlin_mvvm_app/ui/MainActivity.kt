package com.example.kotlin_mvvm_app.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.kotlin_mvvm_app.App
import com.example.kotlin_mvvm_app.BuildConfig
import com.example.kotlin_mvvm_app.R
import com.example.kotlin_mvvm_app.databinding.MainActivityBinding
import com.example.kotlin_mvvm_app.utils.Reporter
import com.example.kotlin_mvvm_app.utils.logTag
import com.example.kotlin_mvvm_app.utils.wrappers.TextResource
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    private val logTag = logTag()

    lateinit var navController: NavController

    @Inject
    lateinit var mViewModel: MainViewModel
    private lateinit var binding: MainActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        val isProcessRestored = savedInstanceState != null
        Reporter.appAction(logTag, "onCreate isProcessRestored: $isProcessRestored")

        super.onCreate(savedInstanceState)

        (application as App).component!!.inject(this)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
        navController = navHostFragment.navController

        initUI()
        mViewModel.observeCommands(this, this)
    }

    override fun onResume() {
        super.onResume()
        Reporter.appAction(logTag, "onResume")
        mViewModel.checkCodeOnResume()
    }

    private fun initUI() {
        observeState()
    }

    private fun observeState() {
        mViewModel.state().observe(this) { state ->
            if (state.token != "" ) {
                Log.d("TOKEN TOKEN TOKEN:", state.token)
//                mViewModel.saveUserToken()
            }
        }
    }

    fun checkIsCodeExist() {
        Reporter.appAction(logTag, "checkIsCodeExist")

        val uri = intent!!.data
        if (uri != null && uri.toString().startsWith(BuildConfig.AUTHORIZATION_CALLBACK_URL)) {
            val code = uri.getQueryParameter("code")
            if (code != null) {
                Reporter.appAction(logTag, code)
                mViewModel.onCodeRetrieve(code)
            } else if (uri.getQueryParameter("error") != null) {
                val error = uri.getQueryParameter("error")
                Reporter.appAction(logTag, "ERROR")
                mViewModel.onCodeErrorRetrieve(error.orEmpty())
            }
        }
    }

    fun handleErrorWithToastMessage(errorMessage: TextResource){
        Reporter.appAction(logTag, "handleErrorWithToastMessage")
        Toast.makeText(applicationContext, errorMessage.toValue(applicationContext), Toast.LENGTH_LONG).show()
    }

    fun navigateToSpotifyLogin() {
        Reporter.appAction(logTag, "navigateToSpotifyLogin")

        startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://accounts.spotify.com/authorize?client_id=${BuildConfig.CLIENT_ID}&redirect_uri=${BuildConfig.AUTHORIZATION_CALLBACK_URL}&response_type=code")
            )
        )
    }

}