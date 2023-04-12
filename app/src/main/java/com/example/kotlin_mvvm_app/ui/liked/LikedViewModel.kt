package com.example.kotlin_mvvm_app.ui.liked

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.kotlin_mvvm_app.data.repositories.DatabaseRepository
import com.example.kotlin_mvvm_app.data.repositories.NetworkRepository
import com.example.kotlin_mvvm_app.data.repositories.model.track.TrackItem
import com.example.kotlin_mvvm_app.ui.MainViewCommandProcessor
import com.example.kotlin_mvvm_app.ui.base.BaseViewModel
import com.example.kotlin_mvvm_app.utils.Logger
import com.example.kotlin_mvvm_app.utils.Reporter
import com.example.kotlin_mvvm_app.utils.logTag
import com.example.kotlin_mvvm_app.utils.tokenFormatter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LikedViewModel @Inject constructor(
    private val mMainCommands: MainViewCommandProcessor,
    private val mDatabaseRepository: DatabaseRepository,
    private val mNetworkRepository: NetworkRepository,
) : BaseViewModel() {

    private val logTag = logTag()

    data class State(
        val isProgress: Boolean = false,
        val trackList: MutableList<TrackItem>,
        val isLoadListEmpty: Boolean = false,
        val errorMessage: String = "",
        val searchText: String = "",
        val token: String = "",
    ) {
        fun hasTrackItems(): Boolean {
            return trackList.isNotEmpty()
        }
    }

    private val mState: MutableLiveData<State>

    fun state(): LiveData<State> = mState

    init {
        mState = MutableLiveData(State(trackList = mutableListOf()))
        getUserPrefs()
    }

    private fun getUserPrefs() {
        Reporter.appAction(logTag, "getUserPrefs")

        val oldState = mState.value!!
        mState.value = oldState.copy(isProgress = true)

        viewModelScope.launch(Dispatchers.IO) {
            val userToken: String? = mDatabaseRepository.getUserToken()
            withContext(Dispatchers.Main) {
                mState.value =
                    oldState.copy(isProgress = false, token = userToken.orEmpty().tokenFormatter())
            }
        }

    }

      fun getLikedTracks() {
        Reporter.appAction(logTag, "getLikedTracks")

        val oldState = mState.value!!

        //TODO implement offset(page)

        viewModelScope.launch {
            mNetworkRepository.getLikedTracks(oldState.token, 0)
                .flowOn(Dispatchers.IO)
                .onEach { flowResponse ->
                    flowResponse.peek(
                        onProgress = {
                            Logger.d(logTag, "getLikedTracks onProgress")
                            mState.value = oldState.copy(isProgress = true)
                        },
                        onData = { data ->
                            Logger.d(logTag, "getLikedTracks onData $data")
                            mState.value = oldState.copy(
                                isProgress = false,
                                isLoadListEmpty = data.items.isEmpty()
                            )
                        },
                        onError = { message ->
                            Logger.d(logTag, "getLikedTracks onError $message")
                            mState.value = oldState.copy(
                                isProgress = false,
                                errorMessage = message.toString()
                            )
                        }
                    )
                }.collect()
        }
    }

    fun errorMessageShown() {
        Reporter.appAction(logTag, "errorMessageShown")

        val oldState = mState.value!!
        mState.value = oldState.copy(errorMessage = "")
    }


}