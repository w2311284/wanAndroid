package com.tong.wanandroid.common.services.http

data class NetworkResponse<T>(
    private val data : T? = null,
    private val code : Int? = -1,
    private val msg : String? = ""
){

}

sealed class RequestResult<out T>{
    data class Success<out T>(val data: T) : RequestResult<T>()
    data class Error(val code: Int = -1,val msg: String? = "") : RequestResult<Nothing>()
}
