package com.example.kotlin_mvvm_app.data.repositories

import com.example.kotlin_mvvm_app.R
import com.example.kotlin_mvvm_app.data.network.ApiService
import com.example.kotlin_mvvm_app.data.repositories.model.search.SearchResult
import com.example.kotlin_mvvm_app.data.repositories.model.track.TracksResult
import com.example.kotlin_mvvm_app.utils.wrappers.Resource
import com.example.kotlin_mvvm_app.utils.wrappers.TextResource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.lang.Exception
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject


class NetworkRepository @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun getSearchResult(token: String, q: String, type: String, offset: Int): Flow<Resource<SearchResult>> =
        flow {
            emit(Resource.progress())
            try {
                val limit = 10 // page length
                val response = apiService.getSearch(token, q, type, limit, offset)
                emit(Resource(response))

            } catch (exception: Exception) {
                when (exception) {
                    is HttpException, is SocketTimeoutException, is UnknownHostException -> {
                        emit(Resource(error = TextResource(textResId = R.string.server_connection_error)))
                        return@flow
                    }
                    else -> throw exception
                }
            }
        }

    suspend fun getLikedTracks(token: String, offset: Int): Flow<Resource<TracksResult>> =
        flow {
            emit(Resource.progress())
            try {
                val limit = 10 // page length
                val response = apiService.getLikedTracks(token, limit, offset)
                emit(Resource(response))
            } catch (exception: Exception) {
                when (exception) {
                    is HttpException, is SocketTimeoutException, is UnknownHostException -> {
                        emit(Resource(error = TextResource(textResId = R.string.server_connection_error)))
                        return@flow
                    }
                    else -> throw exception
                }
            }
        }
}