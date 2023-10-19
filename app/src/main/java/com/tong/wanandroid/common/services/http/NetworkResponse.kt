package com.tong.wanandroid.common.services.http

data class NetworkResponse<T>(
    val data : T,
    val errorCode : Int,
    val errorMsg : String
){
    fun isSuccess(): Boolean {
        return errorCode == 0
    }
}

sealed class RequestResult<out T>{
    data class Success<out T>(val data: T) : RequestResult<T>()
    data class Error(val errorCode: Int = -1,val errorMsg: String? = "") : RequestResult<Nothing>()
}
