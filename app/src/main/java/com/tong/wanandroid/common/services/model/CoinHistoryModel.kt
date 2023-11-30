package com.tong.wanandroid.common.services.model

data class CoinHistoryModel(
    var coinCount: Int,
    var date: Long,
    var desc: String,
    var id: Int,
    var type: Int,
    var reason: String,
    var userId: Int,
    var userName: String
)
