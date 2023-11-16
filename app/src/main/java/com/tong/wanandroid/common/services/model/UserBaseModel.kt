package com.tong.wanandroid.common.services.model

data class UserBaseModel(
    val coinInfo: CoinModel = CoinModel(),
    val collectArticleInfo: CollectArticleModel = CollectArticleModel(),
    val userInfo: UserModel = UserModel()
)
