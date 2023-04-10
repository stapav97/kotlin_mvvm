package com.example.kotlin_mvvm_app.ui.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.kotlin_mvvm_app.data.db.entity.User
import com.example.kotlin_mvvm_app.data.repositories.DatabaseRepository
import com.example.kotlin_mvvm_app.ui.base.BaseViewModel
import com.example.kotlin_mvvm_app.utils.Reporter
import com.example.kotlin_mvvm_app.utils.logTag
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SplashViewModel @Inject constructor(
    private val mDatabaseRepository: DatabaseRepository,
) : BaseViewModel() {

    private val logTag = logTag()

    data class State(
        val userFromDB: User? = null,
    )

    private val mState: MutableLiveData<State>
    fun state(): LiveData<State> = mState

    init {
        mState = MutableLiveData(State())
    }

    fun checkUserInDBOnStart() {
        Reporter.appAction(logTag, "navigateToFirstFragment")

        val oldState = mState.value!!
        viewModelScope.launch(Dispatchers.IO) {
            delay(1500)
            val user = mDatabaseRepository.getUser()
            withContext(Dispatchers.Main) {
                if (user != null)
                    mState.value = oldState.copy(userFromDB = user)
                else
                    mState.value = oldState.copy(userFromDB = User())
            }
        }
    }
}