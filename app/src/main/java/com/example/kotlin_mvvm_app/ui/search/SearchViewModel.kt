package com.example.kotlin_mvvm_app.ui.search

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.kotlin_mvvm_app.data.db.entity.SearchHistory
import com.example.kotlin_mvvm_app.data.repositories.DatabaseRepository
import com.example.kotlin_mvvm_app.data.repositories.NetworkRepository
import com.example.kotlin_mvvm_app.ui.MainViewCommandProcessor
import com.example.kotlin_mvvm_app.ui.base.BaseViewModel
import com.example.kotlin_mvvm_app.ui.base.commands.ViewCommandProcessor
import com.example.kotlin_mvvm_app.ui.search.searchList.SearchListItem
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
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SearchViewModel @Inject constructor(
    private val mMainCommands: MainViewCommandProcessor,
    private val mDatabaseRepository: DatabaseRepository,
    private val mNetworkRepository: NetworkRepository,
) : BaseViewModel() {

    private val logTag = logTag()

    data class State(
        val isProgress: Boolean = false,
        val searchList: MutableList<SearchListItem>,
        val isLoadListEmpty: Boolean = false,
        val errorMessage: String = "",
        val queryString: String = "",
        val token: String = "",
        val offset: Int = 0,
    ) {
        fun hasSearchItems(): Boolean {
            return searchList.isNotEmpty()
        }
    }

    private val mCommands: ViewCommandProcessor<SearchFragment> = ViewCommandProcessor()
    fun observeCommands(owner: LifecycleOwner, view: SearchFragment) {
        mCommands.observe(owner, view)
    }

    private val mState: MutableLiveData<State>
    fun state(): LiveData<State> = mState

    init {
        mState = MutableLiveData(State(searchList = mutableListOf()))
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
            mNetworkRepository.getSearchResult(oldState.token, queryString, Types.ARTIST.typeName, 0)
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
                                searchList = data.artists.items.map {
                                    SearchListItem(
                                        id = it.id,
                                        name = it.name,
                                        type = it.type,
                                        image = it.images.firstOrNull()?.url
                                    )
                                }.toMutableList(),
                                isLoadListEmpty = data.artists.items.isEmpty(),
                                offset = 0 + data.artists.limit
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

    private fun loadNextPage() {
        Reporter.appAction(logTag, "loadNextPage")

        val oldState = mState.value!!

        viewModelScope.launch {
            mNetworkRepository.getSearchResult(oldState.token, oldState.queryString, Types.ARTIST.typeName, oldState.offset)
                .flowOn(Dispatchers.IO)
                .onEach { flowResponse ->
                    flowResponse.peek(
                        onProgress = {
                            Logger.d(logTag, "getSearchResult onProgress")
                            mState.value = oldState.copy(isProgress = true)
                        },
                        onData = { data ->
                            Logger.d(logTag, "getSearchResult onData $data")

                            val newList = oldState.searchList.toMutableList()
                            newList.addAll(data.artists.items.map {
                                SearchListItem(
                                    id = it.id,
                                    name = it.name,
                                    type = it.type,
                                    image = it.images.firstOrNull()?.url
                                )
                            })

                            mState.value = oldState.copy(
                                isProgress = false,
                                searchList = newList,
                                isLoadListEmpty = data.artists.items.isEmpty(),
                                offset = data.artists.offset + data.artists.limit
                            )
                        },
                        onError = { message ->
                            Logger.d(logTag, "getSearchResult onError $message")
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

    fun onSearchListItemClick(item: SearchListItem) {
        Reporter.userAction(logTag, "onListItemClick")

        val oldState = mState.value!!
        mState.value = oldState.copy(isProgress = true)

        viewModelScope.launch(Dispatchers.IO) {
            mDatabaseRepository.insertSearchHistory(
                SearchHistory(
                    id = item.id,
                    name = item.name,
                    type = item.type,
                    image = item.image
                )
            )
            withContext(Dispatchers.Main) {
                mState.value = oldState.copy(
                    isProgress = false,
                    errorMessage = "Insert complete"
                )
            }
        }
    }

    private fun onEmptySearchText() {
        Reporter.appAction(logTag, "onEmptySearchText")

        val oldState = mState.value!!
        mState.value = oldState.copy(searchList = mutableListOf())
    }

    fun onSearchTextChanged(text: String) {
        Reporter.appAction(logTag, "onSearchTextChanged")

        val oldState = mState.value!!

        if (oldState.queryString == text) return
        if (oldState.isProgress) return

        mState.value = oldState.copy(queryString = text)

        if (text.isNotBlank())
            getSearchResult(text)
        else
            onEmptySearchText()
    }

    fun errorMessageShown() {
        Reporter.appAction(logTag, "errorMessageShown")

        val oldState = mState.value!!
        mState.value = oldState.copy(errorMessage = "")
    }

}