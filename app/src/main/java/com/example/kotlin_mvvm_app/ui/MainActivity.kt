package com.example.kotlin_mvvm_app.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.example.kotlin_mvvm_app.App
import com.example.kotlin_mvvm_app.BuildConfig
import com.example.kotlin_mvvm_app.R
import com.example.kotlin_mvvm_app.databinding.MainActivityBinding
import com.example.kotlin_mvvm_app.utils.Reporter
import com.example.kotlin_mvvm_app.utils.logTag
import com.example.kotlin_mvvm_app.utils.wrappers.TextResource
import com.google.android.material.bottomnavigation.BottomNavigationView
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    private val logTag = logTag()

    lateinit var navController: NavController
    lateinit var bottomNavigationView: BottomNavigationView

    @Inject
    lateinit var mViewModel: MainViewModel
    private lateinit var binding: MainActivityBinding

    private var mLastConsumedState: MainViewModel.State? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        val isProcessRestored = savedInstanceState != null
        Reporter.appAction(logTag, "onCreate isProcessRestored: $isProcessRestored")

        super.onCreate(savedInstanceState)

        (application as App).component!!.inject(this)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
        navController = navHostFragment.navController
        bottomNavigationView = binding.bottomNavigationView
        setupWithNavController(bottomNavigationView, navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.splashFragment -> bottomNavigationView.visibility = View.GONE
                R.id.loginFragment -> bottomNavigationView.visibility = View.GONE
                else -> bottomNavigationView.visibility = View.VISIBLE
            }
        }

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
            if (state.token != "" && mLastConsumedState?.token != state.token) {
                mViewModel.saveUserToken()
            }

            if (state.userFromDB?.token != null && state.userFromDB.token.isNotEmpty()) {
                navigateToFirst()
            }
            mLastConsumedState = state
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

    fun handleErrorWithToastMessage(errorMessage: TextResource) {
        Reporter.appAction(logTag, "handleErrorWithToastMessage")
        Toast.makeText(
            applicationContext,
            errorMessage.toValue(applicationContext),
            Toast.LENGTH_LONG
        ).show()
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

    fun navigateToFirst() {
        Reporter.appAction(logTag, "navigateToFirst")
        navController.navigate(R.id.bottom_menu_graph)
    }

}