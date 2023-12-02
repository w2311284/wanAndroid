package com.tong.wanandroid.common.services.model

data class ShareModel(
    val coinInfo: CoinModel,
    val shareArticles: PageModel<ArticleModel>
)
