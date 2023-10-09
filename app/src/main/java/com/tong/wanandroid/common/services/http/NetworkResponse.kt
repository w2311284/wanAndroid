package com.tong.wanandroid.common.services.http

sealed class NetworkResponse<out T : Any>{
    /**
     * 成功
     */
    data class Success<T : Any>(val data: T) : NetworkResponse<T>()

    /**
     * 业务错误
     */
    data class BizError(val errorCode: Int = 0, val errorMessage: String = "") :
        NetworkResponse<Nothing>()

    /**
     * 其他错误
     */
    data class UnknownError(val throwable: Throwable) : NetworkResponse<Nothing>()
}
