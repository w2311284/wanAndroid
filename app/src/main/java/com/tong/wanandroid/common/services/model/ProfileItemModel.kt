package com.tong.wanandroid.common.services.model

data class ProfileItemModel(
    val iconRes: Int,
    val title: String,
    var badge: BadgeModel = BadgeModel()
)


data class BadgeModel(
    val type: BadgeType = BadgeType.NONE,
    val number: Int = 0
) {
    enum class BadgeType {
        NONE,
        DOT,
        NUMBER
    }
}