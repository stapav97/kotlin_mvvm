package com.example.kotlin_mvvm_app.ui

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.kotlin_mvvm_app.data.db.entity.User
import com.example.kotlin_mvvm_app.data.repositories.AuthRepository
import com.example.kotlin_mvvm_app.data.repositories.DatabaseRepository
import com.example.kotlin_mvvm_app.ui.base.BaseViewModel
import com.example.kotlin_mvvm_app.utils.logTag
import com.example.kotlin_mvvm_app.ui.base.commands.ViewCommandProcessor
import com.example.kotlin_mvvm_app.ui.base.commands.enqueue
import com.example.kotlin_mvvm_app.utils.Logger
import com.example.kotlin_mvvm_app.utils.Reporter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainViewCommandProcessor @Inject constructor() : ViewCommandProcessor<MainActivity>()

@Singleton
class MainViewModel @Inject constructor(
    private val mDatabaseRepository: DatabaseRepository,
    private val authRepository: AuthRepository,
    private val mCommands: MainViewCommandProcessor
) : BaseViewModel() {

    private val logTag = logTag()

    data class State(
        val isProgress: Boolean = false,
        val token: String = "",
        val errorMessage: String = "",
        val userFromDB: User? = null
    )

    fun observeCommands(owner: LifecycleOwner, view: MainActivity) {
        mCommands.observe(owner, view)
    }

    private val mState: MutableLiveData<State>
    fun state(): LiveData<State> = mState

    init {
        mState = MutableLiveData(State())
    }
    fun checkCodeOnResume() {
        mCommands.enqueue { it.checkIsCodeExist() }
    }

    fun onCodeRetrieve(code: String) {
        Reporter.appAction(logTag, "onCodeRetrieve:$code")
        getAccessToken(code)
    }

    fun onCodeErrorRetrieve(error: String) {
        Reporter.appAction(logTag, "onCodeErrorRetrieve")

        val oldState = mState.value!!
        mState.value = oldState.copy(errorMessage = error)
    }
    private fun getAccessToken(code: String) {
        Reporter.appAction(logTag, "getAccessToken")
        viewModelScope.launch {
            authRepository.getAccessToken(code)
                .flowOn(Dispatchers.IO)
                .onEach { flowResponse ->
                    val oldState = mState.value!!

                    flowResponse.peek(
                        onProgress = {
                            Logger.d(logTag, "getAccessToken onProgress")
                            mState.value = oldState.copy(isProgress = true)
                        },
                        onData = { data ->
                            Logger.d(logTag, "getAccessToken onData $data")
                            mState.value = oldState.copy(
                                isProgress = false,
                                token = data.accessToken
                            )
                        },
                        onError = { message ->
                            Logger.d(logTag, "getAccessToken onError $message")
                            mState.value = oldState.copy(isProgress = false)
                            mCommands.enqueue { it.handleErrorWithToastMessage(message) }
                        },
                    )
                }.collect()
        }

    }

    fun saveUserToken() {
        Reporter.appAction(logTag, "saveUserToken")

        val oldState = mState.value!!
        mState.value = oldState.copy(isProgress = true)

        viewModelScope.launch(Dispatchers.IO) {
            mDatabaseRepository.insertUser(User(token = oldState.token))
            withContext(Dispatchers.Main) {
                mState.value = oldState.copy(isProgress = false)
                mCommands.enqueue { it.navigateToFirst() }
            }
        }
    }

}