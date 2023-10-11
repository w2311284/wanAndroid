package com.tong.wanandroid.common.services.http

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn


object RequestHelper {
    suspend fun <T> request(call: suspend () -> NetworkResponse<T>) : Flow<RequestResult<NetworkResponse<T>>> {
        return flow {
            val response : NetworkResponse<T> = call.invoke()
            if (response.code == 0) {
                emit(RequestResult.Success(response))
            } else {
                emit(RequestResult.Error(response.code, response.msg))
            }
        }.flowOn(Dispatchers.IO).catch { throwable: Throwable ->
            emit(RequestResult.Error(-1, throwable.message))
        }
    }
}