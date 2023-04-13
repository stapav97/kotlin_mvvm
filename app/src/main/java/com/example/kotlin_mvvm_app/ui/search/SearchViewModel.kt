package com.example.kotlin_mvvm_app.ui.search

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.kotlin_mvvm_app.data.repositories.DatabaseRepository
import com.example.kotlin_mvvm_app.data.repositories.NetworkRepository
import com.example.kotlin_mvvm_app.data.repositories.model.track.TrackItem
import com.example.kotlin_mvvm_app.ui.MainViewCommandProcessor
import com.example.kotlin_mvvm_app.ui.base.BaseViewModel
import com.example.kotlin_mvvm_app.ui.base.commands.ViewCommandProcessor
import com.example.kotlin_mvvm_app.utils.Logger
import com.example.kotlin_mvvm_app.utils.Reporter
import com.example.kotlin_mvvm_app.utils.enums.Types
import com.example.kotlin_mvvm_app.utils.logTag
import com.example.kotlin_mvvm_app.utils.tokenFormatter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

class SearchViewModel @Inject constructor(
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
        fun hasReposItems(): Boolean {
            return trackList.isNotEmpty()
        }
    }

    private val mCommands: ViewCommandProcessor<SearchFragment> = ViewCommandProcessor()
    fun observeCommands(owner: LifecycleOwner, view: SearchFragment) {
        mCommands.observe(owner, view)
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

    private fun getSearchResult(queryString: String) {
        Reporter.appAction(logTag, "getSearchResult")

        val oldState = mState.value!!

        //TODO implement offset(page)

        viewModelScope.launch {
            mNetworkRepository.getSearchResult(oldState.token, queryString, Types.TRACK.typeName, 0)
                .flowOn(Dispatchers.IO)
                .onEach { flowResponse ->
                    flowResponse.peek(
                        onProgress = {
                            Logger.d(logTag, "getSearchResult onProgress")
                            mState.value = oldState.copy(isProgress = true)
                        },
                        onData = { data ->
                            Logger.d(logTag, "getSearchResult onData $data")
                            mState.value = oldState.copy(
                                isProgress = false,
                                isLoadListEmpty = data.tracks.items.isEmpty()
                            )
                        },
                        onError = { message ->
                            Logger.d(logTag, "getSearchResult onError $message")
                            mState.value = oldState.copy(
                                isProgress = false,
                                errorMessage = message.toString()
                            )
                        }
                    )
                }.collect()
        }
    }

    fun onSearchTextChanged(text: String) {
        Reporter.appAction(logTag, "onSearchTextChanged")

        val oldState = mState.value!!

        if (oldState.searchText == text) return
        if (oldState.isProgress) return

        mState.value = oldState.copy(searchText = text)

        if (text.isNotBlank())
            getSearchResult(text)
//        else
//            onEmptySearchText()
    }

    fun errorMessageShown() {
        Reporter.appAction(logTag, "errorMessageShown")

        val oldState = mState.value!!
        mState.value = oldState.copy(errorMessage = "")
    }

}