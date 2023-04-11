package com.example.kotlin_mvvm_app.data.repositories

import com.example.kotlin_mvvm_app.BuildConfig
import com.example.kotlin_mvvm_app.R
import com.example.kotlin_mvvm_app.data.network.AuthApiService
import com.example.kotlin_mvvm_app.data.repositories.model.AccessToken
import com.example.kotlin_mvvm_app.utils.wrappers.Resource
import com.example.kotlin_mvvm_app.utils.wrappers.TextResource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.lang.Exception
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.*
import javax.inject.Inject


class AuthRepository @Inject constructor(
    private val authApiService: AuthApiService,
    private val mDatabaseRepository: DatabaseRepository
) {
    suspend fun getAccessToken(code: String): Flow<Resource<AccessToken>> = flow {
        emit(Resource.progress())
        try {
            val string = BuildConfig.CLIENT_ID + ":" + BuildConfig.CLIENT_SECRET
            val encodedString: String =
                "Basic " + Base64.getEncoder().encodeToString(string.toByteArray())
            val response = authApiService.getAccessToken(
                encodedString,
                BuildConfig.AUTHORIZATION_CALLBACK_URL,
                "authorization_code",
                code
            )
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

    suspend fun getRefreshToken(): Flow<Resource<AccessToken>> = flow {
        emit(Resource.progress())
        try {
            val string = BuildConfig.CLIENT_ID + ":" + BuildConfig.CLIENT_SECRET
            val encodedString: String =
                "Basic " + Base64.getEncoder().encodeToString(string.toByteArray())
            val refreshToken: String? = mDatabaseRepository.getRefreshToken()
            val response = authApiService.getRefreshToken(encodedString, "refresh_token", refreshToken)
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
