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
