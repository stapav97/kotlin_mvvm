package com.example.kotlin_mvvm_app.ui.liked

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.kotlin_mvvm_app.data.repositories.DatabaseRepository
import com.example.kotlin_mvvm_app.data.repositories.NetworkRepository
import com.example.kotlin_mvvm_app.data.repositories.model.track.TrackItemResult
import com.example.kotlin_mvvm_app.ui.MainViewCommandProcessor
import com.example.kotlin_mvvm_app.ui.base.BaseViewModel
import com.example.kotlin_mvvm_app.ui.liked.list.TrackListItem
import com.example.kotlin_mvvm_app.utils.Logger
import com.example.kotlin_mvvm_app.utils.Reporter
import com.example.kotlin_mvvm_app.utils.logTag
import com.example.kotlin_mvvm_app.utils.tokenFormatter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

class LikedViewModel @Inject constructor(
    private val mMainCommands: MainViewCommandProcessor,
    private val mDatabaseRepository: DatabaseRepository,
    private val mNetworkRepository: NetworkRepository,
) : BaseViewModel() {

    private val logTag = logTag()

    data class State(
        val isProgress: Boolean = false,
        val trackList: MutableList<TrackListItem>,
        val offset: Int = 0,
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
        getUserToken()
    }

    private fun getUserToken() {
        Reporter.appAction(logTag, "getUserToken")
        val oldState = mState.value!!

        viewModelScope.launch {
            mDatabaseRepository.getUserToken().collect() { data ->
                mState.value = oldState.copy(token = data.orEmpty().tokenFormatter())
            }
        }
    }

    fun getLikedTracks() {
        Reporter.appAction(logTag, "getLikedTracks")

        val oldState = mState.value!!


        //TODO implement offset(page)
        mNetworkRepository.initPageToDefValue()
        viewModelScope.launch {
            mNetworkRepository.getLikedTracks(oldState.token, oldState.offset)
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
                                trackList = data.items.map { it.toRecycleItemMapper() }.toMutableList(),
                                isLoadListEmpty = data.items.isEmpty(),
                                offset = data.offset + data.limit
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

    private fun loadNextPage() {
        Reporter.appAction(logTag, "loadNextPage")

        val oldState = mState.value!!

        viewModelScope.launch {
            mNetworkRepository.getLikedTracks(oldState.token, oldState.offset)
                .flowOn(Dispatchers.IO)
                .onEach { flowResponse ->
                    flowResponse.peek(
                        onProgress = {
                            Logger.d(logTag, "getLikedTracks onProgress")
                            mState.value = oldState.copy(isProgress = true)
                        },
                        onData = { data ->
                            Logger.d(logTag, "getLikedTracks onData $data")

                            val newList = oldState.trackList.toMutableList()
                            newList.addAll(data.items.map { it.toRecycleItemMapper() })

                            mState.value = oldState.copy(
                                isProgress = false,
                                trackList = newList,
                                isLoadListEmpty = data.items.isEmpty(),
                                offset = data.offset + data.limit
                            )
                        },
                        onError = { message ->
                            Logger.d(logTag, "getLikedTracks onError $message")
                            mState.value = oldState.copy(
                                isProgress = false,
                                errorMessage = message.toString()
                            )
                        })
                }.collect()
        }
    }

    fun onScrolledToEnd() {
        Reporter.appAction(logTag, "onScrolledToEnd")

        val oldState = mState.value!!
        if (!oldState.isProgress && !oldState.isLoadListEmpty) {
            loadNextPage()
        }
    }

    fun onListItemClick(item: TrackListItem) {
        Reporter.userAction(logTag, "onListItemClick")
    }

    fun errorMessageShown() {
        Reporter.appAction(logTag, "errorMessageShown")

        val oldState = mState.value!!
        mState.value = oldState.copy(errorMessage = "")
    }

    private fun TrackItemResult.toRecycleItemMapper(): TrackListItem {
        return TrackListItem(
            id = this.track.id,
            name = this.track.name,
            type = this.track.type,
        )
    }
}

