package com.tong.wanandroid.common.services.model

import android.os.Parcelable

data class UserModel(
    val admin: Boolean = false,
    val chapterTops: List<String> = emptyList(),
    val collectIds: MutableList<String> = mutableListOf(),
    val email: String = "",
    val icon: String = "",
    val id: String = "",
    val nickname: String = "",
    val password: String = "",
    val token: String = "",
    val type: Int = 0,
    val username: String = ""
)
