package com.tong.wanandroid.common.services.model

data class SeriesModel(
    val author: String,
    val children: List<ClassifyModel>,
    val courseId: Int,
    val cover: String,
    val desc: String,
    val id: Int,
    val lisense: String,
    val lisenseLink: String,
    val name: String,
    val order: Int,
    val parentChapterId: Int,
    val userControlSetTop: Boolean,
    val visible: Int
)
