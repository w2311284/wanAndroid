package com.tong.wanandroid.common.services.http

data class NetworkResponse<T>(
    val data : T,
    val code : Int,
    val msg : String
){
    fun isSuccess(): Boolean {
        return code == 0
    }
}

sealed class RequestResult<out T>{
    data class Success<out T>(val data: T) : RequestResult<T>()
    data class Error(val code: Int = -1,val msg: String? = "") : RequestResult<Nothing>()
}
